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
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author sylvia
 */
@Entity
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(max = 64)
    @Email
    private String email;
    
    @Column(nullable = false, length = 8)
    @NotNull
    @Size(min = 8, max = 8)
    private String phoneNumber;
    
    @Column(nullable = false, length = 10)
    @NotNull
    @Size(min = 10, max = 10)
    private String bankAccountNumber;
    
    @Column(nullable = false, length = 100)
    @NotNull
    @Size(min = 1, max = 100)
    private String bio;
    
    @Column(nullable = false, length = 32, unique = true)
    @Size(min=1, max=32)
    @NotNull
    private String username;
    
    @Column(nullable = false, length = 32)
    @Size(min=1, max=32)
    @NotNull
    private String name;
    
    @Column(nullable = false, length = 32)
    @Size(min=8, max=32)
    @NotNull
    private String password;
    
//    @OneToMany(mappedBy = "offerer", fetch = FetchType.LAZY)
//    @JoinColumn(nullable = false)
//    @NotNull
//    private List<ConversationEntity> conversationsAsOfferer;
//    
//    @OneToMany(mappedBy = "offeree", fetch = FetchType.LAZY)
//    @JoinColumn(nullable = false)
//    @NotNull
//    private List<ConversationEntity> conversationsAsOfferee;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    private List<SalesTransactionEntity> transactions;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    private List<ListingEntity> listings;
    
    @Column(nullable = true, unique = false)
    private String picturePath;
    
    
    @OneToMany(mappedBy = "user")
    @JoinColumn(nullable = false)
    @NotNull
    private List<LikedItemEntity> likedItems;
   
    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    private List<ReviewEntity> reviews;
    
    @OneToMany(mappedBy= "user", fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    private List<OfferEntity> offers;
    
    public UserEntity() {
        //conversationsAsOfferer = new ArrayList<>();
        //conversationsAsOfferee = new ArrayList<>();
        reviews = new ArrayList<>();
        likedItems = new ArrayList<>();
        transactions = new ArrayList<>();
        offers = new ArrayList<>();
        listings = new ArrayList<>();
    }

    public UserEntity(String email, String phoneNumber, String bankAccountNumber, String bio, String username, String name, String password) {
        this();
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bankAccountNumber = bankAccountNumber;
        this.bio = bio;
        this.username = username;
        this.name = name;
        this.password = password;
    } 
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserEntity)) {
            return false;
        }
        UserEntity other = (UserEntity) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GeneralUserEntity[ id=" + userId + " ]";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SalesTransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<SalesTransactionEntity> transactions) {
        this.transactions = transactions;
    }

    public List<ListingEntity> getListings() {
        return listings;
    }

    public void setListings(List<ListingEntity> listings) {
        this.listings = listings;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public List<LikedItemEntity> getLikedItems() {
        return likedItems;
    }

    public void setLikedItems(List<LikedItemEntity> likedItems) {
        this.likedItems = likedItems;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public List<OfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferEntity> offers) {
        this.offers = offers;
    }
    
}
