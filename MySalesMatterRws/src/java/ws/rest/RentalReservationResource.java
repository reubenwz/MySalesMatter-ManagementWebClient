/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.RentalReservationEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.RentalReservationEntity;
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
import util.exception.CreateNewRentalReservationException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ListingNotFoundException;
import util.exception.RentalReservationExistException;
import util.exception.RentalReservationNotFoundException;
import util.exception.SalesTransactionNotFoundException;
import util.exception.UnknownPersistenceException;
import ws.datamodel.CreateRentalReservationReq;
import ws.datamodel.UpateReservationStatusReq;

/**
 * REST Web Service
 *
 * @author sylvia
 */
@Path("RentalReservation")
public class RentalReservationResource {

    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();

    RentalReservationEntitySessionBeanLocal rentalReservationEntitySessionBeanLocal = lookupRentalReservationEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    public RentalReservationResource() {
    }

    @Path("retrieveAllRentalReservation")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRentalReservation(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** RentalReservationResource.retrieveAllRentalReservation(): User " + userEntity.getUsername() + " login remotely via web service");

            List<RentalReservationEntity> rentalReservationEntities = rentalReservationEntitySessionBeanLocal.retrieveAllRentalReservation();
            for (RentalReservationEntity r : rentalReservationEntities) {
                r.setListing(null);
            }

            GenericEntity<List<RentalReservationEntity>> genericEntity = new GenericEntity<List<RentalReservationEntity>>(rentalReservationEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveReservation/{reservationId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveRentalReservationById(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("reservationId") Long reservationId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** RentalReservationResource.retrieveRentalReservationById(): User " + userEntity.getUsername() + " login remotely via web service");

            RentalReservationEntity rentalReservationEntity = rentalReservationEntitySessionBeanLocal.retrieveRentalReservationById(reservationId);

            rentalReservationEntity.setListing(null);

            return Response.status(Response.Status.OK).entity(rentalReservationEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (RentalReservationNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReservationStatus(UpateReservationStatusReq upateReservationStatusReq) {
        if (upateReservationStatusReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(upateReservationStatusReq.getUsername(), upateReservationStatusReq.getPassword());
                System.out.println("********** RentalReservationResource.updateReservationStatus(): User " + userEntity.getUsername() + " login remotely via web service");
                rentalReservationEntitySessionBeanLocal.updateReservationStatus(upateReservationStatusReq.getRentalReservationId(), upateReservationStatusReq.getStatus());
                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (RentalReservationNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update reservation request").build();
        }
    }

    @Path("{reservationId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRentalReservation(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("reservationId") Long reservationId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** RentalReservationResource.deleteRentalReservation(): User " + userEntity.getUsername() + " login remotely via web service");

            rentalReservationEntitySessionBeanLocal.deleteRentalReservation(reservationId);

            return Response.status(Response.Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (RentalReservationNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRentalReservation(CreateRentalReservationReq createRentalReservationReq) {
        if (createRentalReservationReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createRentalReservationReq.getUsername(), createRentalReservationReq.getPassword());
                System.out.println("********** RentalReservationResource.createRentalReservation(): User " + userEntity.getUsername() + " login remotely via web service");

                RentalReservationEntity rentalReservationEntity = rentalReservationEntitySessionBeanLocal.createRentalReservation(createRentalReservationReq.getStartDate(), createRentalReservationReq.getEndDate(), createRentalReservationReq.getTotalPrice(), createRentalReservationReq.getIssues(), createRentalReservationReq.getReservationStatus(), createRentalReservationReq.getListingId(), createRentalReservationReq.getDepositId());
                return Response.status(Response.Status.OK).entity(rentalReservationEntity.getRentalReservationId()).build();
            } catch (SalesTransactionNotFoundException | RentalReservationExistException | UnknownPersistenceException | InputDataValidationException | ListingNotFoundException | CreateNewRentalReservationException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new rental reservation request").build();
        }
    }

    private RentalReservationEntitySessionBeanLocal lookupRentalReservationEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RentalReservationEntitySessionBeanLocal) c.lookup("java:global/MySalesMatter/MySalesMatter-ejb/RentalReservationEntitySessionBean!ejb.session.stateless.RentalReservationEntitySessionBeanLocal");
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
