package cn.mxsic.easyfile;


import cn.mxsic.easyfile.annotation.Cols;
import cn.mxsic.easyfile.annotation.Format;
import cn.mxsic.easyfile.annotation.Title;
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
public class Body {

    @Cols(1)
    @Title("头")
    private String head;
    @Title("身高")
//    @Cols(2)
    private double high;
    @Title("状态")
    @Format(BodyStateFormater.class)
    @Cols(3)
    private boolean  state;

//    @Transient
    @Cols(5)
    @Title("脚")
    private String foot;


}
