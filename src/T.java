/**
 * Created by Administrator on 2017/12/27.
 */


/**
 * @Author zhangxin
 * @Date 2017-12-27 15:07:33
 * @Desciption 军事目标类
 **/

enum statusForT{
    exist, blown;
}
public class T {
    private double x;  //军事目标x轴坐标
    private double y;  //军事目标y轴坐标
    private statusForT status; //军事目标状态，exist--未被炸毁，blown--被炸毁
    double xForRoute1; //T起点x坐标 针对R1--T1坐标系
    double yForRoute1; //T起点y坐标 针对R1--T1坐标系
    double xForRoute2; //T起点x坐标 针对R2--T1坐标系
    double yForRoute2; //T起点y坐标 针对R2--T1坐标系
    double xForRoute3; //T起点x坐标 针对R2--T2坐标系
    double yForRoute3; //T起点y坐标 针对R2--T2坐标系
    String name; //T1 或 T2

    T(String name){
        this.name = name;
        if(name.equals("T1")){
            x = 43;
            y = 34;
            xForRoute1 = 31;
            yForRoute1 = 0;
            xForRoute2 = 41;
            yForRoute2 = 0;
            xForRoute3 = 0; //无用
            yForRoute3 = 0; //无用
        }
        if(name.equals("T2")){
            x = 38;
            y = 14;
            xForRoute1 = 0; //无用
            yForRoute1 = 0; //无用
            xForRoute2 = 0; //无用
            yForRoute2 = 0; //无用
            xForRoute3 = 29;
            yForRoute3 = 0;
        }
        status = statusForT.exist;
    }

    /**
     * @Author zhangxin
     * @Date 2017-12-27 15:59:45
     * @Desciption 判断军事目标是否被炸毁，若未被炸毁返回false，若被炸毁返回true
     **/
    public boolean isBlown(){
        if (status.ordinal() == 0){
            return false;
        }
        return true;
    }

    /**
     * @Author zhangxin
     * @Date 2017-12-27 16:01:40
     * @Desciption 军事目标被炸毁，改变状态
     **/
    public void blown(){
        status = statusForT.blown;
    }

    public String toString(){
        return name + ": " + status;
    }

    //返回军事目标x坐标
    public double getX() {
        return x;
    }
    //返回军事目标y坐标
    public double getY() {
        return y;
    }

    //返回军事目标状态
    public statusForT getStatus() {
        return status;
    }
    //返回军事目标的名称 T1/T2
    public String getName() {
        return name;
    }

    public double getxForRoute1(){
        return xForRoute1;
    }

    public double getyForRoute1(){
        return yForRoute1;
    }

    public double getxForRoute2(){
        return  xForRoute2;
    }

    public double getyForRoute2(){
        return  yForRoute2;
    }

    public double getxForRoute3(){
        return  xForRoute3;
    }

    public double getyForRoute3(){
        return yForRoute3;
    }

}
