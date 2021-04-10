/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.UserEntity;
import entity.ListingEntity;
import entity.OfferEntity;
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
import util.exception.CreateNewOfferException;
import util.exception.DeleteOfferException;
import util.exception.InputDataValidationException;
import util.exception.ListingNotFoundException;
import util.exception.OfferNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Yuki
 */
@Stateless
public class OfferEntitySessionBean implements OfferEntitySessionBeanLocal {

    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    @EJB
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager em;
    
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public OfferEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public OfferEntity createNewOffer(OfferEntity newOfferEntity, Long userId, Long listingId) throws UnknownPersistenceException, InputDataValidationException, CreateNewOfferException, UserNotFoundException {
        Set<ConstraintViolation<OfferEntity>>constraintViolations = validator.validate(newOfferEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserById(userId);
                userEntity.getOffers().add(newOfferEntity);
                
                ListingEntity listingEntity = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);
                listingEntity.getOffers().add(newOfferEntity);
                
                em.persist(newOfferEntity);
                em.merge(userEntity);
                em.merge(listingEntity);
                em.flush();

                return newOfferEntity;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {                  
                    throw new UnknownPersistenceException(ex.getMessage());                   
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            catch(UserNotFoundException | ListingNotFoundException ex)
            {
                throw new CreateNewOfferException("An error has occurred while creating the new offer: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<OfferEntity> retrieveAllOffers() {
        Query query = em.createQuery("SELECT o FROM OfferEntity o ORDER BY o.offerDate desc");
        return query.getResultList();
    }
    
    @Override
    public OfferEntity retrieveOfferById(Long offerId) throws OfferNotFoundException {
        OfferEntity offerEntity = em.find(OfferEntity.class, offerId);
        if (offerEntity != null) {
            return offerEntity;
        } else {
            throw new OfferNotFoundException("Offer ID " + offerId + " does not exist!");
        }
    }
    
    @Override
    public List<OfferEntity> retrieveOffersByUserId(Long userId) {
        Query query = em.createQuery("SELECT o FROM OfferEntity o WHERE o.user.userId =:inUserId ");
        query.setParameter("inUserId", userId);
        return query.getResultList();
    }
    
    @Override
    public List<OfferEntity> retrieveOffersByListingId(Long listingId) {
        Query query = em.createQuery("SELECT o FROM OfferEntity o WHERE o.listing.listingId =:inListingId");
        query.setParameter("inListingId", listingId);
        return query.getResultList();
    }
    
    public void updateOffer(OfferEntity offerEntity) throws InputDataValidationException, OfferNotFoundException {
        if(offerEntity != null && offerEntity.getOfferId()!= null)
        {
            Set<ConstraintViolation<OfferEntity>>constraintViolations = validator.validate(offerEntity);
        
            if(constraintViolations.isEmpty())
            {
                OfferEntity offerEntityToUpdate = retrieveOfferById(offerEntity.getOfferId());

                offerEntityToUpdate.setAccepted(true);
                offerEntityToUpdate.setUser(offerEntity.getUser());
                offerEntityToUpdate.setListing(offerEntity.getListing());
                offerEntityToUpdate.setOfferDate(offerEntity.getOfferDate());
                offerEntityToUpdate.setOfferType(offerEntity.getOfferType());
                offerEntityToUpdate.setTotalPrice(offerEntity.getTotalPrice());
                offerEntityToUpdate.setSales(offerEntity.getSales());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new OfferNotFoundException("Offer ID not provided for Offer to be updated");
        }
    }
    
    
    @Override
    public void deleteOffer(Long offerId) throws DeleteOfferException {
        OfferEntity offerEntityToBeRemoved = em.find(OfferEntity.class, offerId);
        if (offerEntityToBeRemoved.getSales() != null) {
            throw new DeleteOfferException("Offer ID " + offerId + " is associated with existing sale transaction and cannot be deleted!");
        } else {
            UserEntity generalUserEntity = offerEntityToBeRemoved.getUser();
            generalUserEntity.getOffers().remove(offerEntityToBeRemoved);

            ListingEntity listingEntity = offerEntityToBeRemoved.getListing();
            listingEntity.getOffers().remove(offerEntityToBeRemoved);
            em.remove(offerEntityToBeRemoved);
        }
    }
    
    @Override
    public void acceptOffer(Long offerId) throws OfferNotFoundException {
        OfferEntity offerEntityToAccept = retrieveOfferById(offerId);
        offerEntityToAccept.setAccepted(true);
        System.out.println("accepting offer in sb");
        em.merge(offerEntityToAccept);
        em.flush();
    }
    
    @Override
    public void doSetPaid(Long offerId) throws OfferNotFoundException {
        OfferEntity offerEntityToSetPaid = retrieveOfferById(offerId);
        offerEntityToSetPaid.setPaid(true);
        em.merge(offerEntityToSetPaid);
        em.flush();
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<OfferEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}
