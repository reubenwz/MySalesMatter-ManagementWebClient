/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.TagEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.DeleteTagException;
import util.exception.InvalidLoginCredentialException;
import util.exception.TagNotFoundException;
import util.exception.UpdateTagException;
import ws.datamodel.CreateTagReq;
import ws.datamodel.UpdateTagReq;

/**
 * REST Web Service
 *
 * @author sylvia
 */
@Path("Tag")
public class TagResource {

    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();

    TagEntitySessionBeanLocal tagEntitySessionBeanLocal = lookupTagEntitySessionBeanLocal();


    @Context
    private UriInfo context;

    public TagResource() {
        
    }
    
    @Path("retrieveAllTags")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTags(@QueryParam("username") String username, 
                                        @QueryParam("password") String password)
    {
        try
        {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** TagResource.retrieveAllTags(): User " + userEntity.getUsername() + " login remotely via web service");

            List<TagEntity> tagEntities = tagEntitySessionBeanLocal.retrieveAllTags();
            
            for(TagEntity tagEntity:tagEntities)
            {                
                tagEntity.getListings().clear();
            }
            
            GenericEntity<List<TagEntity>> genericEntity = new GenericEntity<List<TagEntity>>(tagEntities) {
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
    
    @Path("retrieveTag/{tagId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveTagByTagId(@QueryParam("username") String username, 
                                        @QueryParam("password") String password,
                                        @PathParam("tagId") Long tagId)
    {
        try
        {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** TagResource.retrieveTagByTagId(): User " + userEntity.getUsername() + " login remotely via web service");

            TagEntity tagEntity = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
            
//            for (ListingEntity l : tagEntity.getListings()) {
//                l.getOffers().clear();
//                l.getReservations().clear();
//                l.getReservedDates().clear();
//                l.getReviews().clear();
//                l.getTags().clear();
//            }
            tagEntity.getListings().clear();
            
            return Response.status(Response.Status.OK).entity(tagEntity).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(TagNotFoundException ex)
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
    public Response updateTag(UpdateTagReq updateTagReq) {
        if (updateTagReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(updateTagReq.getUsername(), updateTagReq.getPassword());
                System.out.println("********** TagResource.updateTag(): User " + userEntity.getUsername() + " login remotely via web service");

                tagEntitySessionBeanLocal.updateTag(updateTagReq.getTagEntity());

                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (TagNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (UpdateTagException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update tag request").build();
        }
    }

    @Path("{tagId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTag(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("tagId") Long tagId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** TagResource.deleteTag(): User " + userEntity.getUsername() + " login remotely via web service");

            tagEntitySessionBeanLocal.deleteTag(tagId);

            return Response.status(Response.Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (TagNotFoundException | DeleteTagException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewTagEntity(CreateTagReq createTagReq)
    {
        if(createTagReq != null)
        {
            try
            {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createTagReq.getUsername(), createTagReq.getPassword());
                System.out.println("********** TagResource.createNewTagEntity(): User " + userEntity.getUsername() + " login remotely via web service");
                
                TagEntity tagEntity  = tagEntitySessionBeanLocal.createNewTagEntity(createTagReq.getTagEntity());                
                
                return Response.status(Response.Status.OK).entity(tagEntity.getTagId()).build();
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
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new tag request").build();
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
