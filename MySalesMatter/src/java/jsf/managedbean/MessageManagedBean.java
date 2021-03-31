/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MessageEntitySessionBeanLocal;
import entity.MessageEntity;
import entity.UserEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import util.exception.MessageNotFoundException;

/**
 *
 * @author rtan3
 */
@Named(value = "messageManagedBean")
@RequestScoped
public class MessageManagedBean {

    @EJB
    private MessageEntitySessionBeanLocal messageEntitySessionBeanLocal;

    private MessageEntity messageEntity;
    private UserEntity currentUser;
    
    
    public MessageManagedBean() {
        messageEntity = new MessageEntity();
        currentUser = new UserEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
        currentUser = (UserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
    }
    
    public boolean sentByUser(Long messageId) {
        try {
            messageEntity = messageEntitySessionBeanLocal.retrieveMessageById(messageId);
        } catch (MessageNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while loading conversations: " + ex.getMessage(), null));
        }
        if (messageEntity.getSender().getUserId() == currentUser.getUserId()) {
            return true;
        } return false;
    }
    
}
