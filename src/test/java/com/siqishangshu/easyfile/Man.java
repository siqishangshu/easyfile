package com.siqishangshu.easyfile;

import com.siqishangshu.easyfile.annotation.Cols;
import com.siqishangshu.easyfile.annotation.Format;
import com.siqishangshu.easyfile.annotation.Title;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Function: Man <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-08-26 11:09:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Man {

    @Cols(1)
    @Title("姓名")
    private String name;
    @Title("年龄")
//    @Cols(2)
    private int age;
    @Title("属性")
    @Format(value = ManGoodFormater.class)
    @Cols(3)
    private boolean    good;
    @Format(value = DateTimeFormater.class)
    @Cols(4)
    @Title("生日")
    private Date birthday;

//    @Transient
    @Cols(5)
    @Title("工作")
    private String job;
//    @Transient
    private String job2;


}
