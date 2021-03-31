/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ConversationEntity;
import entity.MessageEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author rtan3
 */
@Named(value = "viewConversationManagedBean")
@ViewScoped
public class ViewConversationManagedBean implements Serializable {

    private ConversationEntity conversationEntityToView;
    private List<MessageEntity> messageEntities;
    
    public ViewConversationManagedBean() {
        conversationEntityToView = new ConversationEntity();
        messageEntities = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct()
    {        
        messageEntities = conversationEntityToView.getMessages();
    }

    public ConversationEntity getConversationEntityToView() {
        return conversationEntityToView;
    }

    public void setConversationEntityToView(ConversationEntity conversationEntityToView) {
        this.conversationEntityToView = conversationEntityToView;
    }

    public List<MessageEntity> getMessageEntities() {
        return messageEntities;
    }

    public void setMessageEntities(List<MessageEntity> messageEntities) {
        this.messageEntities = messageEntities;
    }
    
}
