/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.TagEntity;
import entity.UserEntity;
import java.util.ArrayList;
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
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewListingException;
import util.exception.DeleteListingException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.ListingNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OfferNotFoundException;
import util.exception.UpdateListingException;
import util.exception.UserNotFoundException;

/**
 *
 * @author reuben
 */
@Stateless
public class ListingEntitySessionBean implements ListingEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager entityManager;

    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @EJB
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;

    @EJB
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ListingEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    //Reviews are not created during creation of Listing
    //List of pictures to be added to be clarified with prof how to do.
    @Override
    public ListingEntity createNewListing(ListingEntity newListingEntity, Long categoryId, List<Long> tagIds, Long userId) throws UserNotFoundException, UnknownPersistenceException, InputDataValidationException, CreateNewListingException {
        Set<ConstraintViolation<ListingEntity>> constraintViolations = validator.validate(newListingEntity);

        if (constraintViolations.isEmpty()) {
            try {
                UserEntity user = userEntitySessionBeanLocal.retrieveUserById(userId);

                if (categoryId == null) {
                    throw new CreateNewListingException("The new Listing must be associated a leaf category");
                }

                CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

                if (!categoryEntity.getSubCategoryEntities().isEmpty()) {
                    throw new CreateNewListingException("Selected category for the new Listing is not a leaf category");
                }
                entityManager.persist(newListingEntity);
                user.getListings().add(newListingEntity);
                newListingEntity.setUser(user);
                entityManager.merge(user);
                //entityManager.persist(newListingEntity);
                newListingEntity.setCategoryEntity(categoryEntity);
                categoryEntity.getListings().add(newListingEntity);
                entityManager.merge(categoryEntity);

                if (tagIds != null && (!tagIds.isEmpty())) {
                    for (Long tagId : tagIds) {
                        TagEntity tagEntity = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
                        newListingEntity.addTag(tagEntity);
                        tagEntity.getListings().add(newListingEntity);
                        entityManager.merge(tagEntity);
                    }
                }

                entityManager.flush();

                return newListingEntity;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    throw new UnknownPersistenceException(ex.getMessage());
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } catch (CategoryNotFoundException | TagNotFoundException | UserNotFoundException ex) {
                throw new CreateNewListingException("An error has occurred while creating the new Listing: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<ListingEntity> retrieveAllListings() {
        Query query = entityManager.createQuery("SELECT l FROM ListingEntity l ORDER BY l.listingId ASC");
        List<ListingEntity> listingEntities = query.getResultList();

        for (ListingEntity listingEntity : listingEntities) {
            listingEntity.getCategoryEntity();
            listingEntity.getTags().size();
        }

        return listingEntities;
    }

    @Override
    public List<ListingEntity> searchListingsByName(String searchString) {
        Query query = entityManager.createQuery("SELECT l FROM ListingEntity l WHERE LOWER(l.name) LIKE :inSearchString ORDER BY l.listingId ASC");
        query.setParameter("inSearchString", "%" + searchString.toLowerCase() + "%");
        List<ListingEntity> listingEntities = query.getResultList();

        for (ListingEntity listingEntity : listingEntities) {
            listingEntity.getCategoryEntity();
            listingEntity.getTags().size();
        }

        return listingEntities;
    }

    @Override
    public List<ListingEntity> filterListingsByCategory(Long categoryId) throws CategoryNotFoundException {
        List<ListingEntity> listingEntities = new ArrayList<>();
        CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

        if (categoryEntity.getSubCategoryEntities().isEmpty()) {
            listingEntities = categoryEntity.getListings();
        } else {
            for (CategoryEntity subCategoryEntity : categoryEntity.getSubCategoryEntities()) {
                listingEntities.addAll(addSubCategoryListings(subCategoryEntity));
            }
        }

        for (ListingEntity listingEntity : listingEntities) {
            listingEntity.getCategoryEntity();
            listingEntity.getTags().size();
        }

        return listingEntities;
    }

    @Override
    public List<ListingEntity> filterListingsByTags(List<Long> tagIds, String condition) {
        List<ListingEntity> listingEntities = new ArrayList<>();

        if (tagIds == null || tagIds.isEmpty() || (!condition.equals("AND") && !condition.equals("OR"))) {
            return listingEntities;
        } else {
            if (condition.equals("OR")) {
                Query query = entityManager.createQuery("SELECT DISTINCT l FROM ListingEntity l, IN (l.tagEntities) te WHERE te.tagId IN :inTagIds ORDER BY l.listingId ASC");
                query.setParameter("inTagIds", tagIds);
                listingEntities = query.getResultList();
            } else // AND
            {
                String selectClause = "SELECT l FROM ListingEntity l";
                String whereClause = "";
                Boolean firstTag = true;
                Integer tagCount = 1;

                for (Long tagId : tagIds) {
                    selectClause += ", IN (l.tagEntities) te" + tagCount;

                    if (firstTag) {
                        whereClause = "WHERE te1.tagId = " + tagId;
                        firstTag = false;
                    } else {
                        whereClause += " AND te" + tagCount + ".tagId = " + tagId;
                    }

                    tagCount++;
                }

                String jpql = selectClause + " " + whereClause + " ORDER BY l.listingId ASC";
                Query query = entityManager.createQuery(jpql);
                listingEntities = query.getResultList();
            }

            for (ListingEntity listingEntity : listingEntities) {
                listingEntity.getCategoryEntity();
                listingEntity.getTags().size();
            }

            return listingEntities;
        }
    }

    @Override
    public ListingEntity retrieveListingByListingId(Long listingId) throws ListingNotFoundException {
        ListingEntity listingEntity = entityManager.find(ListingEntity.class, listingId);

        if (listingEntity != null) {
            listingEntity.getCategoryEntity();
            listingEntity.getTags().size();

            return listingEntity;
        } else {
            throw new ListingNotFoundException("Listing ID " + listingId + " does not exist!");
        }
    }

    @Override
    public ListingEntity retrieveListingByOfferId(Long offerId) throws OfferNotFoundException {
        try {
            OfferEntity o = offerEntitySessionBeanLocal.retrieveOfferById(offerId);
            o.getListing();
            o.getMessage().size();
            o.getSales();
            o.getUser();
            return o.getListing();
        } catch (OfferNotFoundException ex) {
            throw new OfferNotFoundException("Offer ID " + offerId + " does not exist!");
        }

    }

    public List<ListingEntity> retrieveListingsByUser(Long userId) {
        Query query = entityManager.createQuery("SELECT l FROM ListingEntity l WHERE l.user.userId = :inUserId");
        query.setParameter("inUserId", userId);
        return query.getResultList();
    }

//Users should not be able to update reviews when they are updating their listings.
    //List of pictures to be updated to be clarified with prof how to do.
    @Override
    public void updateListing(ListingEntity listingEntity, Long categoryId, List<Long> tagIds) throws ListingNotFoundException, CategoryNotFoundException, TagNotFoundException, UpdateListingException, InputDataValidationException {
        if (listingEntity != null && listingEntity.getListingId() != null) {
            Set<ConstraintViolation<ListingEntity>> constraintViolations = validator.validate(listingEntity);

            if (constraintViolations.isEmpty()) {
                ListingEntity listingEntityToUpdate = retrieveListingByListingId(listingEntity.getListingId());

                if (categoryId != null && (!listingEntityToUpdate.getCategoryEntity().getCategoryId().equals(categoryId))) {
                    CategoryEntity categoryEntityToUpdate = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

                    if (!categoryEntityToUpdate.getSubCategoryEntities().isEmpty()) {
                        throw new UpdateListingException("Selected category for the new listing is not a leaf category");
                    }
                    CategoryEntity currCategory = listingEntityToUpdate.getCategoryEntity();
                    currCategory.getListings().remove(listingEntityToUpdate);
                    entityManager.merge(currCategory);
                    listingEntityToUpdate.setCategoryEntity(categoryEntityToUpdate);
                    categoryEntityToUpdate.getListings().add(listingEntity);
                    entityManager.merge(categoryEntityToUpdate);
                }

                if (tagIds != null) {
                    for (TagEntity tagEntity : listingEntityToUpdate.getTags()) {
                        tagEntity.getListings().remove(listingEntityToUpdate);
                    }
                    listingEntityToUpdate.setTags(null);
                    entityManager.merge(listingEntityToUpdate);
                    List<TagEntity> tags = new ArrayList<>();
//                    listingEntityToUpdate.getTags().clear();
//
//                    for (Long tagId : tagIds) {
//                        TagEntity tagEntity = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
//                        listingEntityToUpdate.addTag(tagEntity);
//                        tagEntity.getListings().add(listingEntity);
//                        entityManager.merge(tagEntity);
//                    }
                    for (Long tagId : tagIds) {
                        TagEntity tagEntity = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
                        tagEntity.getListings().add(listingEntity);
                        entityManager.merge(tagEntity);
                        tags.add(tagEntity);
                    }
                    listingEntityToUpdate.setTags(tags);
                }

                listingEntityToUpdate.setName(listingEntity.getName());
                listingEntityToUpdate.setDescription(listingEntity.getDescription());
                listingEntityToUpdate.setBrand(listingEntity.getBrand());
                listingEntityToUpdate.setRentalPrice(listingEntity.getRentalPrice());
                listingEntityToUpdate.setSalePrice(listingEntity.getSalePrice());
                listingEntityToUpdate.setLocation(listingEntity.getLocation());
                listingEntityToUpdate.setRentalAvailability(listingEntity.isRentalAvailability());
                listingEntityToUpdate.setForSaleAvailability(listingEntity.isForSaleAvailability());
                listingEntityToUpdate.setIsRentOut(listingEntity.isIsRentOut());
                //listingEntityToUpdate.setReservedDates(listingEntity.getReservedDates());
                listingEntityToUpdate.setPicturePath(listingEntity.getPicturePath());
                entityManager.merge(listingEntityToUpdate);

            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new ListingNotFoundException("Listing ID not provided for Listing to be updated");
        }
    }

    @Override
    public void deleteListing(Long listingId) throws ListingNotFoundException, DeleteListingException {
        ListingEntity listingEntityToRemove = retrieveListingByListingId(listingId);
        if (listingEntityToRemove.getReviews().isEmpty() || listingEntityToRemove.getTags().isEmpty()) {

            UserEntity user = listingEntityToRemove.getUser();

            CategoryEntity category = listingEntityToRemove.getCategoryEntity();
            category.getListings().remove(listingEntityToRemove);
            entityManager.merge(category);
            for (TagEntity t : listingEntityToRemove.getTags()) {
                t.getListings().remove(listingEntityToRemove);
                entityManager.merge(t);
            }

            for (OfferEntity o : listingEntityToRemove.getOffers()) {
                entityManager.remove(o);
            }
            user.getListings().remove(listingEntityToRemove);
            entityManager.merge(user);
            entityManager.remove(listingEntityToRemove);
        } else {
            throw new DeleteListingException("Listing ID " + listingId + " is associated with existing reviews(s) or tag(s) or reservation(s) and cannot be deleted!");
        }
    }

    private List<ListingEntity> addSubCategoryListings(CategoryEntity categoryEntity) {
        List<ListingEntity> listingEntities = new ArrayList<>();

        if (categoryEntity.getSubCategoryEntities().isEmpty()) {
            return categoryEntity.getListings();
        } else {
            for (CategoryEntity subCategoryEntity : categoryEntity.getSubCategoryEntities()) {
                listingEntities.addAll(addSubCategoryListings(subCategoryEntity));
            }

            return listingEntities;
        }
    }

    @Override
    public void addOffer(Long listingId, Long offerId) throws ListingNotFoundException, OfferNotFoundException {
        ListingEntity listingEntityToAddOffer = retrieveListingByListingId(listingId);
        OfferEntity offerEntityToBeAdded = offerEntitySessionBeanLocal.retrieveOfferById(offerId);
        listingEntityToAddOffer.getOffers().add(offerEntityToBeAdded);
        entityManager.merge(listingEntityToAddOffer);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ListingEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
