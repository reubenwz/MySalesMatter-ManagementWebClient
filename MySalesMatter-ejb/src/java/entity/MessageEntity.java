/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author sylvia
 */
@Entity
public class MessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date sentDate;
   
    @Column(nullable = false, length = 100)
    @NotNull
    @Size(min=1, max=100)
    private String message;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    private UserEntity sender;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    private UserEntity recipient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    private OfferEntity offer;
    
    public MessageEntity() {
        
    }

    public MessageEntity(Date sentDate, String message) {
        this.sentDate = sentDate;
        this.message = message;
    }
    
    

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (messageId != null ? messageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the messageId fields are not set
        if (!(object instanceof MessageEntity)) {
            return false;
        }
        MessageEntity other = (MessageEntity) object;
        if ((this.messageId == null && other.messageId != null) || (this.messageId != null && !this.messageId.equals(other.messageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MessageEntity[ id=" + messageId + " ]";
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getRecipient() {
        return recipient;
    }

    public void setRecipient(UserEntity recipient) {
        this.recipient = recipient;
    }

    public OfferEntity getOffer() {
        return offer;
    }

    public void setOffer(OfferEntity offer) {
        this.offer = offer;
    }
    
}
