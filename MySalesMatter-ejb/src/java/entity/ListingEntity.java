/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author sylvia
 */
@Entity
public class ListingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listingId;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String name;

    @Column(nullable = false, length = 100)
    @NotNull
    @Size(min = 1, max = 100)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date dateListed;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String brand;

    // No need not null since can be just for rental
    @DecimalMin("0.00")
    private BigDecimal rentalPrice;

    // No need not null since can be just for rental
    @DecimalMin("0.00")
    private BigDecimal salePrice;

    private int likes;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String location;

    @NotNull
    @Column(nullable = false)
    private boolean rentalAvailability;

    @NotNull
    @Column(nullable = false)
    private boolean forSaleAvailability;
    
    @NotNull
    @Column(nullable = false)
    private boolean isRentOut;

//    @NotNull
//    @Column(nullable = false)
//    private List<Date> reservedDates;

//    @NotNull
//    @Column(nullable = false)
//    private List<String> picturePaths;
    @NotNull
    @Column(nullable = false)
    private String picturePath;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private CategoryEntity category;

    @ManyToMany(mappedBy = "listings", fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private List<TagEntity> tags;

    @OneToMany(mappedBy = "listing", fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private List<ReviewEntity> reviews;

//    @OneToMany(mappedBy = "listing", fetch = FetchType.LAZY)
//    @JoinColumn(nullable = false)
//    private List<RentalReservationEntity> reservations;

    @OneToMany(mappedBy = "listing", fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private List<OfferEntity> offers;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private UserEntity user;
    
//    @ManyToMany(mappedBy = "listings", fetch = FetchType.LAZY)
//    @JoinColumn(nullable = false)
//    private List<NotificationEntity> notifications;

    public ListingEntity() {
        this.name = "";
        //this.reservedDates = new ArrayList<>();
        //this.reservations = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.tags = new ArrayList<>();
        //this.picturePaths = new ArrayList<>();
        this.picturePath = "";
        this.rentalAvailability = true;
        this.forSaleAvailability = true;
        this.isRentOut = false;
        this.dateListed = new Date();
        this.likes = 0;
        this.category = null;
    }

    public ListingEntity(String description, Date dateListed, String brand, BigDecimal rentalPrice, BigDecimal salePrice, String location, String picturePath) {
        this();
        this.description = description;
        this.dateListed = dateListed;
        this.brand = brand;
        this.rentalPrice = rentalPrice;
        this.salePrice = salePrice;
        this.location = location;
        //this.picturePaths = picturePaths;
        this.picturePath = picturePath;
    }

    public ListingEntity(String description, Date dateListed, String brand, BigDecimal rentalPrice, BigDecimal salePrice, int likes, String location, String picturePath, List<Date> reservedDates) {
        this.description = description;
        this.dateListed = dateListed;
        this.brand = brand;
        this.rentalPrice = rentalPrice;
        this.salePrice = salePrice;
        this.likes = likes;
        this.location = location;
        //this.picturePaths = picturePaths;
        this.picturePath = picturePath;
        //this.reservedDates = reservedDates;
    }

    public ListingEntity(String name, String description, String brand, BigDecimal rentalPrice, BigDecimal salePrice, String location, String picturePath) {
        this.name = name;
        this.description = description;
        this.dateListed = new Date();
        this.brand = brand;
        this.rentalPrice = rentalPrice;
        this.salePrice = salePrice;
        this.likes = 0;
        this.location = location;
        //this.picturePaths = picturePaths;
        this.picturePath = picturePath;
        //this.reservedDates = new ArrayList<>();
    }

    public void addTag(TagEntity tagEntity) {
        if (tagEntity != null) {
            if (!this.tags.contains(tagEntity)) {
                this.tags.add(tagEntity);

                if (!tagEntity.getListings().contains(this)) {
                    tagEntity.getListings().add(this);
                }
            }
        }
    }

    public void removeTag(TagEntity tagEntity) {
        if (tagEntity != null) {
            if (this.tags.contains(tagEntity)) {
                this.tags.remove(tagEntity);

                if (tagEntity.getListings().contains(this)) {
                    tagEntity.getListings().remove(this);
                }
            }
        }
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (listingId != null ? listingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the listingId fields are not set
        if (!(object instanceof ListingEntity)) {
            return false;
        }
        ListingEntity other = (ListingEntity) object;
        if ((this.listingId == null && other.listingId != null) || (this.listingId != null && !this.listingId.equals(other.listingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ListingEntity[ id=" + listingId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateListed() {
        return dateListed;
    }

    public void setDateListed(Date dateListed) {
        this.dateListed = dateListed;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isRentalAvailability() {
        return rentalAvailability;
    }

    public void setRentalAvailability(boolean rentalAvailability) {
        this.rentalAvailability = rentalAvailability;
    }

    public boolean isForSaleAvailability() {
        return forSaleAvailability;
    }

    public void setForSaleAvailability(boolean forSaleAvailability) {
        this.forSaleAvailability = forSaleAvailability;
    }
    
    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePaths) {
        this.picturePath = picturePaths;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    public CategoryEntity getCategoryEntity() {
        return category;
    }

    public void setCategoryEntity(CategoryEntity category) {
        if (this.category != null) {
            if (this.category.getListings().contains(this)) {
                this.category.getListings().remove(this);
            }
        }

        this.category = category;

        if (this.category != null) {
            if (!this.category.getListings().contains(this)) {
                this.category.getListings().add(this);
            }
        }
    }

    public List<OfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferEntity> offers) {
        this.offers = offers;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * @return the isRentOut
     */
    public boolean isIsRentOut() {
        return isRentOut;
    }

    /**
     * @param isRentOut the isRentOut to set
     */
    public void setIsRentOut(boolean isRentOut) {
        this.isRentOut = isRentOut;
    }
}
