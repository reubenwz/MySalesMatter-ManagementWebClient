/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.MessageEntity;
import java.util.Date;

/**
 *
 * @author rtan3
 */
public class CreateMessageReq {
    
    private String username;
    private String password;
    private String message;
    private Long offerId;
    private Long senderId;
    private Long recipientId;
    private Date date;
    
    public CreateMessageReq() {
    }

    public CreateMessageReq(String username, String password, String message, Long offerId, Long senderId, Long recipientId, Date date) {
        this.username = username;
        this.password = password;
        this.message = message;
        this.offerId = offerId;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.date = date;
    }

      
    
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

        
}
