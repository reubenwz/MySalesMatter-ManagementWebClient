/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.OfferEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.MessageEntity;
import entity.OfferEntity;
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
import util.exception.CreateNewOfferException;
import util.exception.DeleteOfferException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OfferNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;
import ws.datamodel.CreateOfferReq;
import ws.datamodel.UpdateOfferReq;

/**
 * REST Web Service
 *
 * @author sylvia
 */
@Path("Offer")
public class OfferResource {

    OfferEntitySessionBeanLocal offerEntitySessionBeanLocal = lookupOfferEntitySessionBeanLocal();

    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    public OfferResource() {
    }

    @Path("retrieveAllOffers")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllOffers(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** OfferResource.retrieveAllOffers(): User " + userEntity.getUsername() + " login remotely via web service");

            List<OfferEntity> offerEntities = offerEntitySessionBeanLocal.retrieveAllOffers();
            for (OfferEntity o : offerEntities) {
                o.getListing().getOffers().clear();
                //o.getListing().getReservations().clear();
                o.getListing().getReviews().clear();
                o.getListing().getTags().clear();
                o.getListing().setUser(null);
                o.getListing().setCategoryEntity(null);
                o.getSales().setOffer(null);
                o.getSales().setUser(null);
                //o.getUser().getConversationsAsOfferee().clear();
                //o.getUser().getConversationsAsOfferer().clear();
                o.getUser().getLikedItems().clear();
                o.getUser().getListings().clear();
                o.getUser().getOffers().clear();
                o.getUser().getReviews().clear();
                o.getUser().getTransactions().clear();
                for (MessageEntity m : o.getMessage()) {
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
            }

            GenericEntity<List<OfferEntity>> genericEntity = new GenericEntity<List<OfferEntity>>(offerEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveOffer/{offerId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOfferById(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("offerId") Long offerId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** OfferResource.retrieveOfferById(): User " + userEntity.getUsername() + " login remotely via web service");

            OfferEntity o = offerEntitySessionBeanLocal.retrieveOfferById(offerId);

            o.getListing().getOffers().clear();
            //o.getListing().getReservations().clear();
            o.getListing().getReviews().clear();
            o.getListing().getTags().clear();
            o.getListing().setUser(null);
            o.getListing().setCategoryEntity(null);
            o.getSales().setOffer(null);
            o.getSales().setUser(null);
            //o.getUser().getConversationsAsOfferee().clear();
            //o.getUser().getConversationsAsOfferer().clear();
            o.getUser().getLikedItems().clear();
            o.getUser().getListings().clear();
            o.getUser().getOffers().clear();
            o.getUser().getReviews().clear();
            o.getUser().getTransactions().clear();
            
            for (MessageEntity m : o.getMessage()) {
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
            

            return Response.status(Response.Status.OK).entity(o).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (OfferNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("updateOffer/{offerId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOffer(UpdateOfferReq updateOfferReq) {
        if (updateOfferReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(updateOfferReq.getUsername(), updateOfferReq.getPassword());
                System.out.println("********** OfferResource.updateOffer(): User " + userEntity.getUsername() + " login remotely via web service");
                offerEntitySessionBeanLocal.updateOffer(updateOfferReq.getOfferEntity());
                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (OfferNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update offer request").build();
        }
    }

    @Path("accceptOffer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response acceptOffer(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("offerId") Long offerId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** OfferResource.acceptOffer(): User " + userEntity.getUsername() + " login remotely via web service");
            offerEntitySessionBeanLocal.acceptOffer(offerId);
            return Response.status(Response.Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (OfferNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

    @Path("{offerId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOffer(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("offerId") Long offerId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** OfferResource.deleteOffer(): User " + userEntity.getUsername() + " login remotely via web service");
            offerEntitySessionBeanLocal.deleteOffer(offerId);
            return Response.status(Response.Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (DeleteOfferException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewOffer(CreateOfferReq createOfferReq) {
        if (createOfferReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createOfferReq.getUsername(), createOfferReq.getPassword());
                System.out.println("********** OfferResource.createNewOffer(): User " + userEntity.getUsername() + " login remotely via web service");
                OfferEntity offerEntity = offerEntitySessionBeanLocal.createNewOffer(createOfferReq.getNewOfferEntity(), createOfferReq.getUserId(), createOfferReq.getListingId());
                return Response.status(Response.Status.OK).entity(offerEntity.getOfferId()).build();
            } catch (UnknownPersistenceException | InputDataValidationException | CreateNewOfferException | UserNotFoundException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new offer request").build();
        }
    }

    @Path("retrieveOfferByUserId")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOffersByUserId(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** OfferResource.retrieveOffersByUserId(): User " + userEntity.getUsername() + " login remotely via web service");

            List<OfferEntity> offerEntities = offerEntitySessionBeanLocal.retrieveOffersByUserId(userEntity.getUserId());
            for (OfferEntity o : offerEntities) {
                o.getListing().getOffers().clear();
                //o.getListing().getReservations().clear();
                o.getListing().getReviews().clear();
                o.getListing().getTags().clear();
                o.getListing().setUser(null);
                o.getListing().setCategoryEntity(null);
                o.getSales().setOffer(null);
                o.getSales().setUser(null);
                //o.getUser().getConversationsAsOfferee().clear();
                //o.getUser().getConversationsAsOfferer().clear();
                o.getUser().getLikedItems().clear();
                o.getUser().getListings().clear();
                o.getUser().getOffers().clear();
                o.getUser().getReviews().clear();
                o.getUser().getTransactions().clear();
                
                for (MessageEntity m : o.getMessage()) {
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
            }

            GenericEntity<List<OfferEntity>> genericEntity = new GenericEntity<List<OfferEntity>>(offerEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveOfferByListingId")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOffersByListingId(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("listingId") Long listingId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** OfferResource.retrieveOffersByListingId(): User " + userEntity.getUsername() + " login remotely via web service");

            List<OfferEntity> offerEntities = offerEntitySessionBeanLocal.retrieveOffersByListingId(listingId);
            for (OfferEntity o : offerEntities) {
                o.getListing().getOffers().clear();
                //o.getListing().getReservations().clear();
                o.getListing().getReviews().clear();
                o.getListing().getTags().clear();
                o.getListing().setUser(null);
                o.getListing().setCategoryEntity(null);
                o.getSales().setOffer(null);
                o.getSales().setUser(null);
                //o.getUser().getConversationsAsOfferee().clear();
                //o.getUser().getConversationsAsOfferer().clear();
                o.getUser().getLikedItems().clear();
                o.getUser().getListings().clear();
                o.getUser().getOffers().clear();
                o.getUser().getReviews().clear();
                o.getUser().getTransactions().clear();
                
                for (MessageEntity m : o.getMessage()) {
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
            }

            GenericEntity<List<OfferEntity>> genericEntity = new GenericEntity<List<OfferEntity>>(offerEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
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

}
