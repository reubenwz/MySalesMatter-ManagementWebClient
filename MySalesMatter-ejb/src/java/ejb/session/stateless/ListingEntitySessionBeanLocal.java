/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ListingEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewListingException;
import util.exception.DeleteListingException;
import util.exception.InputDataValidationException;
import util.exception.ListingNotFoundException;
import util.exception.OfferNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateListingException;
import util.exception.UserNotFoundException;

/**
 *
 * @author reuben
 */
@Local
public interface ListingEntitySessionBeanLocal {
    public ListingEntity createNewListing(ListingEntity newListingEntity, Long categoryId, List<Long> tagIds, Long userId) throws UserNotFoundException, UnknownPersistenceException, InputDataValidationException, CreateNewListingException;
    public List<ListingEntity> retrieveAllListings();
    public List<ListingEntity> searchListingsByName(String searchString);
    public List<ListingEntity> filterListingsByCategory(Long categoryId) throws CategoryNotFoundException;
    public List<ListingEntity> filterListingsByTags(List<Long> tagIds, String condition);
    public ListingEntity retrieveListingByListingId(Long listingId) throws ListingNotFoundException;
    public void updateListing(ListingEntity listingEntity, Long categoryId, List<Long> tagIds) throws ListingNotFoundException, CategoryNotFoundException, TagNotFoundException, UpdateListingException, InputDataValidationException;
    public void deleteListing(Long listingId) throws ListingNotFoundException, DeleteListingException;
    public void addOffer(Long listingId, Long offerId) throws ListingNotFoundException, OfferNotFoundException;
    public List<ListingEntity> retrieveListingsByUser(Long userId);
}
