/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

/**
 *
 * @author rtan3
 */
public class UnlikeItemReq {
    
    private String username;
    private String password;
    private Long listingId;

    public UnlikeItemReq() {
    }

    public UnlikeItemReq(String username, String password, Long listingId) {
        this.username = username;
        this.password = password;
        this.listingId = listingId;
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

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }
    
    
    
}
