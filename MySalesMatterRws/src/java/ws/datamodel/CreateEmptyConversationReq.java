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
public class CreateEmptyConversationReq {
    
    private String username;
    private String password;
    private Long offereeId;
    private Long offererId;

    public CreateEmptyConversationReq() {
    }

    public CreateEmptyConversationReq(String username, String password, Long offereeId, Long offererId) {
        this.username = username;
        this.password = password;
        this.offereeId = offereeId;
        this.offererId = offererId;
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

    public Long getOffereeId() {
        return offereeId;
    }

    public void setOffereeId(Long offereeId) {
        this.offereeId = offereeId;
    }

    public Long getOffererId() {
        return offererId;
    }

    public void setOffererId(Long offererId) {
        this.offererId = offererId;
    }
    
}
