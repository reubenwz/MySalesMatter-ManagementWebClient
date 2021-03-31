/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author sylvia
 */
@Entity
public class ReviewEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    
    @Column(nullable = false, length = 5)
    @NotNull
    @Size(min=1, max=5)
    private int starRating;
    
    @Column(nullable = false, length = 100)
    @NotNull
    @Size(min=1, max=100)
    private String descripion;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(nullable = false)
    private UserEntity reviewer;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(nullable = false)
    private ListingEntity listing;
    
    @NotNull
    @Column(nullable = false)
    private List<String> picturePaths;
    
    public ReviewEntity() {
        this.picturePaths = new ArrayList<>();
    }

    public ReviewEntity(int starRating, String descripion, List<String> picturePaths) {
        this();
        this.starRating = starRating;
        this.descripion = descripion;
        this.picturePaths = picturePaths;
    }
    

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reviewId != null ? reviewId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reviewId fields are not set
        if (!(object instanceof ReviewEntity)) {
            return false;
        }
        ReviewEntity other = (ReviewEntity) object;
        if ((this.reviewId == null && other.reviewId != null) || (this.reviewId != null && !this.reviewId.equals(other.reviewId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReviewEntity[ id=" + reviewId + " ]";
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getDescripion() {
        return descripion;
    }

    public void setDescripion(String descripion) {
        this.descripion = descripion;
    }

    public UserEntity getReviewer() {
        return reviewer;
    }

    public void setReviewer(UserEntity reviewer) {
        this.reviewer = reviewer;
    }

    public ListingEntity getListing() {
        return listing;
    }

    public void setListing(ListingEntity listing) {
        this.listing = listing;
    }
    
    public List<String> getPicturePaths() {
        return picturePaths;
    }

    public void setPicturePaths(List<String> picturePaths) {
        this.picturePaths = picturePaths;
    }

    
}
