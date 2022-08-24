package com.yama.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 主要用来封装表单数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)//链式风格，在调用set方法时，返回这个类的实例对象
public class MemberVO implements Serializable {
    private Integer id;

    private String loginacct;

    private String userpswd;

    private String username;

    private String email;

    private String phoneNum;

    private String verityCode;


    public MemberVO(Integer id,String loginacct, String userpswd, String username, String email) {
        this.id = id;
        this.loginacct = loginacct;
        this.userpswd = userpswd;
        this.username = username;
        this.email = email;
    }
}
