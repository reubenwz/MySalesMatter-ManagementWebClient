/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MessageEntitySessionBeanLocal;
import ejb.session.stateless.OfferEntitySessionBeanLocal;
import ejb.session.stateless.SalesTransactionEntitySessionBeanLocal;
import entity.BuyOfferEntity;
import entity.OfferEntity;
import entity.RentalOfferEntity;
import entity.UserEntity;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.enterprise.context.SessionScoped;
import util.enumeration.OfferType;
import util.exception.CreateNewTransactionException;
import util.exception.InputDataValidationException;
import util.exception.OfferNotFoundException;
import util.exception.SalesTransactionExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author rtan3
 */
@Named(value = "transactionManagedBean")
@SessionScoped
public class TransactionManagedBean implements Serializable {

    @EJB
    private SalesTransactionEntitySessionBeanLocal salesTransactionEntitySessionBeanLocal;
    @EJB
    private MessageEntitySessionBeanLocal messageEntitySessionBeanLocal;
    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;

    private UserEntity currentUser;
    private OfferEntity acceptedOfferToMakePayment;
    private RentalOfferEntity acceptedRentalOffer;
    private BuyOfferEntity acceptedBuyOffer;
    private String message;

    public TransactionManagedBean() {
        currentUser = new UserEntity();
    }

    @PostConstruct
    public void postConstruct() {
        setCurrentUser((UserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser"));
    }

    public boolean isRentalOffer(OfferType type) {
        if (type == OfferType.RENTAL) {
            return true;
        }
        return false;
    }

    public void doMakePayment(ActionEvent event) {
        
        acceptedOfferToMakePayment = (OfferEntity) event.getComponent().getAttributes().get("acceptedOfferToPay");
        if (acceptedOfferToMakePayment.getOfferType() == OfferType.RENTAL) {
            acceptedRentalOffer = (RentalOfferEntity) acceptedOfferToMakePayment;
        } else {
            acceptedBuyOffer = (BuyOfferEntity) acceptedOfferToMakePayment;
        }
        try {
            offerEntitySessionBeanLocal.doSetPaid(acceptedOfferToMakePayment.getOfferId());
        } catch (OfferNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while accepting the offer: " + ex.getMessage(), null));
        }
    }

    public void doArrangeMeetup(ActionEvent event) {
        acceptedOfferToMakePayment = (OfferEntity) event.getComponent().getAttributes().get("acceptedOffer");
        if (acceptedOfferToMakePayment.getOfferType() == OfferType.RENTAL) {
            acceptedRentalOffer = (RentalOfferEntity) acceptedOfferToMakePayment;
        } else {
            acceptedBuyOffer = (BuyOfferEntity) acceptedOfferToMakePayment;
        }
        System.out.println("******** " + acceptedOfferToMakePayment.getTotalPrice());
    }

    public void makePayment(ActionEvent event) {
        try {
            salesTransactionEntitySessionBeanLocal.createSalesTransaction(acceptedOfferToMakePayment.getOfferId(), currentUser.getUserId(), "paid", new Date(), acceptedOfferToMakePayment.getTotalPrice());
        } catch (UnknownPersistenceException | InputDataValidationException | UserNotFoundException | CreateNewTransactionException | SalesTransactionExistException | OfferNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while making the payment: " + ex.getMessage(), null));
        }
    }

    public void addMessage(ActionEvent event) {
        try {
            messageEntitySessionBeanLocal.addMessage(message, acceptedOfferToMakePayment.getOfferId(), currentUser.getUserId(), new Date());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Message sent successfully", null));
        } catch (UserNotFoundException | OfferNotFoundException | InputDataValidationException | UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while making the payment: " + ex.getMessage(), null));
        }
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public OfferEntity getAcceptedOfferToMakePayment() {
        return acceptedOfferToMakePayment;
    }

    public void setAcceptedOfferToMakePayment(OfferEntity acceptedOfferToMakePayment) {
        this.acceptedOfferToMakePayment = acceptedOfferToMakePayment;
    }

    public RentalOfferEntity getAcceptedRentalOffer() {
        return acceptedRentalOffer;
    }

    public void setAcceptedRentalOffer(RentalOfferEntity acceptedRentalOffer) {
        this.acceptedRentalOffer = acceptedRentalOffer;
    }

    public BuyOfferEntity getAcceptedBuyOffer() {
        return acceptedBuyOffer;
    }

    public void setAcceptedBuyOffer(BuyOfferEntity acceptedBuyOffer) {
        this.acceptedBuyOffer = acceptedBuyOffer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
