/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import java.math.BigDecimal;
import java.util.Date;
import util.enumeration.ReservationStatus;

/**
 *
 * @author sylvia
 */
public class CreateRentalReservationReq {
    private String username;
    private String password;
    private Date startDate;
    private Date endDate;
    private BigDecimal totalPrice;
    private String issues;
    private ReservationStatus reservationStatus;
    private Long listingId;
    private Long depositId;

    public CreateRentalReservationReq(String username, String password, Date startDate, Date endDate, BigDecimal totalPrice, String issues, ReservationStatus reservationStatus, Long listingId, Long depositId) {
        this.username = username;
        this.password = password;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.issues = issues;
        this.reservationStatus = reservationStatus;
        this.listingId = listingId;
        this.depositId = depositId;
    }
    
    public CreateRentalReservationReq() {
        
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

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public Long getDepositId() {
        return depositId;
    }

    public void setDepositId(Long depositId) {
        this.depositId = depositId;
    }
    
    
    
    
}
