/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import util.enumeration.OfferType;


/**
 *
 * @author sylvia
 */
@Entity
public class BuyOfferEntity extends OfferEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public BuyOfferEntity() {
        super();
    }

    public BuyOfferEntity(BigDecimal totalPrice, Date offerDate, OfferType offerType) {
        super(totalPrice, offerDate);
        this.offerType = OfferType.BUY;
    }

    

    public Long getBuyOfferId() {
        return offerId;
    }

    public void setBuyOfferId(Long buyOfferId) {
        this.offerId = buyOfferId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (offerId != null ? offerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the buyOfferId fields are not set
        if (!(object instanceof BuyOfferEntity)) {
            return false;
        }
        BuyOfferEntity other = (BuyOfferEntity) object;
        if ((this.offerId == null && other.offerId != null) || (this.offerId != null && !this.offerId.equals(other.offerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BuyOfferEntity[ id=" + offerId + " ]";
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }
    
}
