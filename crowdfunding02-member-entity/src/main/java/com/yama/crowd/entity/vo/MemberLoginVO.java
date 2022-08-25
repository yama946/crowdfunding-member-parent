package com.yama.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 封装登陆表单对象，并将其放入session域中
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)//链式风格，在调用set方法时，返回这个类的实例对象
public class MemberLoginVO implements Serializable {

    private static final long serialVersionUID = 2L;

    private Integer id;

    private String loginacct;

    private String userpswd;

}
