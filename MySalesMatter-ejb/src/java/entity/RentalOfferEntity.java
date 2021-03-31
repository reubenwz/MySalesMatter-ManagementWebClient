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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import util.enumeration.OfferType;

/**
 *
 * @author sylvia
 */
@Entity
public class RentalOfferEntity extends OfferEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date startDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date endDate;
   

    public RentalOfferEntity() {
    }

    public RentalOfferEntity(BigDecimal totalPrice, Date offerDate, Date startDate, Date endDate) {
        super(totalPrice, offerDate);
        this.offerType = OfferType.RENTAL;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    

    public Long getRentalOfferId() {
        return offerId;
    }

    public void setRentalOfferId(Long rentalOfferId) {
        this.offerId = rentalOfferId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (offerId != null ? offerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalOfferId fields are not set
        if (!(object instanceof RentalOfferEntity)) {
            return false;
        }
        RentalOfferEntity other = (RentalOfferEntity) object;
        if ((this.offerId == null && other.offerId != null) || (this.offerId != null && !this.offerId.equals(other.offerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalOfferEntity[ id=" + offerId + " ]";
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
}
