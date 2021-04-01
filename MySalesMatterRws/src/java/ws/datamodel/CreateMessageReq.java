/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.MessageEntity;

/**
 *
 * @author rtan3
 */
public class CreateMessageReq {
    
    private String username;
    private String password;
    private MessageEntity newMessageEntity;
    private Long conversationId;
    
    public CreateMessageReq() {
    }

    public CreateMessageReq(String username, String password, MessageEntity newMessageEntity, Long conversationId) {
        this.username = username;
        this.password = password;
        this.newMessageEntity = newMessageEntity;
        this.conversationId = conversationId;
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

    public MessageEntity getNewMessageEntity() {
        return newMessageEntity;
    }

    public void setNewMessageEntity(MessageEntity newMessageEntity) {
        this.newMessageEntity = newMessageEntity;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
    
}
