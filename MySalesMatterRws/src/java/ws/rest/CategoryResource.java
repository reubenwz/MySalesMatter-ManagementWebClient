/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.ListingEntity;
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
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCategoryException;
import util.exception.UserNotFoundException;
import ws.datamodel.CreateCategoryReq;
import ws.datamodel.UpdateCategoryReq;

/**
 * REST Web Service
 *
 * @author rtan3
 */
@Path("Category")
public class CategoryResource {
    
    CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal = lookupCategoryEntitySessionBeanLocal();
    
    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    public CategoryResource() {
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewCategory(CreateCategoryReq createCategoryReq) {
        if (createCategoryReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createCategoryReq.getUsername(), createCategoryReq.getPassword());
                System.out.println("********** CategoryResource.createNewCategory(): User " + userEntity.getUsername() + " login remotely via web service");
                CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.createNewCategoryEntity(createCategoryReq.getNewCategoryEntity(), createCategoryReq.getParentCategoryId());
                return Response.status(Response.Status.OK).entity(categoryEntity.getCategoryId()).build();
            } catch (InputDataValidationException | CreateNewCategoryException  ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new category request").build();
        }
    }
    
    @Path("retrieveAllCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategories(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** CategoryResource.retrieveAllCategories(): User " + userEntity.getUsername() + " login remotely via web service");

            List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllCategories();
            for (CategoryEntity c : categoryEntities) {
                c.getParentCategoryEntity().getSubCategoryEntities().remove(c);
                
                List<CategoryEntity> subcategories = c.getSubCategoryEntities();
                for (CategoryEntity s : subcategories) {
                    s.setParentCategoryEntity(null);
                }
                
                List<ListingEntity> listings = c.getListings();
                for (ListingEntity l : listings) {
                    l.setCategoryEntity(null);
                }
            }

            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(categoryEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllRootCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRootCategories(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** CategoryResource.retrieveAllRootCategories(): User " + userEntity.getUsername() + " login remotely via web service");

            List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllCategories();
            for (CategoryEntity c : categoryEntities) {
                c.getParentCategoryEntity().getSubCategoryEntities().remove(c);
                
                List<CategoryEntity> subcategories = c.getSubCategoryEntities();
                for (CategoryEntity s : subcategories) {
                    s.setParentCategoryEntity(null);
                }
                
                List<ListingEntity> listings = c.getListings();
                for (ListingEntity l : listings) {
                    l.setCategoryEntity(null);
                }
            }

            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(categoryEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllLeafCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllLeafCategories(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** CategoryResource.retrieveAllLeafCategories(): User " + userEntity.getUsername() + " login remotely via web service");

            List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllCategories();
            for (CategoryEntity c : categoryEntities) {
                c.getParentCategoryEntity().getSubCategoryEntities().remove(c);
                
                List<CategoryEntity> subcategories = c.getSubCategoryEntities();
                for (CategoryEntity s : subcategories) {
                    s.setParentCategoryEntity(null);
                }
                
                List<ListingEntity> listings = c.getListings();
                for (ListingEntity l : listings) {
                    l.setCategoryEntity(null);
                }
            }

            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(categoryEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllCategoriesWithoutListing")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategoriesWithoutListing(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** CategoryResource.retrieveAllCategoriesWithoutListing(): User " + userEntity.getUsername() + " login remotely via web service");

            List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllCategories();
            for (CategoryEntity c : categoryEntities) {
                c.getParentCategoryEntity().getSubCategoryEntities().remove(c);
                
                List<CategoryEntity> subcategories = c.getSubCategoryEntities();
                for (CategoryEntity s : subcategories) {
                    s.setParentCategoryEntity(null);
                }
                
                List<ListingEntity> listings = c.getListings();
                for (ListingEntity l : listings) {
                    l.setCategoryEntity(null);
                }
            }

            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(categoryEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveCategoryByCategoryId/{categoryId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCategoryByCategoryId(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("categoryId") Long categoryId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** CategoryResource.retrieveCategoryByCategoryId(): User " + userEntity.getUsername() + " login remotely via web service");

            CategoryEntity c = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

            c.getParentCategoryEntity().getSubCategoryEntities().remove(c);
                
                List<CategoryEntity> subcategories = c.getSubCategoryEntities();
                for (CategoryEntity s : subcategories) {
                    s.setParentCategoryEntity(null);
                }
                
                List<ListingEntity> listings = c.getListings();
                for (ListingEntity l : listings) {
                    l.setCategoryEntity(null);
                }

            return Response.status(Response.Status.OK).entity(c).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (CategoryNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("updateCategory/{categoryId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategory(UpdateCategoryReq updateCategoryReq) {
        if (updateCategoryReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(updateCategoryReq.getUsername(), updateCategoryReq.getPassword());
                System.out.println("********** CategoryResource.updateCategory(): User " + userEntity.getUsername() + " login remotely via web service");
                categoryEntitySessionBeanLocal.updateCategory(updateCategoryReq.getCategoryEntity(), updateCategoryReq.getParentCategoryId());
                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (CategoryNotFoundException | InputDataValidationException | UpdateCategoryException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update category request").build();
        }
    }
    
    @Path("{categoryId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("categoryId") Long categoryId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** CategoryResource.deleteCategory(): User " + userEntity.getUsername() + " login remotely via web service");
            categoryEntitySessionBeanLocal.deleteCategory(categoryId);
            return Response.status(Response.Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (DeleteCategoryException | CategoryNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
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
