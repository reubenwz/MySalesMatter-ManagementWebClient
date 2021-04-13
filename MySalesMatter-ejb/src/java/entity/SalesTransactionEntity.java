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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.Status;

/**
 *
 * @author sylvia
 */
@Entity
public class SalesTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesTransactionId;
    
    @Column(nullable = false)
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal totalAmt;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date transactionDate;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String ccName;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 16, max = 16)
    private String ccNum;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 3, max = 3)
    private String cvv;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 5, max = 5)
    private String expiry;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(nullable = false)
    private UserEntity user;
    
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(nullable = false)
    private OfferEntity offer;

    public SalesTransactionEntity() {
    }

    public SalesTransactionEntity(BigDecimal totalAmt, Date transactionDate) {
        //this.status = status;
        this.totalAmt = totalAmt;
        this.transactionDate = transactionDate;
    }
    
    
    public Long getSalesTransactionId() {
        return salesTransactionId;
    }

    public void setSalesTransactionId(Long salesTransactionId) {
        this.salesTransactionId = salesTransactionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (salesTransactionId != null ? salesTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the salesTransactionId fields are not set
        if (!(object instanceof SalesTransactionEntity)) {
            return false;
        }
        SalesTransactionEntity other = (SalesTransactionEntity) object;
        if ((this.salesTransactionId == null && other.salesTransactionId != null) || (this.salesTransactionId != null && !this.salesTransactionId.equals(other.salesTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SalesTransactionEntity[ id=" + salesTransactionId + " ]";
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }


    public OfferEntity getOffer() {
        return offer;
    }

    public void setOffer(OfferEntity offer) {
        this.offer = offer;
    }

    public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = ccName;
    }

    public String getCcNum() {
        return ccNum;
    }

    public void setCcNum(String ccNum) {
        this.ccNum = ccNum;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
    
}
