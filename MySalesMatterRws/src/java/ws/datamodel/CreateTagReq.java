/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.TagEntity;

/**
 *
 * @author sylvia
 */
public class CreateTagReq {
    
    private String username;
    private String password;
    private TagEntity tagEntity;
 
    public CreateTagReq() {   
        
    }
    
    public CreateTagReq(String username, String password, TagEntity tagEntity) {
        this.username = username;
        this.password = password;
        this.tagEntity = tagEntity;
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

    public TagEntity getTagEntity() {
        return tagEntity;
    }

    public void setTagEntity(TagEntity tagEntity) {
        this.tagEntity = tagEntity;
    }
    
}
