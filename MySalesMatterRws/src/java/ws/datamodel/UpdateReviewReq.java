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
    private Long reviewId;
    private int starRating;
    private String pictPath = "";
    private String desc;

    public UpdateReviewReq(String username, String password, Long reviewId, int starRating, String desc) {
        this.username = username;
        this.password = password;
        this.reviewId = reviewId;
        this.starRating = starRating;
        this.desc = desc;
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

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getPictPath() {
        return pictPath;
    }

    public void setPictPath(String pictPath) {
        this.pictPath = pictPath;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    
}
