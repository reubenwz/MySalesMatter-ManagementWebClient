/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalReservationEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface RentalReservationEntitySessionBeanLocal {
    public RentalReservationEntity createRentalReservation(Date startDate, Date endDate, BigDecimal totalPrice, String issues, ReservationStatus reservationStatus, Long listingId, Long depositId) throws SalesTransactionNotFoundException, RentalReservationExistException, UnknownPersistenceException, InputDataValidationException, ListingNotFoundException, CreateNewRentalReservationException;
    public List<RentalReservationEntity> retrieveAllRentalReservation();
    public void deleteRentalReservation(Long rentalReservationId) throws RentalReservationNotFoundException;
    public void updateReservationStatus(Long rentalReservationId, ReservationStatus status) throws RentalReservationNotFoundException;
    public RentalReservationEntity retrieveRentalReservationById(Long id) throws RentalReservationNotFoundException;
}
