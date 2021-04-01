/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.UserEntity;

/**
 *
 * @author sylvia
 */
public class UpdateUserReq {
    
    private String username;
    private String password;
    private UserEntity userEntity;

    
    
    public UpdateUserReq() {        
    }

    public UpdateUserReq(String username, String password, UserEntity userEntity){
        this.setUsername(username);
        this.setPassword(password);
        this.setUserEntity(userEntity);
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

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
    
}
