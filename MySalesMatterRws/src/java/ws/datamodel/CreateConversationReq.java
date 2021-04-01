/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.ConversationEntity;
import entity.MessageEntity;
import java.util.List;

/**
 *
 * @author rtan3
 */
public class CreateConversationReq {
    
    private String username;
    private String password;
    private ConversationEntity newConversationEntity;
    private Long offereeId;
    private Long offererId;
    private List<MessageEntity> messageEntities;

    public CreateConversationReq() {
    }

    public CreateConversationReq(String username, String password, ConversationEntity newConversationEntity, Long offereeId, Long offererId, List<MessageEntity> messageEntities) {
        this.username = username;
        this.password = password;
        this.newConversationEntity = newConversationEntity;
        this.offereeId = offereeId;
        this.offererId = offererId;
        this.messageEntities = messageEntities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ConversationEntity getNewConversationEntity() {
        return newConversationEntity;
    }

    public void setNewConversationEntity(ConversationEntity newConversationEntity) {
        this.newConversationEntity = newConversationEntity;
    }

    public Long getOffereeId() {
        return offereeId;
    }

    public void setOffereeId(Long offereeId) {
        this.offereeId = offereeId;
    }

    public Long getOffererId() {
        return offererId;
    }

    public void setOffererId(Long offererId) {
        this.offererId = offererId;
    }

    public List<MessageEntity> getMessageEntities() {
        return messageEntities;
    }

    public void setMessageEntities(List<MessageEntity> messageEntities) {
        this.messageEntities = messageEntities;
    }
    
    
    
}
