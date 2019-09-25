package cn.mxsic.easyfile;

import java.util.HashMap;
import java.util.Map;

import cn.mxsic.easyfile.annotation.Cols;
import cn.mxsic.easyfile.annotation.Title;
import cn.mxsic.easyfile.annotation.Transient;
import lombok.Data;

/**
 * Function: Customer <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-09-25 15:43:00
 */
@Data
public class Customer {

    @Transient
    private Integer index;

    @Cols(1)
    @Title("名称")
    private String name;

    @Title("账号")
    @Cols(2)
    private String code;

    @Title("手机")
    @Cols(3)
    private String mobilePhone;

    @Cols(4)
    @Title("邮箱")
    private String email;

    @Transient
    private Integer status;

    @Transient
    private Map<String,Object> errorMsg = new HashMap<>();

}
