/**
 * Created by Administrator on 2017/12/27.
 */

/**
 * @Author zhangxin
 * @Date 2017-12-27 16:21:22
 * @Desciption 红军机场
 **/
public class R {
    double x; //x坐标
    double y; //y坐标
    String name; //R1 或 R2

    /**
     * @Author zhangxin
     * @Date 2017-12-27 16:22:46
     * @Desciption //红军机场构造函数
     **/
    R(String name){
        this.name = name;
        if(name.equals("R1")){
            x = 12;
            y = 39;
        }
        if(name.equals("R2")){
            x = 9;
            y = 14;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

}
