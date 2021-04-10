/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OfferEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewOfferException;
import util.exception.DeleteOfferException;
import util.exception.InputDataValidationException;
import util.exception.OfferNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Yuki
 */
@Local
public interface OfferEntitySessionBeanLocal {
    
    public OfferEntity createNewOffer(OfferEntity newOfferEntity, Long userId, Long listingId) throws UnknownPersistenceException, InputDataValidationException, CreateNewOfferException, UserNotFoundException;
    
    public List<OfferEntity> retrieveAllOffers();
    
    public OfferEntity retrieveOfferById(Long offerId) throws OfferNotFoundException;
    
    public List<OfferEntity> retrieveOffersByUserId(Long userId);
    
    public List<OfferEntity> retrieveOffersByListingId(Long listingId);
    
    public void updateOffer(OfferEntity offerEntity) throws InputDataValidationException, OfferNotFoundException;
    
    public void deleteOffer(Long offerId) throws DeleteOfferException;
    
    public void acceptOffer(Long offerId) throws OfferNotFoundException;
    public void doSetPaid(Long offerId) throws OfferNotFoundException;
}
