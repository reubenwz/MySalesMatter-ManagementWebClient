/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.LikedItemEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.LikedItemEntity;
import entity.UserEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ItemNotLikedException;
import util.exception.LikedItemEntityNotFoundException;
import util.exception.ListingNotFoundException;
import util.exception.UserNotFoundException;
import ws.datamodel.CreateLikedItemReq;
import ws.datamodel.UnlikeItemReq;

/**
 * REST Web Service
 *
 * @author rtan3
 */
@Path("LikedItem")
public class LikedItemResource {
    
    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();

    LikedItemEntitySessionBeanLocal likedItemEntitySessionBeanLocal = lookupLikedItemEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    public LikedItemResource() {
    }
    
    @Path("getLikedItems")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLikedItems(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** LikedItemResource.getLikedItems(): User " + userEntity.getUsername() + " login remotely via web service");

            List<LikedItemEntity> likedItemEntities = likedItemEntitySessionBeanLocal.getLikedItems(userEntity.getUserId());
            for (LikedItemEntity l : likedItemEntities) {
                l.getUser().getLikedItems().remove(l);
            }

            GenericEntity<List<LikedItemEntity>> genericEntity = new GenericEntity<List<LikedItemEntity>>(likedItemEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewLikedItem(CreateLikedItemReq createLikedItemReq) {
        if (createLikedItemReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createLikedItemReq.getUsername(), createLikedItemReq.getPassword());
                System.out.println("********** LikedItemResource.createNewLikedItem(): User " + userEntity.getUsername() + " login remotely via web service");
                LikedItemEntity likedItemEntity = likedItemEntitySessionBeanLocal.createNewLikedItem(userEntity.getUserId(), createLikedItemReq.getListingId());
                return Response.status(Response.Status.OK).entity(likedItemEntity.getLikedItemId()).build();
            } catch (UserNotFoundException | InputDataValidationException | ListingNotFoundException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new liked item request").build();
        }
    }
    
    @Path("unlikeItem")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unlikeItem(UnlikeItemReq unlikeItemReq) {
        if (unlikeItemReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(unlikeItemReq.getUsername(), unlikeItemReq.getPassword());
                System.out.println("********** LikedItemResource.unlikeItem(): User " + userEntity.getUsername() + " login remotely via web service");
                likedItemEntitySessionBeanLocal.unlikeItem(userEntity.getUserId(), unlikeItemReq.getListingId());
                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (ListingNotFoundException | ItemNotLikedException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid unlike item request").build();
        }
    }
    
    @Path("retrieveLikedItemById/{likedItemId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveLikedItemById(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("likedItemId") Long likedItemId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** LikedItemResource.retrieveLikedItemById(): User " + userEntity.getUsername() + " login remotely via web service");

            LikedItemEntity l = likedItemEntitySessionBeanLocal.retrieveLikedItemEntityById(likedItemId);
            
            l.getUser().getLikedItems().remove(l);

            return Response.status(Response.Status.OK).entity(l).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (LikedItemEntityNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private LikedItemEntitySessionBeanLocal lookupLikedItemEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (LikedItemEntitySessionBeanLocal) c.lookup("java:global/MySalesMatter/MySalesMatter-ejb/LikedItemEntitySessionBean!ejb.session.stateless.LikedItemEntitySessionBeanLocal");
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
