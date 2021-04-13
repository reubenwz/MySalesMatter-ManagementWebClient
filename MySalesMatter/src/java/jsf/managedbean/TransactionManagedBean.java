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
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String ccName;
    private String ccNum;
    private String cvv;
    private String expiry;
    private BigDecimal totalAmt;

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
        totalAmt = acceptedOfferToMakePayment.getTotalPrice().divide(acceptedOfferToMakePayment.getListing().getRentalPrice()).multiply(new BigDecimal(5));
        if (acceptedOfferToMakePayment.getOfferType() == OfferType.RENTAL) {
            acceptedRentalOffer = (RentalOfferEntity) acceptedOfferToMakePayment;
        } else {
            acceptedBuyOffer = (BuyOfferEntity) acceptedOfferToMakePayment;
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
            salesTransactionEntitySessionBeanLocal.createSalesTransaction(acceptedOfferToMakePayment.getOfferId(), currentUser.getUserId(), new Date(), this.totalAmt, this.ccName, this.ccNum, this.cvv, this.expiry);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment made successfully", null));
            offerEntitySessionBeanLocal.doSetPaid(acceptedOfferToMakePayment.getOfferId());
        } catch (UnknownPersistenceException | InputDataValidationException | UserNotFoundException | CreateNewTransactionException | SalesTransactionExistException | OfferNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while making the payment: " + ex.getMessage(), null));
        }
    }
    
    public boolean paidDeposit(OfferEntity offer) {
        if (offer.getOfferType() == OfferType.BUY) {
            return true;
        } else if (offer.isPaid()) {
            return true;
        } return false;
    }
    
    public void doMakePurchase(ActionEvent event) {
        OfferEntity offerPurchase = (OfferEntity) event.getComponent().getAttributes().get("acceptedOfferToBuy");     
        try {
            offerEntitySessionBeanLocal.doSetPaid(offerPurchase.getOfferId());
        } catch (OfferNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while making the payment: " + ex.getMessage(), null));
        }

    }
    
    public boolean toBuy(OfferEntity offer) {
        if (offer.getOfferType() == OfferType.BUY && !offer.isPaid()) {
            return true;
        } return false;
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

    public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = ccName;
    }

    public String getCcNum() {
        return ccNum;
    }

    public void setCcNum(String ccNum) {
        this.ccNum = ccNum;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

}
