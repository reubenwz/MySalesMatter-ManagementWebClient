/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ConversationEntitySessionBeanLocal;
import entity.ConversationEntity;
import entity.OfferEntity;
import entity.UserEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.ConversationNotFoundException;
import util.exception.UserNotFoundException;

/**
 *
 * @author rtan3
 */
@Named(value = "conversationManagementManagedBean")
@ViewScoped
public class ConversationManagementManagedBean implements Serializable {

    @EJB
    private ConversationEntitySessionBeanLocal conversationEntitySessionBeanLocal;
    
    private List<ConversationEntity> conversationEntities;
    private UserEntity currentUser;
    private OfferEntity offerEntityToCreateChat;
    private UserEntity offeree;
    private ConversationEntity newConversationEntity;


    public ConversationManagementManagedBean() {
        conversationEntities = new ArrayList<>();
        currentUser = new UserEntity();
        offeree = new UserEntity();
        newConversationEntity = new ConversationEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
        currentUser = (UserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        try {
            conversationEntities = conversationEntitySessionBeanLocal.retrieveConverationsByUser(currentUser.getUserId());
        } catch (UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while loading conversations: " + ex.getMessage(), null));
        }
    }
    
    public void createConversation(ActionEvent event) {
        System.out.println("CREATING CONVERSATION");
        offerEntityToCreateChat = (OfferEntity) event.getComponent().getAttributes().get("offerEntityToCreateConversation");
        offeree = offerEntityToCreateChat.getUser();
        try {
            newConversationEntity = conversationEntitySessionBeanLocal.createNewEmptyConversation(currentUser.getUserId(), offeree.getUserId());
        } catch (UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating conversation: " + ex.getMessage(), null));
        }
    }
    
    public String getRecipientName(Long conversationId) {
        try {
            ConversationEntity conversation = conversationEntitySessionBeanLocal.retrieveConversationById(conversationId);
            if (conversation.getOfferee().getUserId() == currentUser.getUserId()) {
                return conversation.getOfferer().getName();
            } else {
                return conversation.getOfferee().getName();
            }
        } catch (ConversationNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating conversation: " + ex.getMessage(), null));
        } finally {
            return "";
        }
    }

    public List<ConversationEntity> getConversationEntities() {
        return conversationEntities;
    }

    public void setConversationEntities(List<ConversationEntity> conversationEntities) {
        this.conversationEntities = conversationEntities;
    }

    public OfferEntity getOfferEntityToCreateChat() {
        return offerEntityToCreateChat;
    }

    public void setOfferEntityToCreateChat(OfferEntity offerEntityToCreateChat) {
        this.offerEntityToCreateChat = offerEntityToCreateChat;
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public UserEntity getOfferee() {
        return offeree;
    }

    public void setOfferee(UserEntity offeree) {
        this.offeree = offeree;
    }

    public ConversationEntity getNewConversationEntity() {
        return newConversationEntity;
    }

    public void setNewConversationEntity(ConversationEntity newConversationEntity) {
        this.newConversationEntity = newConversationEntity;
    }
    
}
