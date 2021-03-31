/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sylvia
 */
@Entity
public class LikedItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likedItemId;
    
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    private ListingEntity listing;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    private UserEntity user;

    public LikedItemEntity() {
    }
    

    public Long getLikedItemId() {
        return likedItemId;
    }

    public void setLikedItemId(Long likedItemId) {
        this.likedItemId = likedItemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (likedItemId != null ? likedItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the likedItemId fields are not set
        if (!(object instanceof LikedItemEntity)) {
            return false;
        }
        LikedItemEntity other = (LikedItemEntity) object;
        if ((this.likedItemId == null && other.likedItemId != null) || (this.likedItemId != null && !this.likedItemId.equals(other.likedItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.LikedItemEntity[ id=" + likedItemId + " ]";
    }

    public ListingEntity getListing() {
        return listing;
    }

    public void setListing(ListingEntity listing) {
        this.listing = listing;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
    
}
