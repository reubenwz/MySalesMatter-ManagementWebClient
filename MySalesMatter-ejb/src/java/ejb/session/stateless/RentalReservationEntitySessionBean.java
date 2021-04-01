/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ListingEntity;
import entity.RentalReservationEntity;
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
import util.enumeration.ReservationStatus;
import util.exception.CreateNewRentalReservationException;
import util.exception.InputDataValidationException;
import util.exception.ListingNotFoundException;
import util.exception.RentalReservationExistException;
import util.exception.RentalReservationNotFoundException;
import util.exception.SalesTransactionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author sylvia
 */
@Stateless
public class RentalReservationEntitySessionBean implements RentalReservationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    @EJB
    private SalesTransactionEntitySessionBeanLocal transactionEntitySessionBeanLocal;

    public RentalReservationEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public RentalReservationEntity createRentalReservation(Date startDate, Date endDate, BigDecimal totalPrice, String issues, ReservationStatus reservationStatus, Long listingId, Long depositId) throws SalesTransactionNotFoundException, RentalReservationExistException, UnknownPersistenceException, InputDataValidationException, ListingNotFoundException, CreateNewRentalReservationException {

        try {
            ListingEntity listingEntity = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);
            RentalReservationEntity rentalReservation = new RentalReservationEntity(startDate, endDate, totalPrice, issues, reservationStatus);
            rentalReservation.setListing(listingEntity);

            Set<ConstraintViolation<RentalReservationEntity>> constraintViolations = validator.validate(rentalReservation);

            if (constraintViolations.isEmpty()) {
                try {
                    em.persist(rentalReservation);
                    em.flush();
                } catch (PersistenceException ex) {
                    if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {

                        if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                            throw new RentalReservationExistException("Rental Reservation already exists");
                        } else {
                            throw new UnknownPersistenceException(ex.getMessage());
                        }

                    }
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
            return rentalReservation;
        } catch (ListingNotFoundException ex) {
            throw new CreateNewRentalReservationException("An error has occurred while creating the new Listing: " + ex.getMessage());
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalReservationEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }

    @Override
    public List<RentalReservationEntity> retrieveAllRentalReservation() {
        Query query = em.createQuery("SELECT r FROM RentalReservationEntity r");

        List<RentalReservationEntity> list = query.getResultList();
        for (RentalReservationEntity rentalReservationEntity : list) {
            rentalReservationEntity.getListing();
        }
        return list;
    }

    @Override
    public void deleteRentalReservation(Long rentalReservationId) throws RentalReservationNotFoundException {
        try {
            RentalReservationEntity rentalReservationEntity = retrieveRentalReservationById(rentalReservationId);
            ListingEntity listingEntity = rentalReservationEntity.getListing();
            listingEntity.getReservations().remove(rentalReservationEntity);
            em.merge(listingEntity);
            em.remove(rentalReservationEntity);
        } catch (RentalReservationNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation with this id, " + rentalReservationId + ", does not exist!");
        }
    }

    @Override
    public void updateReservationStatus(Long rentalReservationId, ReservationStatus status) throws RentalReservationNotFoundException {
        try {
            RentalReservationEntity rentalReservationEntity = retrieveRentalReservationById(rentalReservationId);
            rentalReservationEntity.setReservationStatus(status);
            em.merge(rentalReservationEntity);
        } catch (RentalReservationNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation with this id, " + rentalReservationId + ", does not exist!");
        }
    }

    @Override
    public RentalReservationEntity retrieveRentalReservationById(Long id) throws RentalReservationNotFoundException {
        RentalReservationEntity rentalReservationEntity = em.find(RentalReservationEntity.class, id);

        if (rentalReservationEntity != null) {
            rentalReservationEntity.getListing();
            return rentalReservationEntity;
        } else {
            throw new RentalReservationNotFoundException("Rental Reservation with this id, " + id + ", does not exist!");
        }
    }
}
