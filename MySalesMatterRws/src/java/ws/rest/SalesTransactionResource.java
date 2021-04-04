/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.SalesTransactionEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.CreateNewTransactionException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OfferNotFoundException;
import util.exception.SalesTransactionExistException;
import util.exception.SalesTransactionNotFoundException;
import util.exception.TransactionDeletionException;
import util.exception.UserNotFoundException;
import ws.datamodel.CreateSalesTransactionReq;
import ws.datamodel.UpdateStatusReq;

/**
 * REST Web Service
 *
 * @author sylvia
 */
@Path("SalesTransaction")
public class SalesTransactionResource {

    SalesTransactionEntitySessionBeanLocal salesTransactionEntitySessionBeanLocal = lookupSalesTransactionEntitySessionBeanLocal();

    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();
    

    @Context
    private UriInfo context;

    
    public SalesTransactionResource() {
    }
    
    @Path("retrieveAllTransactions")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTransactions(@QueryParam("username") String username, 
                                        @QueryParam("password") String password)
    {
        try
        {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** SalesTransactionResource.retrieveAllTransactions(): User " + userEntity.getUsername() + " login remotely via web service");

            List<SalesTransactionEntity> salesTransactionEntities = salesTransactionEntitySessionBeanLocal.retrieveAllTransactions();
            for(SalesTransactionEntity s: salesTransactionEntities)
            {    
                s.getOffer().setListing(null);
                s.getOffer().setSales(null);
                s.getOffer().setUser(null);
                s.getUser().getConversationsAsOfferee().clear();
                s.getUser().getConversationsAsOfferer().clear();
                s.getUser().getLikedItems().clear();
                s.getUser().getListings().clear();
                s.getUser().getOffers().clear();
                s.getUser().getReviews().clear();
                s.getUser().getTransactions().clear();
            }
            
            GenericEntity<List<SalesTransactionEntity>> genericEntity = new GenericEntity<List<SalesTransactionEntity>>(salesTransactionEntities) {
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
    
    @Path("retrieveTransactionByUserId")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalesTransactionByUserId(@QueryParam("username") String username, 
                                        @QueryParam("password") String password)
    {
        try
        {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** SalesTransactionResource.getSalesTransactionByUserId(): User " + userEntity.getUsername() + " login remotely via web service");

            List<SalesTransactionEntity> salesTransactionEntities = salesTransactionEntitySessionBeanLocal.getSalesTransactionByUserId(userEntity.getUserId());
            for(SalesTransactionEntity s: salesTransactionEntities)
            {                
                s.getOffer().setListing(null);
                s.getOffer().setSales(null);
                s.getOffer().setUser(null);
                s.getUser().getConversationsAsOfferee().clear();
                s.getUser().getConversationsAsOfferer().clear();
                s.getUser().getLikedItems().clear();
                s.getUser().getListings().clear();
                s.getUser().getOffers().clear();
                s.getUser().getReviews().clear();
                s.getUser().getTransactions().clear();
            }
            
            GenericEntity<List<SalesTransactionEntity>> genericEntity = new GenericEntity<List<SalesTransactionEntity>>(salesTransactionEntities) {
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
    
    @Path("retrieveTransaction/{transactionId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveTransactionById(@QueryParam("username") String username, 
                                        @QueryParam("password") String password,
                                        @PathParam("transactionId") Long transactionId)
    {
        try
        {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** SalesTransactionResource.retrieveTransactionById(): User " + userEntity.getUsername() + " login remotely via web service");

            SalesTransactionEntity salesTransactionEntity = salesTransactionEntitySessionBeanLocal.retrieveTransactionById(transactionId);
            
            salesTransactionEntity.setOffer(null);
            salesTransactionEntity.setUser(null);
            
            return Response.status(Response.Status.OK).entity(salesTransactionEntity).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(SalesTransactionNotFoundException ex)
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
    public Response updateStatus(UpdateStatusReq updateStatusReq) {
        if (updateStatusReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(updateStatusReq.getUsername(), updateStatusReq.getPassword());
                System.out.println("********** SalesTransactionResource.updateStatus(): User " + userEntity.getUsername() + " login remotely via web service");

                salesTransactionEntitySessionBeanLocal.updateStatus(updateStatusReq.getTransactionId(), updateStatusReq.getStatus());

                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (SalesTransactionNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update status request").build();
        }
    }

    @Path("{transactionId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTransaction(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("transactionId") Long transactionId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** SalesTransactionResource.deleteTransaction(): User " + userEntity.getUsername() + " login remotely via web service");

            salesTransactionEntitySessionBeanLocal.deleteTransaction(transactionId);

            return Response.status(Response.Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (SalesTransactionNotFoundException | TransactionDeletionException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSalesTransaction(CreateSalesTransactionReq createSalesTransactionReq)
    {
        if(createSalesTransactionReq != null)
        {
            try
            {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createSalesTransactionReq.getUsername(), createSalesTransactionReq.getPassword());
                System.out.println("********** SalesTransactionResource.createSalesTransaction(): User " + userEntity.getUsername() + " login remotely via web service");
                
                SalesTransactionEntity salesTransactionEntity  = salesTransactionEntitySessionBeanLocal.createSalesTransaction(createSalesTransactionReq.getOfferId(), createSalesTransactionReq.getUserId(), createSalesTransactionReq.getStatus(), createSalesTransactionReq.getTransactionDate(), createSalesTransactionReq.getTotalAmt());                
                
                return Response.status(Response.Status.OK).entity(salesTransactionEntity.getSalesTransactionId()).build();
            }
            catch(UserNotFoundException | CreateNewTransactionException | SalesTransactionExistException | OfferNotFoundException ex)
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
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new sales transaction request").build();
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

    private SalesTransactionEntitySessionBeanLocal lookupSalesTransactionEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SalesTransactionEntitySessionBeanLocal) c.lookup("java:global/MySalesMatter/MySalesMatter-ejb/SalesTransactionEntitySessionBean!ejb.session.stateless.SalesTransactionEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
}
