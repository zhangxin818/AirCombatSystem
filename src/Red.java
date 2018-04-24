/**
 * Created by Administrator on 2017/12/27.
 */
/**
 * @Author zhangxin
 * @Date 2017-12-27 15:17:00
 * @Desciption 红军类
 **/


enum statusForRed{
    startR, flyToT, intercepted, bombsuccess, flyToR, stopR ;
}
enum routeForRed{ //针对红军基地，红军机群有3条路线分别为R1--T1，R2--T1，R2--T2
    R1T1, R2T1, R2T2;
}

public class Red {
    statusForRed status; //红军轰炸机状态 startR--未起飞，flyToT--飞向军事目标， bombsuccess--轰炸成功, intercepted--被拦截，flyToR--返航，stopR--返航成功
    R r; //红军基地
    T t; //军事目标
    double x; //红军x坐标
    double y; // 红军y坐标
    double startTime; //起飞时间
    int number; //红军机群数量
    int speed; //红军机群速度
    double currentTime; //当前时间
    double xTmp; //暂时存储下一时间节点红军的坐标
    double yTmp; //暂时存储下一时间节点红军的坐标
    int disturbSuccess = 0;//干扰成功次数
    int bombSuccess = 0;//轰炸成功次数

    /**
     * @Author zhangxin
     * @Date 2017-12-27 16:27:46
     * @Desciption //红军集群构造函数
     **/
    Red(R r, T t, int number, double startTime, int speed) {
        status = statusForRed.startR; //初始化为未起飞
        this.number = number;
        this.startTime = startTime;
        this.speed = speed;
        this.r = r;
        this.t = t;
        this.x = 0;
        this.y = 0;
        this.currentTime = 0.0;
    }

    /**
     * @Author zhangxin
     * @Date 2017-12-27 20:08:50
     * @Desciption 改变红军机群的x,y坐标
     **/
    public void changeXY(double timeSlot, double currentTime){

        if((startTime <= currentTime) && status == statusForRed.startR && number != 0){
            status = statusForRed.flyToT;
        }
        if(bombSuccess()){
            status = statusForRed.flyToR;
        }
        if(status.equals(statusForRed.intercepted)){
           return;
        }
        calcuteXY(timeSlot); //计算出下一时间节点的坐标
        switch (getRouteNum()){
            case 0:
                if(xTmp > t.getxForRoute1()){
                    status = statusForRed.flyToR;
                    calcuteXY(timeSlot);
                }
                break;
            case 1:
                if(xTmp > t.getxForRoute2()){
                    status = statusForRed.flyToR;
                    calcuteXY(timeSlot);
                }
                break;
            case 2:
                if(xTmp > t.getxForRoute3()){
                    status = statusForRed.flyToR;
                    calcuteXY(timeSlot);
                }
                break;
        }
        x = xTmp;
        y = yTmp;
        if((x <= 0.0) && (status != statusForRed.startR)){
            status = statusForRed.stopR;
        }
        if(bombSuccess()){
            status = statusForRed.bombsuccess;
        }
//        System.out.println(r.name + " " + status + " " + number + "x: " + x + " y: " + y);
//        System.out.println(x + "  " + y);
    }

    /**
     * @Author zhangxin
     * @Date 2017-12-27 17:23:06
     * @Desciption //计算红军机群这一时刻的x,y坐标，根据基地和攻击点的不同公分为6中情况。分别为
     * R1--T1 攻击
     * R1--T1 返航
     * R2--T1 攻击
     * R2--T1 返航
     * R2--T2 攻击
     * R2--T3  返航
     **/
    public void calcuteXY(double timeSlot){
        if(status == statusForRed.flyToT){ //攻击方向前进
           xTmp = x + timeSlot * speed;
        }
        else if(status == statusForRed.flyToR) { //返航方向
            xTmp = x - timeSlot * speed;
            if(xTmp < 0.0){
                xTmp = 0.0;
            }
        }
        if(t.getName().equals("T1") && r.getName().equals("R1")){//基地为R1，攻击T1
            if(status == statusForRed.flyToT){ //攻击方向
                yTmp = Math.sqrt((1 - Math.pow((xTmp - 15.5), 2) / Math.pow(15.5, 2)) * 25);
            }
            else if(status == statusForRed.flyToR){ //返航方向
                yTmp = - Math.sqrt((1 - Math.pow((xTmp - 15.5), 2) / Math.pow(15.5, 2)) * 25);
            }
        }
        if(t.getName().equals("T1") && r.getName().equals("R2")){//基地为R2，攻击T1
            if(status == statusForRed.flyToT) { //攻击方向
                yTmp = Math.sqrt((1 - Math.pow((xTmp - 20.5), 2) / Math.pow(20.5, 2)) * 25);
            }
            else if(status == statusForRed.flyToR){ //返航方向
                yTmp = - Math.sqrt((1 - Math.pow((xTmp - 20.5), 2) / Math.pow(20.5, 2)) * 25);
            }
        }
        if(t.getName().equals("T2") && r.getName().equals("R2")){
            if(status == statusForRed.flyToT){ //攻击方向
                yTmp = Math.sqrt((1 - Math.pow((xTmp - 14.5), 2) / Math.pow(14.5, 2)) * 25);
            }
            else if(status == statusForRed.flyToR){
                yTmp = -Math.sqrt((1 - Math.pow((xTmp - 14.5), 2) / Math.pow(14.5, 2)) * 25);
            }
        }
        if(yTmp == -0.0){
            yTmp = 0.0;
        }

//        System.out.println(xTmp);
    }

    public boolean bombSuccess(){
        double length = 0.0;
        switch (getRouteNum()){
            case 0:
                length = AirWarSystem.calTwoPointsLength(x, y, t.getxForRoute1(), t.getyForRoute1());
                break;
            case 1:
                length = AirWarSystem.calTwoPointsLength(x, y, t.getxForRoute2(), t.getyForRoute2());
                break;
            case 2:
                length = AirWarSystem.calTwoPointsLength(x, y, t.getxForRoute3(), t.getyForRoute3());
                break;
        }
        if(length <= 1.0){
            bombSuccess++;
            return true;
        }
        return false;

    }


    /**
     * @Author zhangxin
     * @Date 2017-12-27 21:17:30
     * @Desciption 返回路线的编号 0,1,2
     **/
    public int getRouteNum(){
        String route = getBase() + getTarget();
        for(routeForRed r: routeForRed.values()){
            if(route.equals(r.toString()))
                return r.ordinal();
        }
        return 4;
    }

    /**
     * @Author zhangxin
     * @Date 2017/12/29 16:32
     * @Description 红军被拦截
     **/
    public void interceptSuccess(){
        number--;
        if(number == 0){
            status = statusForRed.intercepted; //若红军数量为0，则该批红军被拦截成功
        }
    }

    /**
     * @Author zhangxin
     * @Date 17/12/30 16:38
     * @Description 返回红军机群的飞机数
     **/
    public int getNumber(){
        return number;
    }

    public String toString(){
        if(status.equals(statusForRed.intercepted)){
            return  "Base: " + r.getName() + "   Target: " + t.getName() + "   Status: " + status + "  numberLeft: " + number;
        }
        return  "Base: " + r.getName() + "   Target: " + t.getName() + "   Status: " + status + "   Speed: " + speed + "   x: "+ x + "   y: " +y +"  numberLeft: " + number;
    }

    /**
     * @Author zhangxin
     * @Date 2017-12-27 21:05:59
     * @Desciption 返回红军机群的目标
     **/
    public String getTarget(){
        return t.getName();
    }

    /**
     * @Author zhangxin
     * @Date 2017-12-27 21:06:18
     * @Desciption 返回红军基地
     **/
    public String getBase(){
        return r.getName();
    }

    public double getStartTime(){
        return startTime;
    }

    public statusForRed getStatus(){
        return status;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }


    public double getxTmp(){
        return xTmp;
    }

    public double getyTmp(){
        return yTmp;
    }


}
