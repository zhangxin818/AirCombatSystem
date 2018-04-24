/**
 * Created by Administrator on 2017/12/27.
 */


/**
 * @Author zhangxin
 * @Date 2017-12-27 16:24:00
 * @Desciption 蓝军基地
 **/
public class B {
    double x; //x坐标
    double y; //y坐标
    String name; //蓝军基地名称 B1 或 B2
    double xForRoute1; //导弹起点x坐标 针对R1--T1坐标系
    double yForRoute1; //导弹起点y坐标 针对R1--T1坐标系
    double xForRoute2; //导弹起点x坐标 针对R2--T1坐标系
    double yForRoute2; //导弹起点y坐标 针对R2--T1坐标系
    double xForRoute3; //导弹起点x坐标 针对R2--T2坐标系
    double yForRoute3; //导弹起点y坐标 针对R2--T2坐标系

    /**
     * @Author zhangxin
     * @Date 2017-12-27 16:24:52
     * @Desciption //蓝军基地构造函数
     **/
    B(String name){

        this.name = name;
        if(name.equals("B1")){
            x = 44;
            y = 41;
            xForRoute1 = 31.274193;
            yForRoute1 = 7.065749;
            xForRoute2 = 43.7195122;
            yForRoute2 = 6.52719311;
            xForRoute3 = 35.0;
            yForRoute3 = - 27.0;
        }
        if(name.equals("B2")){
            x = 42;
            y = 20;
            xForRoute1 = 32.6612903;
            yForRoute1 = -13.937005;
            xForRoute2 = 31.8170732;
            yForRoute2 = -10.6133807;
            xForRoute3 = 33.0;
            yForRoute3 = 6.0;
        }


    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName(){
        return  name;
    }

    public double getXforRoute1(){
        return  xForRoute1;
    }

    public double getxForRoute2(){
        return  xForRoute2;
    }

    public double getxForRoute3(){
        return xForRoute3;
    }

    public double getyForRoute1(){
        return yForRoute1;
    }

    public double getyForRoute2(){
        return yForRoute2;
    }

    public double getyForRoute3(){
        return yForRoute3;
    }
}
