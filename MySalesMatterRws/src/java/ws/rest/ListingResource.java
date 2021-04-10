/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.ListingEntitySessionBeanLocal;
import ejb.session.stateless.OfferEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.ReviewEntity;
import entity.TagEntity;
import entity.UserEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.enumeration.OfferType;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewListingException;
import util.exception.DeleteListingException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ListingNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateListingException;
import util.exception.UserNotFoundException;
import ws.datamodel.CreateListingReq;
import ws.datamodel.FilterByTagsReq;
import ws.datamodel.UpdateListingReq;

/**
 * REST Web Service
 *
 * @author Yuki
 */
@Path("Listing")
public class ListingResource {

    OfferEntitySessionBeanLocal offerEntitySessionBeanLocal = lookupOfferEntitySessionBeanLocal();

    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();

    TagEntitySessionBeanLocal tagEntitySessionBeanLocal = lookupTagEntitySessionBeanLocal();

    CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal = lookupCategoryEntitySessionBeanLocal();

    ListingEntitySessionBeanLocal listingEntitySessionBeanLocal = lookupListingEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    public ListingResource() {
    }

    @Path("retrieveAllListings")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllListings(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ListingResource.retrieveAllListings(): User " + userEntity.getUsername() + " login remotely via web service");

            List<ListingEntity> listingEntities = listingEntitySessionBeanLocal.retrieveAllListings();
            for (ListingEntity l : listingEntities) {
                l.getCategoryEntity().getListings().clear();
                l.getCategoryEntity().getSubCategoryEntities().clear();
                l.getUser().getLikedItems().clear();
                l.getUser().getListings().clear();
                l.getUser().getOffers().clear();
                l.getUser().getReviews().clear();
                l.getUser().getTransactions().clear();
                for (OfferEntity o: l.getOffers()) {
                    o.setListing(null);
                    o.getMessage().clear();
                    o.setUser(null);
                    o.setSales(null);
                }
                for (ReviewEntity r: l.getReviews()) {
                    r.setReviewer(null);
                    r.setListing(null);
                }
                for (TagEntity t: l.getTags()) {
                    t.getListings().clear();
                }
                
//                l.getUser().getListings().remove(l);
//                l.getCategoryEntity().getListings().remove(l);
//                List<OfferEntity> offers = l.getOffers();
//                for (OfferEntity o : offers) {
//                    o.setListing(null);
//                    o.getMessage().clear();
//                }
//                List<ReviewEntity> reviews = l.getReviews();
//                for (ReviewEntity r : reviews) {
//                    r.setListing(null);
//                }
//                List<TagEntity> tags = l.getTags();
//                for (TagEntity t : tags) {
//                    t.getListings().remove(l);
//                }
            }

            GenericEntity<List<ListingEntity>> genericEntity = new GenericEntity<List<ListingEntity>>(listingEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveListingByListingId/{listingId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveListingByListingId(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("listingId") Long listingId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ListingResource.retrieveListingByListingId(): User " + userEntity.getUsername() + " login remotely via web service");

            ListingEntity l = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);
            
     
            l.getCategoryEntity().getListings().clear();
            l.getCategoryEntity().getSubCategoryEntities().clear();
            l.getUser().getLikedItems().clear();
            l.getUser().getListings().clear();
            l.getUser().getOffers().clear();
            l.getUser().getReviews().clear();
            l.getUser().getTransactions().clear();
            for (OfferEntity o: l.getOffers()) {
                o.setListing(null);
                o.getMessage().clear();
                o.setUser(null);
                o.setSales(null);
            }
            for (ReviewEntity r: l.getReviews()) {
                r.setReviewer(null);
                r.setListing(null);
            }
            for (TagEntity t: l.getTags()) {
                t.getListings().clear();
            }

//            l.getUser().getListings().remove(l);
//            l.getCategoryEntity().getListings().remove(l);
//            List<OfferEntity> offers = l.getOffers();
//            for (OfferEntity o : offers) {
//                o.setListing(null);
//                o.getMessage().clear();
//                o.setUser(null);
//                o.setSales(null);
//            }
//            List<ReviewEntity> reviews = l.getReviews();
//            for (ReviewEntity r : reviews) {
//                r.setListing(null);
//            }
//            List<TagEntity> tags = l.getTags();
//            for (TagEntity t : tags) {
//                t.getListings().remove(l);
//            }

            return Response.status(Response.Status.OK).entity(l).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (ListingNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveListingsByUser")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveListingsByUser(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ListingResource.retrieveListingsByUser(): User " + userEntity.getUsername() + " login remotely via web service");

            List<ListingEntity> listingEntities = listingEntitySessionBeanLocal.retrieveListingsByUser(userEntity.getUserId());

            for (ListingEntity l : listingEntities) {
                l.getCategoryEntity().getListings().clear();
                l.getCategoryEntity().getSubCategoryEntities().clear();
                l.getUser().getLikedItems().clear();
                l.getUser().getListings().clear();
                l.getUser().getOffers().clear();
                l.getUser().getReviews().clear();
                l.getUser().getTransactions().clear();
                for (OfferEntity o: l.getOffers()) {
                    o.setListing(null);
                    o.getMessage().clear();
                    o.setUser(null);
                    o.setSales(null);
                }
                for (ReviewEntity r: l.getReviews()) {
                    r.setReviewer(null);
                    r.setListing(null);
                }
                for (TagEntity t: l.getTags()) {
                    t.getListings().clear();
                }
            }
//            for (ListingEntity l : listingEntities) {
//                l.getUser().getListings().remove(l);
//                l.getCategoryEntity().getListings().remove(l);
//                List<OfferEntity> offers = l.getOffers();
//                for (OfferEntity o : offers) {
//                    o.setListing(null);
//                    o.getMessage().clear();
//                    o.setUser(null);
//                    o.setSales(null);
//                }
//                List<ReviewEntity> reviews = l.getReviews();
//                for (ReviewEntity r : reviews) {
//                    r.setListing(null);
//                }
//                List<TagEntity> tags = l.getTags();
//                for (TagEntity t : tags) {
//                    t.getListings().remove(l);
//                }
//            }

            GenericEntity<List<ListingEntity>> genericEntity = new GenericEntity<List<ListingEntity>>(listingEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewListing(CreateListingReq createListingReq) {
        if (createListingReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createListingReq.getUsername(), createListingReq.getPassword());
                System.out.println("********** ListingResource.createNewListing(): User " + userEntity.getUsername() + " login remotely via web service");
                ListingEntity listingEntity = listingEntitySessionBeanLocal.createNewListing(createListingReq.getNewListingEntity(), createListingReq.getCategoryId(), createListingReq.getTagIds(), createListingReq.getUserId());
                return Response.status(Response.Status.OK).entity(listingEntity.getListingId()).build();
            } catch (UnknownPersistenceException | InputDataValidationException | CreateNewListingException | UserNotFoundException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new listing request").build();
        }
    }

    @Path("searchListingsByName/{name}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchListingsByName(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("name") String name) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ListingResource.searchListingsByName(): User " + userEntity.getUsername() + " login remotely via web service");

            List<ListingEntity> listingEntities = listingEntitySessionBeanLocal.searchListingsByName(name);

            for (ListingEntity l : listingEntities) {
                l.getCategoryEntity().getListings().clear();
                l.getCategoryEntity().getSubCategoryEntities().clear();
                l.getUser().getLikedItems().clear();
                l.getUser().getListings().clear();
                l.getUser().getOffers().clear();
                l.getUser().getReviews().clear();
                l.getUser().getTransactions().clear();
                for (OfferEntity o: l.getOffers()) {
                    o.setListing(null);
                    o.getMessage().clear();
                    o.setUser(null);
                    o.setSales(null);
                }
                for (ReviewEntity r: l.getReviews()) {
                    r.setReviewer(null);
                    r.setListing(null);
                }
                for (TagEntity t: l.getTags()) {
                    t.getListings().clear();
                }
            }
//            for (ListingEntity l : listingEntities) {
//                l.getUser().getListings().remove(l);
//                l.getCategoryEntity().getListings().remove(l);
//                List<OfferEntity> offers = l.getOffers();
//                for (OfferEntity o : offers) {
//                    o.setListing(null);
//                    o.getMessage().clear();
//                    o.setUser(null);
//                    o.setSales(null);
//                }
//                List<ReviewEntity> reviews = l.getReviews();
//                for (ReviewEntity r : reviews) {
//                    r.setListing(null);
//                    r.setReviewId(null);
//                }
//                List<TagEntity> tags = l.getTags();
//                for (TagEntity t : tags) {
//                    t.getListings().remove(l);
//                }
//            }

            GenericEntity<List<ListingEntity>> genericEntity = new GenericEntity<List<ListingEntity>>(listingEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("filterListingsByCategory/{categoryId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterListingsByCategory(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("categoryId") Long categoryId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ListingResource.filterListingsByCategory(): User " + userEntity.getUsername() + " login remotely via web service");

            List<ListingEntity> listingEntities = listingEntitySessionBeanLocal.filterListingsByCategory(categoryId);

            for (ListingEntity l : listingEntities) {
                l.getCategoryEntity().getListings().clear();
                l.getCategoryEntity().getSubCategoryEntities().clear();
                l.getUser().getLikedItems().clear();
                l.getUser().getListings().clear();
                l.getUser().getOffers().clear();
                l.getUser().getReviews().clear();
                l.getUser().getTransactions().clear();
                for (OfferEntity o: l.getOffers()) {
                    o.setListing(null);
                    o.getMessage().clear();
                    o.setUser(null);
                    o.setSales(null);
                }
                for (ReviewEntity r: l.getReviews()) {
                    r.setReviewer(null);
                    r.setListing(null);
                }
                for (TagEntity t: l.getTags()) {
                    t.getListings().clear();
                }
            }
//            for (ListingEntity l : listingEntities) {
//                l.getUser().getListings().remove(l);
//                l.getCategoryEntity().getListings().remove(l);
//                List<OfferEntity> offers = l.getOffers();
//                for (OfferEntity o : offers) {
//                    o.setListing(null);
//                    o.getMessage().clear();
//                    o.setUser(null);
//                    o.setSales(null);
//                }
//                List<ReviewEntity> reviews = l.getReviews();
//                for (ReviewEntity r : reviews) {
//                    r.setListing(null);
//                    r.setReviewId(null);
//                }
//                List<TagEntity> tags = l.getTags();
//                for (TagEntity t : tags) {
//                    t.getListings().remove(l);
//                }
//            }

            GenericEntity<List<ListingEntity>> genericEntity = new GenericEntity<List<ListingEntity>>(listingEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (CategoryNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /*@Path("filterListingsByTags")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterListingsByTags(FilterByTagsReq filterByTagsReq) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(filterByTagsReq.getUsername(), filterByTagsReq.getPassword());
            System.out.println("********** ListingResource.filterListingsByTags(): User " + userEntity.getUsername() + " login remotely via web service");

            List<ListingEntity> listingEntities = listingEntitySessionBeanLocal.filterListingsByTags(filterByTagsReq.getTagIds(), filterByTagsReq.getCondition());

            for (ListingEntity l : listingEntities) {
                l.getUser().getListings().remove(l);
                l.getCategoryEntity().getListings().remove(l);
                List<OfferEntity> offers = l.getOffers();
                for (OfferEntity o : offers) {
                    o.setListing(null);
                }
                List<ReviewEntity> reviews = l.getReviews();
                for (ReviewEntity r : reviews) {
                    r.setListing(null);
                }
                List<TagEntity> tags = l.getTags();
                for (TagEntity t : tags) {
                    t.getListings().remove(l);
                }
            }

            GenericEntity<List<ListingEntity>> genericEntity = new GenericEntity<List<ListingEntity>>(listingEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }*/
    @Path("updateListing/{listingId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateListing(UpdateListingReq updateListingReq) {
        if (updateListingReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(updateListingReq.getUsername(), updateListingReq.getPassword());
                System.out.println("********** ListingResource.updateListing(): User " + userEntity.getUsername() + " login remotely via web service");
                listingEntitySessionBeanLocal.updateListing(updateListingReq.getListingEntity(), updateListingReq.getCategoryId(), updateListingReq.getTagIds());
                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (ListingNotFoundException | CategoryNotFoundException | TagNotFoundException | UpdateListingException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update listing request").build();
        }
    }

    @Path("{listingId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteListing(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("listingId") Long listingId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ListingResource.deleteListing(): User " + userEntity.getUsername() + " login remotely via web service");
            listingEntitySessionBeanLocal.deleteListing(listingId);
            return Response.status(Response.Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (DeleteListingException | ListingNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private UserEntitySessionBeanLocal lookupUserEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (UserEntitySessionBeanLocal) c.lookup("java:global/MySalesMatter/MySalesMatter-ejb/UserEntitySessionBean!ejb.session.stateless.UserEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private OfferEntitySessionBeanLocal lookupOfferEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (OfferEntitySessionBeanLocal) c.lookup("java:global/MySalesMatter/MySalesMatter-ejb/OfferEntitySessionBean!ejb.session.stateless.OfferEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private TagEntitySessionBeanLocal lookupTagEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (TagEntitySessionBeanLocal) c.lookup("java:global/MySalesMatter/MySalesMatter-ejb/TagEntitySessionBean!ejb.session.stateless.TagEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CategoryEntitySessionBeanLocal lookupCategoryEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CategoryEntitySessionBeanLocal) c.lookup("java:global/MySalesMatter/MySalesMatter-ejb/CategoryEntitySessionBean!ejb.session.stateless.CategoryEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ListingEntitySessionBeanLocal lookupListingEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ListingEntitySessionBeanLocal) c.lookup("java:global/MySalesMatter/MySalesMatter-ejb/ListingEntitySessionBean!ejb.session.stateless.ListingEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
