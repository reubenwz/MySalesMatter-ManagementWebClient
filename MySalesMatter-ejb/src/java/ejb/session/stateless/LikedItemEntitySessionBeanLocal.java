/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LikedItemEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.ItemNotLikedException;
import util.exception.LikedItemEntityNotFoundException;
import util.exception.ListingNotFoundException;
import util.exception.UserNotFoundException;

/**
 *
 * @author sylvia
 */
@Local
public interface LikedItemEntitySessionBeanLocal {
    
    public List<LikedItemEntity> getLikedItems(Long userId) throws UserNotFoundException;
    
    public LikedItemEntity createNewLikedItem(Long userId, Long listingId) throws InputDataValidationException, UserNotFoundException, ListingNotFoundException;

    public void unlikeItem(Long userId, Long listingId) throws UserNotFoundException, ListingNotFoundException, ItemNotLikedException;
    
    public LikedItemEntity retrieveLikedItemEntityById(Long id) throws LikedItemEntityNotFoundException;

}
