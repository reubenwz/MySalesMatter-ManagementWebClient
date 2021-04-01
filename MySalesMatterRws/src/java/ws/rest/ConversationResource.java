/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.ConversationEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.ConversationEntity;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.ConversationNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;
import ws.datamodel.CreateConversationReq;
import ws.datamodel.CreateEmptyConversationReq;

/**
 * REST Web Service
 *
 * @author rtan3
 */
@Path("Conversation")
public class ConversationResource {
    
    ConversationEntitySessionBeanLocal conversationEntitySessionBeanLocal = lookupConversationEntitySessionBeanLocal();
    
    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();


    @Context
    private UriInfo context;

    public ConversationResource() {
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewConversation(CreateConversationReq createConversationReq) {
        if (createConversationReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createConversationReq.getUsername(), createConversationReq.getPassword());
                System.out.println("********** ConversationResource.createNewConversation(): User " + userEntity.getUsername() + " login remotely via web service");
                ConversationEntity conversationEntity = conversationEntitySessionBeanLocal.createNewConversation(createConversationReq.getNewConversationEntity(), createConversationReq.getOffererId(), createConversationReq.getOffereeId(), createConversationReq.getMessageEntities());
                return Response.status(Response.Status.OK).entity(conversationEntity.getConversationId()).build();
            } catch (InputDataValidationException | UserNotFoundException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new conversation request").build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewEmptyConversation(CreateEmptyConversationReq createEmptyConversationReq) {
        if (createEmptyConversationReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createEmptyConversationReq.getUsername(), createEmptyConversationReq.getPassword());
                System.out.println("********** ConversationResource.createNewEmptyConversation(): User " + userEntity.getUsername() + " login remotely via web service");
                ConversationEntity conversationEntity = conversationEntitySessionBeanLocal.createNewEmptyConversation(createEmptyConversationReq.getOffererId(), createEmptyConversationReq.getOffereeId());
                return Response.status(Response.Status.OK).entity(conversationEntity.getConversationId()).build();
            } catch (UserNotFoundException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new conversation request").build();
        }
    }
    
    @Path("retrieveConverationsByUser")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveConverationsByUser(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ConversationResource.retrieveConverationsByUser(): User " + userEntity.getUsername() + " login remotely via web service");

            List<ConversationEntity> conversationEntities = conversationEntitySessionBeanLocal.retrieveConverationsByUser(userEntity.getUserId());

            for (ConversationEntity c : conversationEntities) {
                c.getOfferee().getConversationsAsOfferee().remove(c);
                c.getOfferer().getConversationsAsOfferer().remove(c);
                
            }

            GenericEntity<List<ConversationEntity>> genericEntity = new GenericEntity<List<ConversationEntity>>(conversationEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveConversationById/{conversationId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveConversationById(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("conversationId") Long conversationId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** ConversationResource.retrieveConversationById(): User " + userEntity.getUsername() + " login remotely via web service");

            ConversationEntity c = conversationEntitySessionBeanLocal.retrieveConversationById(conversationId);

            c.getOfferee().getConversationsAsOfferee().remove(c);
            c.getOfferer().getConversationsAsOfferer().remove(c);

            return Response.status(Response.Status.OK).entity(c).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (ConversationNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    private ConversationEntitySessionBeanLocal lookupConversationEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ConversationEntitySessionBeanLocal) c.lookup("java:global/MySalesMatter/MySalesMatter-ejb/ConversationEntitySessionBean!ejb.session.stateless.ConversationEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
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
