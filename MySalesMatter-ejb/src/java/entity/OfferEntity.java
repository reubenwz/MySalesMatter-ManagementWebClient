/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import util.enumeration.OfferType;

/**
 *
 * @author sylvia
 */
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class OfferEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long offerId;
    
    @Column(nullable = false)
    @NotNull
    protected boolean accepted;
    
    @Column(nullable = false)
    @NotNull
    @DecimalMin("0.00")
    protected BigDecimal totalPrice;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    protected Date offerDate;
    
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected OfferType offerType;
    
    @OneToOne(mappedBy = "offer", fetch = FetchType.LAZY)
    protected SalesTransactionEntity sales;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(nullable = false)
    private UserEntity user;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(nullable = false)
    private ListingEntity listing;
    
    
   
    @OneToMany(mappedBy = "offer", fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(nullable = false)
    private List<MessageEntity> message;

    public OfferEntity() {
        this.accepted = false;
        message = new ArrayList<>();
    }

    public OfferEntity(BigDecimal totalPrice, Date offerDate) {
        this();
        this.totalPrice = totalPrice;
        this.offerDate = offerDate;
    }
    
    public OfferEntity(BigDecimal totalPrice, Date offerDate, List<MessageEntity> message) {
        this();
        this.totalPrice = totalPrice;
        this.offerDate = offerDate;
        this.message = message;
    }
   
    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (offerId != null ? offerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the offerId fields are not set
        if (!(object instanceof OfferEntity)) {
            return false;
        }
        OfferEntity other = (OfferEntity) object;
        if ((this.offerId == null && other.offerId != null) || (this.offerId != null && !this.offerId.equals(other.offerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OfferEntity[ id=" + offerId + " ]";
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OfferType getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ListingEntity getListing() {
        return listing;
    }

    public void setListing(ListingEntity listing) {
        this.listing = listing;
    }

    public SalesTransactionEntity getSales() {
        return sales;
    }

    public void setSales(SalesTransactionEntity sales) {
        this.sales = sales;
    }

    public List<MessageEntity> getMessage() {
        return message;
    }

    public void setMessage(List<MessageEntity> message) {
        this.message = message;
    }
    
    
}
