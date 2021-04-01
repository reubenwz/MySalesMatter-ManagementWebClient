/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import java.util.List;

/**
 *
 * @author rtan3
 */
public class FilterByTagsReq {
    
    private String username;
    private String password;
    private String condition;
    private List<Long> tagIds;

    public FilterByTagsReq() {
    }

    public FilterByTagsReq(String username, String password, String condition, List<Long> tagIds) {
        this.username = username;
        this.password = password;
        this.condition = condition;
        this.tagIds = tagIds;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
    
}
