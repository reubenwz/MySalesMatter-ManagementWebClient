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
public class UpdateReviewReq {
    private String username;
    private String password;
    private ReviewEntity review;

    public UpdateReviewReq(String username, String password, ReviewEntity review) {
        this.username = username;
        this.password = password;
        this.review = review;
    }
    
    public UpdateReviewReq() {
        
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

    public ReviewEntity getReview() {
        return review;
    }

    public void setReview(ReviewEntity review) {
        this.review = review;
    }
    
}
