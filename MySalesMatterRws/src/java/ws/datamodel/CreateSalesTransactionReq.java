/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author sylvia
 */
public class CreateSalesTransactionReq {
    
    private String username;
    private String password;
    private Long offerId;
    private Long userId;
    private Date transactionDate;
    private BigDecimal totalAmt;
    private String ccName;
    private String ccNum;
    private String cvv;
    private String expiry;
 
    public CreateSalesTransactionReq() {   
        
    }

    public CreateSalesTransactionReq(String username, String password, Long offerId, Long userId, Date transactionDate, BigDecimal totalAmt, String ccName, String ccNum, String cvv, String expiry) {
        this.username = username;
        this.password = password;
        this.offerId = offerId;
        this.userId = userId;
        this.transactionDate = transactionDate;
        this.totalAmt = totalAmt;
        this.ccName = ccName;
        this.ccNum = ccNum;
        this.cvv = cvv;
        this.expiry = expiry;
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

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
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
