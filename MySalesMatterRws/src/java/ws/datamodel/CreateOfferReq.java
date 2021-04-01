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
public class CreateOfferReq {
    private String username;
    private String password;
    private OfferEntity newOfferEntity;
    private Long userId;
    private Long listingId;

    public CreateOfferReq(String username, String password, OfferEntity newOfferEntity, Long userId, Long listingId) {
        this.username = username;
        this.password = password;
        this.newOfferEntity = newOfferEntity;
        this.userId = userId;
        this.listingId = listingId;
    }
    
    public CreateOfferReq() {
        
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

    public OfferEntity getNewOfferEntity() {
        return newOfferEntity;
    }

    public void setNewOfferEntity(OfferEntity newOfferEntity) {
        this.newOfferEntity = newOfferEntity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }
    
    
}
