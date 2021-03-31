/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Yuki
 */
@Entity
public class NotificationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 4, max = 32)
    private String title;
    
    @Column(nullable = false, length = 255)
    @NotNull
    @Size(max = 255)
    private String content;
    
    @Column
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedDate;
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    
    @ManyToMany
    @JoinColumn(nullable = false)
    @NotNull
    private List<ListingEntity> listings;

    public NotificationEntity() {
        this.title = "";
        this.content = "";
        this.listings = new ArrayList<>();
    }

    public NotificationEntity(String title, String content, Date postedDate, Date expiryDate) {
        this.title = title;
        this.content = content;
        this.postedDate = postedDate;
        this.expiryDate = expiryDate;
    }
 
    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificationId != null ? notificationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the notificationId fields are not set
        if (!(object instanceof NotificationEntity)) {
            return false;
        }
        NotificationEntity other = (NotificationEntity) object;
        if ((this.notificationId == null && other.notificationId != null) || (this.notificationId != null && !this.notificationId.equals(other.notificationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.NotificationEntity[ id=" + notificationId + " ]";
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the postedDate
     */
    public Date getPostedDate() {
        return postedDate;
    }

    /**
     * @param postedDate the postedDate to set
     */
    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    /**
     * @return the expiryDate
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return the listings
     */
    public List<ListingEntity> getListings() {
        return listings;
    }

    /**
     * @param listings the listings to set
     */
    public void setListings(List<ListingEntity> listings) {
        this.listings = listings;
    }
    
}
