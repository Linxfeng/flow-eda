package com.flow.eda.oauth2.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/** 用户 */
@Data
@NoArgsConstructor
public class OauthUser implements Serializable {
    private static final long serialVersionUID = 5822365438252241772L;
    private String username;
    private String password;
    private String clientId;
    private String authorities;
    private String phone;
    private String email;
    private String registerIp;
    private Boolean status;
    private Date createDate;
    private Date updateDate;

    public OauthUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.status = true;
        this.createDate = new Date();
        this.updateDate = this.createDate;
    }
}
