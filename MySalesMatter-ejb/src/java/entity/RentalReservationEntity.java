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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.ReservationStatus;

/**
 *
 * @author sylvia
 */
@Entity
public class RentalReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalReservationId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date startDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date endDate;
    
    @Column(nullable = false)
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal totalPrice;
    
    @Column(length = 100)
    @Size(min=0, max=100)
    private String issues;
    
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(nullable = false)
    private ListingEntity listing;

    public RentalReservationEntity() {
    }

    public RentalReservationEntity(Date startDate, Date endDate, BigDecimal totalPrice, String issues, ReservationStatus reservationStatus) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.issues = issues;
        this.reservationStatus = reservationStatus;
    }
    
    
    

    public Long getRentalReservationId() {
        return rentalReservationId;
    }

    public void setRentalReservationId(Long rentalReservationId) {
        this.rentalReservationId = rentalReservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalReservationId != null ? rentalReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalReservationId fields are not set
        if (!(object instanceof RentalReservationEntity)) {
            return false;
        }
        RentalReservationEntity other = (RentalReservationEntity) object;
        if ((this.rentalReservationId == null && other.rentalReservationId != null) || (this.rentalReservationId != null && !this.rentalReservationId.equals(other.rentalReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalReservationEntity[ id=" + rentalReservationId + " ]";
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public ListingEntity getListing() {
        return listing;
    }

    public void setListing(ListingEntity listing) {
        this.listing = listing;
    }

    
}
