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
    private String status;
    private Date transactionDate;
    private BigDecimal totalAmt;
 
    public CreateSalesTransactionReq() {   
        
    }

    public CreateSalesTransactionReq(String username, String password, Long offerId, Long userId, String status, Date transactionDate, BigDecimal totalAmt) {
        this.username = username;
        this.password = password;
        this.offerId = offerId;
        this.userId = userId;
        this.status = status;
        this.transactionDate = transactionDate;
        this.totalAmt = totalAmt;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    
    
    
}
