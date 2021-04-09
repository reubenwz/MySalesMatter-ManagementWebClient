/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.MessageEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.MessageEntity;
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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;
import ws.datamodel.CreateMessageReq;

/**
 * REST Web Service
 *
 * @author rtan3
 */
@Path("Message")
public class MessageResource {
    
    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();
    
    MessageEntitySessionBeanLocal messageEntitySessionBeanLocal = lookupMessageEntitySessionBeanLocal();


    @Context
    private UriInfo context;

    public MessageResource() {
    }
    
    
    @Path("retrieveAllMessages")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllMessages(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** MessageResource.retrieveAllMessages(): User " + userEntity.getUsername() + " login remotely via web service");

            List<MessageEntity> messageEntities = messageEntitySessionBeanLocal.retrieveAllMessages();
            
            for (MessageEntity m : messageEntities) {
                m.getRecipient().getListings().clear();
                m.getRecipient().getTransactions().clear();
                m.getRecipient().getReviews().clear();
                m.getRecipient().getLikedItems().clear();
                m.getRecipient().getOffers().clear();
                m.getSender().getListings().clear();
                m.getSender().getTransactions().clear();
                m.getSender().getReviews().clear();
                m.getSender().getLikedItems().clear();
                m.getSender().getOffers().clear();
                m.getOffer().getMessage().clear();
                m.getOffer().setSales(null);
                m.getOffer().setUser(null);
                m.getOffer().setOfferType(null);             
            }

            GenericEntity<List<MessageEntity>> genericEntity = new GenericEntity<List<MessageEntity>>(messageEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveReceivedMessageSByUserId")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveReceivedMessageSByUserId(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** MessageResource.retrieveAllMessages(): User " + userEntity.getUsername() + " login remotely via web service");

            List<MessageEntity> messageEntities = messageEntitySessionBeanLocal.retrieveReceivedMessageSByUserId(userEntity.getUserId());

            for (MessageEntity m : messageEntities) {
                m.getRecipient().getListings().clear();
                m.getRecipient().getTransactions().clear();
                m.getRecipient().getReviews().clear();
                m.getRecipient().getLikedItems().clear();
                m.getRecipient().getOffers().clear();
                m.getSender().getListings().clear();
                m.getSender().getTransactions().clear();
                m.getSender().getReviews().clear();
                m.getSender().getLikedItems().clear();
                m.getSender().getOffers().clear();
                m.getOffer().getMessage().clear();
                m.getOffer().setSales(null);
                m.getOffer().setUser(null);
                m.getOffer().setOfferType(null);  
            }
            
            GenericEntity<List<MessageEntity>> genericEntity = new GenericEntity<List<MessageEntity>>(messageEntities) {
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
    public Response addMessage(CreateMessageReq createMessageReq)
    {
        if(createMessageReq != null)
        {
            try
            {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createMessageReq.getUsername(), createMessageReq.getPassword());
                System.out.println("********** MessageResource.addMessage(): User " + userEntity.getUsername() + " login remotely via web service");
                Long m = messageEntitySessionBeanLocal.addMessage(createMessageReq.getMessage(), createMessageReq.getOfferId(), createMessageReq.getSenderId(), createMessageReq.getDate());
                return Response.status(Response.Status.OK).entity(m).build();
                
            }
            catch(UnknownPersistenceException | InputDataValidationException | UserNotFoundException ex)
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
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid send message request").build();
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
    
    private MessageEntitySessionBeanLocal lookupMessageEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (MessageEntitySessionBeanLocal) c.lookup("java:global/MySalesMatter/MySalesMatter-ejb/MessageEntitySessionBean!ejb.session.stateless.MessageEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
