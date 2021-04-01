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
public class CreateUserReq {
    private UserEntity userEntity;
    
    public CreateUserReq() {
        
    }
    
    public CreateUserReq(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
