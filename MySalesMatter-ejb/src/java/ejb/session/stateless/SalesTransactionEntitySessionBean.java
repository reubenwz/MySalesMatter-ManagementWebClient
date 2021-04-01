/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.UserEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.SalesTransactionEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.Status;
import util.exception.CreateNewTransactionException;
import util.exception.InputDataValidationException;
import util.exception.OfferNotFoundException;
import util.exception.SalesTransactionExistException;
import util.exception.SalesTransactionNotFoundException;
import util.exception.TransactionDeletionException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author sylvia
 */
@Stateless
public class SalesTransactionEntitySessionBean implements SalesTransactionEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    @EJB
    UserEntitySessionBeanLocal generalUserEntitySessionBeanLocal;
    
    @EJB
    OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;
    
    @EJB
    UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    

    public SalesTransactionEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public SalesTransactionEntity createSalesTransaction(Long offerId, Long userId, String status, Date transactionDate, BigDecimal totalAmt) throws UnknownPersistenceException, InputDataValidationException, UserNotFoundException, CreateNewTransactionException, SalesTransactionExistException, OfferNotFoundException {

        try {
            UserEntity userEntity = generalUserEntitySessionBeanLocal.retrieveUserById(userId);
            OfferEntity offerEntity = offerEntitySessionBeanLocal.retrieveOfferById(offerId);
            SalesTransactionEntity salesTransactionEntity = new SalesTransactionEntity();
            salesTransactionEntity.setUser(userEntity);
            salesTransactionEntity.setOffer(offerEntity);
            
            if (status.toLowerCase().equals("paid")) {
                salesTransactionEntity.setStatus(Status.PAID);
            } else {
                salesTransactionEntity.setStatus(Status.NOT_PAID);
            }
            salesTransactionEntity.setTotalAmt(totalAmt);
            salesTransactionEntity.setTransactionDate(transactionDate);
            
            Set<ConstraintViolation<SalesTransactionEntity>> constraintViolations = validator.validate(salesTransactionEntity);

            if (constraintViolations.isEmpty()) {
                try {
                    em.persist(salesTransactionEntity);
                    userEntity.getTransactions().add(salesTransactionEntity);
                    em.merge(userEntity);
                    offerEntity.setSales(salesTransactionEntity);
                    em.merge(offerEntity);
                    em.flush();
                    
                } catch (PersistenceException ex) {
                    if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {

                        if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                            throw new SalesTransactionExistException("Sales Transaction already exists");
                        } else {
                            throw new UnknownPersistenceException(ex.getMessage());
                        }

                    }
                }
                return salesTransactionEntity;
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (UserNotFoundException ex) {
            throw new CreateNewTransactionException("An error has occurred while creating the new Listing: " + ex.getMessage());
        }
        
    }
    
    @Override
    public List<SalesTransactionEntity> getSalesTransactionByUserId(Long userId) throws UserNotFoundException {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserById(userId);

            Query query = em.createQuery("SELECT l FROM SalesTransactionEntity l WHERE l.user.userId = :inUserId");
            query.setParameter("inUserId", userId);
            List<SalesTransactionEntity> list = query.getResultList();
            for (SalesTransactionEntity l : list) {
                l.getOffer();
                l.getUser();
            }
            return list;
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User with this id, " + userId + ", does not exist!");
        }

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<SalesTransactionEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }

    @Override
    public List<SalesTransactionEntity> retrieveAllTransactions() {
        Query query = em.createQuery("SELECT t FROM TransactionEntity t");

        List<SalesTransactionEntity> list = query.getResultList();
        return list;
    }

    @Override
    public void deleteTransaction(Long transactionId) throws SalesTransactionNotFoundException, TransactionDeletionException {
        try {
            SalesTransactionEntity salesTransactionEntity = retrieveTransactionById(transactionId);
            if (salesTransactionEntity.getStatus().toString().toLowerCase().equals("PAID")) {
                throw new TransactionDeletionException("Transaction with this id, " + transactionId + " cannot be deleted as it has been paid.");
            } else {
                OfferEntity offerEntity = salesTransactionEntity.getOffer();
                ListingEntity listing = offerEntity.getListing();
                UserEntity user = salesTransactionEntity.getUser();
                user.getTransactions().remove(salesTransactionEntity);
                listing.getOffers().remove(offerEntity);
                em.merge(user);
                em.remove(offerEntity);
                em.merge(listing);
                em.remove(salesTransactionEntity);
            }
            
        } catch (SalesTransactionNotFoundException ex) {
            throw new SalesTransactionNotFoundException("Transaction with this id, " + transactionId + ", does not exist!");
        }
    }

    @Override
    public void updateStatus(Long transactionId, String status) throws SalesTransactionNotFoundException {
        try {
            SalesTransactionEntity transactionEntity = retrieveTransactionById(transactionId);
            if (status.toLowerCase().equals("paid")) {
                transactionEntity.setStatus(Status.PAID);
            } else {
                transactionEntity.setStatus(Status.NOT_PAID);
            }
        } catch (SalesTransactionNotFoundException ex) {
            throw new SalesTransactionNotFoundException("Transaction with this id, " + transactionId + ", does not exist!");
        }
    }

    @Override
    public SalesTransactionEntity retrieveTransactionById(Long id) throws SalesTransactionNotFoundException {
        SalesTransactionEntity transactionEntity = em.find(SalesTransactionEntity.class, id);
        transactionEntity.getOffer();

        if (transactionEntity != null) {
            return transactionEntity;
        } else {
            throw new SalesTransactionNotFoundException("Transaction with this id, " + id + ", does not exist!");
        }
    }
}
