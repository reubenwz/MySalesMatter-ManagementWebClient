/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.ReviewEntity;
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
import util.exception.CreateNewReviewException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ListingNotFoundException;
import util.exception.ReviewNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateReviewException;
import util.exception.UserNotFoundException;
import ws.datamodel.CreateReviewReq;
import ws.datamodel.UpdateReviewReq;

/**
 * REST Web Service
 *
 * @author sylvia
 */
@Path("Review")
public class ReviewResource {

    ReviewEntitySessionBeanLocal reviewEntitySessionBeanLocal = lookupReviewEntitySessionBeanLocal();

    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();
    

    @Context
    private UriInfo context;

    
    public ReviewResource() {
    }
    
    @Path("retrieveAllReviews")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllReviews(@QueryParam("username") String username, 
                                        @QueryParam("password") String password)
    {
        try
        {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ReviewResource.retrieveAllReviews(): User " + userEntity.getUsername() + " login remotely via web service");

            List<ReviewEntity> reviewEntities = reviewEntitySessionBeanLocal.retrieveAllReviews();
            for(ReviewEntity reviewEntity: reviewEntities)
            {                
                reviewEntity.getListing().getOffers().clear();
                //reviewEntity.getListing().getReservations().clear();
                reviewEntity.getListing().getReviews().clear();
                reviewEntity.getListing().getTags().clear();
                //reviewEntity.getReviewer().getConversationsAsOfferee().clear();
                //reviewEntity.getReviewer().getConversationsAsOfferer().clear();
                reviewEntity.getReviewer().getLikedItems().clear();
                reviewEntity.getReviewer().getListings().clear();
                reviewEntity.getReviewer().getOffers().clear();
                reviewEntity.getReviewer().getReviews().clear();
                reviewEntity.getReviewer().getTransactions().clear();
            
            }
            
            GenericEntity<List<ReviewEntity>> genericEntity = new GenericEntity<List<ReviewEntity>>(reviewEntities) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(InvalidLoginCredentialException ex)
        {            
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveReviewByUserId")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveReviewByUserId(@QueryParam("username") String username, 
                                        @QueryParam("password") String password)
    {
        try
        {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ReviewResource.retrieveReviewByReviewId(): User " + userEntity.getUsername() + " login remotely via web service");

            List<ReviewEntity> reviewEntities = reviewEntitySessionBeanLocal.getReviewsByUserId(userEntity.getUserId());
            for(ReviewEntity reviewEntity: reviewEntities)
            {                
                reviewEntity.getListing().getOffers().clear();
                //reviewEntity.getListing().getReservations().clear();
                reviewEntity.getListing().getReviews().clear();
                reviewEntity.getListing().getTags().clear();
                //reviewEntity.getReviewer().getConversationsAsOfferee().clear();
                //reviewEntity.getReviewer().getConversationsAsOfferer().clear();
                reviewEntity.getReviewer().getLikedItems().clear();
                reviewEntity.getReviewer().getListings().clear();
                reviewEntity.getReviewer().getOffers().clear();
                reviewEntity.getReviewer().getReviews().clear();
                reviewEntity.getReviewer().getTransactions().clear();
            
            }
            
            GenericEntity<List<ReviewEntity>> genericEntity = new GenericEntity<List<ReviewEntity>>(reviewEntities) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(InvalidLoginCredentialException ex)
        {            
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveReview/{reviewId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveReviewByReviewId(@QueryParam("username") String username, 
                                        @QueryParam("password") String password,
                                        @PathParam("reviewId") Long reviewId)
    {
        try
        {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ReviewResource.retrieveReviewByReviewId(): User " + userEntity.getUsername() + " login remotely via web service");

            ReviewEntity reviewEntity = reviewEntitySessionBeanLocal.retrieveReviewByReviewId(reviewId);
            reviewEntity.getListing().getOffers().clear();
            //reviewEntity.getListing().getReservations().clear();
            reviewEntity.getListing().getReviews().clear();
            reviewEntity.getListing().getTags().clear();
            //reviewEntity.getReviewer().getConversationsAsOfferee().clear();
            //reviewEntity.getReviewer().getConversationsAsOfferer().clear();
            reviewEntity.getReviewer().getLikedItems().clear();
            reviewEntity.getReviewer().getListings().clear();
            reviewEntity.getReviewer().getOffers().clear();
            reviewEntity.getReviewer().getReviews().clear();
            reviewEntity.getReviewer().getTransactions().clear();
            
            return Response.status(Response.Status.OK).entity(reviewEntity).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(ReviewNotFoundException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReview(UpdateReviewReq updateReviewReq) {
        if (updateReviewReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(updateReviewReq.getUsername(), updateReviewReq.getPassword());
                System.out.println("********** ReviewResource.updateReview(): User " + userEntity.getUsername() + " login remotely via web service");

                reviewEntitySessionBeanLocal.updateReview(updateReviewReq.getReview());

                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (ReviewNotFoundException | UpdateReviewException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update review request").build();
        }
    }

    @Path("{reviewnId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReview(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("reviewId") Long reviewId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ReviewResource.deleteReview(): User " + userEntity.getUsername() + " login remotely via web service");

            reviewEntitySessionBeanLocal.deleteReview(reviewId);

            return Response.status(Response.Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (ReviewNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewReviewEntity(CreateReviewReq createReviewReq)
    {
        if(createReviewReq != null)
        {
            try
            {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createReviewReq.getUsername(), createReviewReq.getPassword());
                System.out.println("********** ReviewResource.createNewReviewEntity(): User " + userEntity.getUsername() + " login remotely via web service");
                
                ReviewEntity reviewEntity = reviewEntitySessionBeanLocal.createNewReviewEntity(createReviewReq.getReviewEntity(), createReviewReq.getReviewerId(), createReviewReq.getListingId());
                return Response.status(Response.Status.OK).entity(reviewEntity.getReviewId()).build();
            }
            catch(UnknownPersistenceException | InputDataValidationException | CreateNewReviewException | UserNotFoundException | ListingNotFoundException ex)
            {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } 
            catch(Exception ex)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new review request").build();
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

    private ReviewEntitySessionBeanLocal lookupReviewEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ReviewEntitySessionBeanLocal) c.lookup("java:global/MySalesMatter/MySalesMatter-ejb/ReviewEntitySessionBean!ejb.session.stateless.ReviewEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
}
