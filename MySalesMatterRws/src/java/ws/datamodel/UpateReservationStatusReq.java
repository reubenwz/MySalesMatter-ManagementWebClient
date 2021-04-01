/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import util.enumeration.ReservationStatus;

/**
 *
 * @author sylvia
 */
public class UpateReservationStatusReq {
    private String username;
    private String password;
    private Long rentalReservationId;
    private ReservationStatus status;

    public UpateReservationStatusReq(String username, String password, Long rentalReservationId, ReservationStatus status) {
        this.username = username;
        this.password = password;
        this.rentalReservationId = rentalReservationId;
        this.status = status;
    }
    
    public UpateReservationStatusReq() {
        
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

    public Long getRentalReservationId() {
        return rentalReservationId;
    }

    public void setRentalReservationId(Long rentalReservationId) {
        this.rentalReservationId = rentalReservationId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    
    
}
