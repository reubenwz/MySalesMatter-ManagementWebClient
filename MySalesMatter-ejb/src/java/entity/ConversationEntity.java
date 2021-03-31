/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sylvia
 */
@Entity
public class ConversationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private UserEntity offerer;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private UserEntity offeree;
    
    @OneToMany
    @JoinColumn(nullable = false)
    @NotNull
    private List<MessageEntity> messages;
    
    public ConversationEntity() {
        messages = new ArrayList<>();
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (conversationId != null ? conversationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the conversationId fields are not set
        if (!(object instanceof ConversationEntity)) {
            return false;
        }
        ConversationEntity other = (ConversationEntity) object;
        if ((this.conversationId == null && other.conversationId != null) || (this.conversationId != null && !this.conversationId.equals(other.conversationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ConversationEntity[ id=" + conversationId + " ]";
    }

    public UserEntity getOfferer() {
        return offerer;
    }

    public void setOfferer(UserEntity offerer) {
        this.offerer = offerer;
    }

    public UserEntity getOfferee() {
        return offeree;
    }

    public void setOfferee(UserEntity offeree) {
        this.offeree = offeree;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }
    
}
