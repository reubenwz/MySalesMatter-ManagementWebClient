/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.LikedItemEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.ReviewEntity;
import entity.SalesTransactionEntity;
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
import javax.ws.rs.core.Response.Status;
import util.exception.DeleteUserException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserEmailExistsException;
import util.exception.UserNotFoundException;
import ws.datamodel.CreateUserReq;
import ws.datamodel.UpdateUserReq;

/**
 * REST Web Service
 *
 * @author sylvia
 */
@Path("User")
public class UserResource {

    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    public UserResource() {
    }

    @Path("userLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** UserResource.userLogin(): User " + userEntity.getUsername() + " login remotely via web service");

            userEntity.setLikedItems(null);
            userEntity.setTransactions(null);
            userEntity.setReviews(null);
            userEntity.setOffers(null);
            userEntity.setListings(null);
//            for (ReviewEntity r : userEntity.getReviews()) {
//                r.setListing(null);
//                r.setReviewer(null);
//            }
//            for (LikedItemEntity l : userEntity.getLikedItems()) {
//                l.setUser(null);
//                l.setListing(null);
//            }
//            for (OfferEntity o : userEntity.getOffers()) {
//                o.setListing(null);
//                o.setOfferType(null);
//                o.setSales(null);
//                o.setUser(null);
//                o.getMessage().clear();
//            }
//            for (SalesTransactionEntity s : userEntity.getTransactions()) {
//                s.setOffer(null);
//                s.setUser(null);
//            }
//            for (ListingEntity l : userEntity.getListings()) {
//                l.setCategoryEntity(null);
//                l.getTags().clear();
//                l.getReviews().clear();
//                l.getOffers().clear();
//            }

            return Response.status(Status.OK).entity(userEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveUserById/{userId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveUserById(@PathParam("userId") Long userId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserById(userId);
            
            userEntity.setLikedItems(null);
            userEntity.setTransactions(null);
            userEntity.setReviews(null);
            userEntity.setOffers(null);
            userEntity.setListings(null);

//            for (ReviewEntity r : userEntity.getReviews()) {
//                r.setListing(null);
//                r.setReviewer(null);
//            }
//            for (LikedItemEntity l : userEntity.getLikedItems()) {
//                l.setUser(null);
//                l.setListing(null);
//            }
//            for (OfferEntity o : userEntity.getOffers()) {
//                o.setListing(null);
//                o.setOfferType(null);
//                o.setSales(null);
//                o.setUser(null);
//                o.getMessage().clear();
//            }
//            for (SalesTransactionEntity s : userEntity.getTransactions()) {
//                s.setOffer(null);
//                s.setUser(null);
//            }
//            for (ListingEntity l : userEntity.getListings()) {
//                l.setCategoryEntity(null);
//                l.getTags().clear();
//                l.getReviews().clear();
//                l.getOffers().clear();
//            }

            return Response.status(Status.OK).entity(userEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveUserByEmail/{email}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveUserByEmail(@PathParam("email") String email) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserByEmail(email);

            for (ReviewEntity r : userEntity.getReviews()) {
                r.setListing(null);
                r.setReviewer(null);
            }
            for (LikedItemEntity l : userEntity.getLikedItems()) {
                l.setUser(null);
                l.setListing(null);
            }
            for (OfferEntity o : userEntity.getOffers()) {
                o.setListing(null);
                o.setOfferType(null);
                o.setSales(null);
                o.setUser(null);
                o.getMessage().clear();
            }
            for (SalesTransactionEntity s : userEntity.getTransactions()) {
                s.setOffer(null);
                s.setUser(null);
            }
            for (ListingEntity l : userEntity.getListings()) {
                l.setCategoryEntity(null);
                l.getTags().clear();
                l.getReviews().clear();
                l.getOffers().clear();
            }

            return Response.status(Status.OK).entity(userEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(CreateUserReq createUserReq) {
        if (createUserReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.registerUser(createUserReq.getUserEntity());
                return Response.status(Response.Status.OK).entity(userEntity.getUserId()).build();
            } catch (UserEmailExistsException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new user request").build();
        }
    }

    @Path("retrieveAllUsers")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllUsers(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity user = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** UserResource.retrieveAllUsers(): User " + user.getUsername() + " login remotely via web service");

            List<UserEntity> userEntities = userEntitySessionBeanLocal.retrieveAllUsers();

            for (UserEntity userEntity : userEntities) {
                for (ReviewEntity r : userEntity.getReviews()) {
                    r.setListing(null);
                    r.setReviewer(null);
                }
                for (LikedItemEntity l : userEntity.getLikedItems()) {
                    l.setUser(null);
                    l.setListing(null);
                }
                for (OfferEntity o : userEntity.getOffers()) {
                    o.setListing(null);
                    o.setOfferType(null);
                    o.setSales(null);
                    o.setUser(null);
                    o.getMessage().clear();
                }
                for (SalesTransactionEntity s : userEntity.getTransactions()) {
                    s.setOffer(null);
                    s.setUser(null);
                }
                for (ListingEntity l : userEntity.getListings()) {
                    l.setCategoryEntity(null);
                    l.getTags().clear();
                    l.getReviews().clear();
                    l.getOffers().clear();
                }
            }

            GenericEntity<List<UserEntity>> genericEntity = new GenericEntity<List<UserEntity>>(userEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(UpdateUserReq updateUserReq) {
        if (updateUserReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(updateUserReq.getUsername(), updateUserReq.getPassword());
                System.out.println("********** UserResource.updateUser(): User " + userEntity.getUsername() + " login remotely via web service");

                userEntitySessionBeanLocal.updateUser(updateUserReq.getUserEntity());

                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (UserNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update user request").build();
        }
    }

    @Path("{userId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("userId") Long userId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** UserResource.deleteUser(): User " + userEntity.getUsername() + " login remotely via web service");

            userEntitySessionBeanLocal.deleteUser(userId);

            return Response.status(Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (UserNotFoundException | DeleteUserException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
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
}
