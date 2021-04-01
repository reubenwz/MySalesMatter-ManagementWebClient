/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.OfferEntity;

/**
 *
 * @author sylvia
 */
public class UpdateOfferReq {
    private String username;
    private String password;
    private OfferEntity offerEntity;
    
    public UpdateOfferReq(String username, String password, OfferEntity offerEntity) {
        this.username = username;
        this.password = password;
        this.offerEntity = offerEntity;
    }
    
    public UpdateOfferReq() {
        
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

    public OfferEntity getOfferEntity() {
        return offerEntity;
    }

    public void setOfferEntity(OfferEntity offerEntity) {
        this.offerEntity = offerEntity;
    }
    
    
    
    
}
