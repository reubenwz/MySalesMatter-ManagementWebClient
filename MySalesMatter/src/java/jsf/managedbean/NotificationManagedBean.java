/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MessageEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.BuyOfferEntity;
import entity.MessageEntity;
import entity.OfferEntity;
import entity.RentalOfferEntity;
import entity.UserEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.enumeration.OfferType;
import util.exception.InputDataValidationException;
import util.exception.MessageNotFoundException;
import util.exception.OfferNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

@Named(value = "notificationManagedBean")
@ViewScoped
public class NotificationManagedBean implements Serializable {

    @EJB
    UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @EJB
    MessageEntitySessionBeanLocal messageEntitySessionBeanLocal;

    private UserEntity currentUser;

    private List<MessageEntity> messages;
    
    private String message;
    
    private OfferEntity offer;

    public NotificationManagedBean() {
        currentUser = new UserEntity();
        messages = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        try {
            setCurrentUser((UserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser"));
            setMessages(messageEntitySessionBeanLocal.retrieveReceivedMessageSByUserId(getCurrentUser().getUserId()));
        } catch (MessageNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while loading conversations: " + ex.getMessage(), null));
        }
    }
    
    public void doReply(ActionEvent event) {
        offer = (OfferEntity) event.getComponent().getAttributes().get("offerEntity");            
        if (offer.getOfferType() == OfferType.RENTAL) {
            offer = (RentalOfferEntity) offer;
        } else {
            offer = (BuyOfferEntity) offer;
        }
        System.out.println("******** " + offer.getTotalPrice());
    }
    
    public void addMessage(ActionEvent event) {
        try {
            messageEntitySessionBeanLocal.addMessage(getMessage(), offer.getOfferId(), currentUser.getUserId(), new Date());
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

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OfferEntity getOffer() {
        return offer;
    }

    public void setOffer(OfferEntity offer) {
        this.offer = offer;
    }

}
