/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.ReviewEntity;

/**
 *
 * @author sylvia
 */
public class CreateReviewReq {
    
    private String username;
    private String password;
    private ReviewEntity reviewEntity;
    private Long reviewerId;
    private Long listingId;

    public CreateReviewReq(ReviewEntity reviewEntity, String username, String password, Long reviewerId, Long listingId) {
        this.reviewEntity = reviewEntity;
        this.username = username;
        this.password = password;
        this.reviewerId = reviewerId;
        this.listingId = listingId;
    }
    
    public CreateReviewReq() {
        
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

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public ReviewEntity getReviewEntity() {
        return reviewEntity;
    }

    public void setReviewEntity(ReviewEntity reviewEntity) {
        this.reviewEntity = reviewEntity;
    }
    
    
    
}
