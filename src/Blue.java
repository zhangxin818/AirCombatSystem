/**
 * Created by Administrator on 2017/12/27.
 */

import java.util.List;
import java.util.Random;

/**
 * @Author zhangxin
 * @Date 2017-12-27 15:17:17
 * @Desciption 蓝军类
 **/

enum statusForB{//蓝军导弹状态 wait--等待发射，alter--警戒状态，fly--正在飞行，success--拦截成功，fail--拦截失败
    wait, noBomb, alter, fly, success,fail;
}

public class Blue {
    double x; //导弹x坐标
    double y; //导弹y坐标
    B b; //基地
    int target; //导弹拦截的红军机群路线 0--R1T1，1--R2T1，2--R2T2
    statusForB status; //蓝军导弹状态
    int d; //D值
    int number; //蓝军导弹数量
    double endTime; //到达时间
    double speed; //蓝军导弹速度
    int launchTime = 0; //拦截次数
    int interceptSuccess = 0; //拦截成功次数
    int needToLaunch = 0; //是否需要发射导弹，0--否，1--是
    int flag = 0; //是否拦截成功 0--未到达，1--拦截成功，2--干扰成功
    int targetPre = 0; //上一枚导弹的攻击目标



    /**
     * @Author zhangxin
     * @Date 2017-12-27 19:46:19
     * @Desciption 蓝军导弹构造函数
     **/
    Blue(B b, double speed, int number, int d){
        this.b = b;
        this.speed = speed;
        this.number = number;
        this.d = d;
        this.status = statusForB.wait;
        this.endTime = Double.MAX_VALUE;
    }

    /**
     * @Author zhangxin
     * @Date 2017-12-28 10:39:26
     * @Description 蓝军发射导弹整体活动
     * current -- 当前时间
     **/
    public void launchAction(double currentTime, double timeSlot, List<Red> reds){
        //若上一枚导弹的攻击已经产生结果，则蓝军进入警戒状态
//        if(status.equals(statusForB.fail) || status.equals(statusForB.success)){
//            status = statusForB.alter;
//        }
        //判断是否进入警戒状态
        if(status.equals(statusForB.wait)){
            for(int i = 0; i < reds.size(); i++){
                if(reds.get(i).getStatus().equals(statusForRed.flyToT)){ //若红军起飞
                    status = statusForB.alter; //蓝军进入警戒状态
                }
            }
        }
        //若当前时刻蓝军上一枚导弹已经到达红军周围D范围之内
        if(endTime > currentTime - timeSlot/100 && endTime < currentTime + timeSlot/100) {//上一导弹已经到达红军d范围内，决定是否拦截成功
            Random random = new Random();
            int i = random.nextInt(2);
            if(i == 0){  //拦截成功
                interceptSuccess(reds);
            }
            else {      //干扰成功
                disturbSuccess(reds);
            }
            switch (target){
                case 0:
                    AirWarSystem.route1 = 0;
                    break;
                case 1:
                    AirWarSystem.route2 = 0;
                    break;
                case 2:
                    AirWarSystem.route3 = 0;
                    break;
                default:
                    break;
            }
        }
       else if(endTime < currentTime + timeSlot / 100 && endTime != Double.MAX_VALUE){
            if(number == 0){
                //若蓝军基地无导弹，则返回
                status = statusForB.noBomb;
                return;
            }
        }
        //判断红军轰炸活动是否继续
        if(isRedAttacking(reds)){
            //判断当前状态是否可以发射导弹
            if(canLaunch()){
                predict(timeSlot, reds, currentTime);
                if(needToLaunch == 1){ //若判断需要发射导弹
                    launch();
                }
            }
        }
    }

    /**
     * @Author zhangxin
     * @Date 17/12/29 10:33
     * @Description 发射导弹函数
     **/
    public void launch(){
        status = statusForB.fly;
        launchTime ++;
        number --;
    }

    /**
     * @Author zhangxin
     * @Date 2017-12-27 18:55:08
     * @Desciption 预测红军机群的轨迹，决定导弹攻击目标
     * 分别计算出来自三条不同路线的红军机群的导弹到达可攻击范围内的时间，由于一个基地可对两条线路发射导弹，因此决定取计算结果中最早的时间发射导弹
     **/
    public void predict(double timeSlot, List<Red> reds, double currentTime){
        double timeTmp = Integer.MAX_VALUE;
        double time = Integer.MAX_VALUE; //最终决定
        // 发射导弹的时间
        for(int i = 0; i < reds.size(); i++){
            reds.get(i).calcuteXY(timeSlot); //计算红军机群下一时间点的坐标，注意是下一时间点，所以在主函数循环中应该先将红军机群当前的坐标计算出来
            switch (reds.get(i).getRouteNum()){
                case 0:    //预测来自R1，攻击T1的机群的导弹发射时间
                    timeTmp = predictTimeForEachRoute(timeSlot, reds, i, reds.get(i).t.xForRoute1, reds.get(i).t.yForRoute1, b.getXforRoute1(), b.getyForRoute1(), Integer.MAX_VALUE);
                    if(timeTmp < time){
                        time = timeTmp;
                    }
                    continue;
                case 1:    //预测来自R2，攻击T1的机群的导弹发射时间
                    timeTmp = predictTimeForEachRoute(timeSlot, reds, i, reds.get(i).t.xForRoute2, reds.get(i).t.yForRoute2, b.getxForRoute2(), b.getyForRoute2(), timeTmp);
                    if(timeTmp < time){
                        time = timeTmp;
                    }
                    continue;
                case 2:    //预测来自R2，攻击T2的机群的导弹发射时间
                    timeTmp = predictTimeForEachRoute(timeSlot, reds, i, reds.get(i).t.xForRoute3, reds.get(i).t.yForRoute3, b.getxForRoute3(), b.getyForRoute3(), timeTmp);
                    if(timeTmp < time){
                        time = timeTmp;
                    }
                    continue;
                default:
                    break;

            }
        }
        if(time < Integer.MAX_VALUE){ //说明需要发射导弹
            endTime = currentTime + time; //导弹真实发射结束时间
            needToLaunch = 1;
        }
        else {
            needToLaunch = 0;
        }
    }

    /**
     * @Author zhangxin
     * @Date 2017-12-28 10:08:41
     * @Desciption 计算蓝军导弹的发射时间
     * timeSlot--时间间隔
     * reds--红军机群集合
     * i--红军机群集合下标
     * xForT--针对当前坐标系军事目标T的x坐标
     * yForT--针对当前坐标系军事目标T的y坐标
     * xForB--针对当前坐标系蓝军基地的x坐标
     * yForB--针对当前坐标系蓝军基地的y坐标
     **/
    public double predictTimeForEachRoute(double timeSlot,List<Red> reds, int i, double xForT, double yForT, double xForB, double yForB, double timePre){
        if(reds.get(i).getStatus() != statusForRed.flyToT && reds.get(i).getStatus() != statusForRed.flyToR && reds.get(i).getStatus() != statusForRed.bombsuccess ){ ;
            return Integer.MAX_VALUE;
        }
        switch (i){ //若当前红军机群已经有导弹去拦截，那么判断当前红军机群的剩余数量，若为1，则
            case 0:
                if(AirWarSystem.route1 == 1){
                    if(reds.get(i).getNumber() == 1){
                        return Integer.MAX_VALUE;
                    }
                }
                break;
            case 1:
                if(AirWarSystem.route2 == 1){
                    if(reds.get(i).getNumber() == 1){
                        return Integer.MAX_VALUE;
                    }
                }
                break;
            case 2:
                if(AirWarSystem.route3 == 1){
                    if(reds.get(i).getNumber() == 1){
                        return Integer.MAX_VALUE;
                    }
                }
                break;
        }
        double time = Integer.MAX_VALUE;
        double dd = AirWarSystem.calTwoPointsLength(reds.get(i).getxTmp(), reds.get(i).getyTmp(), xForT, yForT); //红军机群当前坐标和军事目标之间的距离
        for(double timeTmp = timeSlot; dd > 1.0; timeTmp += timeSlot ){ //若dd小于1，则认为轰炸成功
//            System.out.println("begin " + dd);
//            System.out.println(reds.get(i).getxTmp() + " " + reds.get(i).getyTmp() + " " + xForB + " " + yForB);
            double length = AirWarSystem.calTwoPointsLength(reds.get(i).getxTmp(), reds.get(i).getyTmp(), xForB, yForB); //计算红军机群与蓝军基地的距离
//            System.out.println(length);
            double lengthForB = 0.0; //导弹在当前时间间隔内可以发射的距离
            if(length <= 15.0){ //若距离在蓝军的可攻击范围之内
//                System.out.println("yes!!");
                lengthForB = timeTmp * speed;
                if((lengthForB  >= length - d) && (lengthForB <= length + d)){  //若距离在红军机群的d范围之内
                    if( timeTmp < time){  //获取到结束时间
                        if(timePre != Integer.MAX_VALUE){ //前一批也可攻击时，用随机数决定攻击哪一批
                            Random random = new Random();
                            int r = random.nextInt(2);
                            if( r ==  1){ //产生随机数0或1，若0则不改变攻击目标，若1则改变攻击目标
//                                System.out.println("change");
                                time = timeTmp;
                                targetPre = target;
                                target = reds.get(i).getRouteNum();
                            }
//                            System.out.println("get time equal" + timeTmp + " " + target);
                        }
                        else
                        {
                            time = timeTmp; //导弹到达攻击目标所需时间
                            targetPre = target;
                            target = reds.get(i).getRouteNum(); //标记导弹的攻击目标 0--R1T1，1--R2T1，2--R2T2
//                            System.out.println("get time else" + timeTmp + " " + target + " " + time);
                        }
                        if(time != Integer.MAX_VALUE){
                            switch (target){
                                case 0: AirWarSystem.route1 = 1;
                                        break;
                                case 1: AirWarSystem.route2 = 1;
                                        break;
                                case 2: AirWarSystem.route3 = 1;
                                        break;
                            }
                        }
                        break;
                    }
                }else { //若距离不再红军机群的d范围之内
                    reds.get(i).calcuteXY(timeTmp); //若蓝军导弹不能在下一时间节点到达指定范围，则继续计算下下一时间节点红军机群的坐标
                    dd = AirWarSystem.calTwoPointsLength(reds.get(i).getxTmp(), reds.get(i).getyTmp(), xForT, yForT); //计算下下一时间节点红军机群和军事目标的距离
                    if(reds.get(i).getxTmp() == 0.0){
                        break;
                    }
                }
            }
            else{ //若红军机群未到蓝军导弹可攻击的范围内
                reds.get(i).calcuteXY(timeTmp); //若蓝军导弹不能在下一时间节点到达指定范围，则继续计算下下一时间节点红军机群的坐标
                dd = AirWarSystem.calTwoPointsLength(reds.get(i).getxTmp(), reds.get(i).getyTmp(), xForT, yForT); //计算下下一时间节点红军机群和军事目标的距离
                if(reds.get(i).getxTmp() == 0.0){
                    break;
                }

            }
        }
//        System.out.println("after predict");
        return time;
    }

    /**
     * @Author zhangxin
     * @Date 2017/12/29 15:44
     * @Description 拦截成功
     **/
    public void interceptSuccess(List<Red> reds){
        flag = 1;
        status = statusForB.alter;
        interceptSuccess++;
        reds.get(target).interceptSuccess(); //红军机群数量减一
    }

    public void disturbSuccess(List<Red> reds){
        flag = 2;
        reds.get(target).disturbSuccess++; //干扰成功次数加一
        status = statusForB.alter;
    }

    public void changeXY(){

    }

    /**
     * @Author zhangxin
     * @Date 2017-12-28 10:45:59
     * @Desciption 判断当前状态是否可发射导弹
     **/
    public boolean canLaunch(){
//        System.out.println(b.getName() + " " + status + " number: " + number + " endTime: " + endTime + " target: " + target);
        if(status != statusForB.fly && number != 0){
            return true;
        }
        return false;
    }


    /**
     * @Author zhangxin
     * @Date 17/12/29 10:51
     * @Description 判断红军是否继续活动
     **/
    public boolean isRedAttacking(List<Red> reds){
        for(int i = 0; i < reds.size(); i++){
            if(!reds.get(i).getStatus().equals(statusForRed.stopR) && !reds.get(i).getStatus().equals(statusForRed.intercepted)){
                return true; //若仍有红军活动则返回真
            }
        }
        return  false; //若所有红军都结束活动则返回假
    }

    public String toString(){
        String result = "";
        if(status == statusForB.alter || status == statusForB.noBomb || status == statusForB.wait || status == statusForB.fail || status == statusForB.success) {
            if (flag == 1) {//拦截成功
                result = b.getName() + "   Status: " + status + "   Speed: " + speed + "   BombLeft: " + number + "   NextTarget：NoTarget" + "    拦截第" + target + "批红军成功";
            }
            if (flag == 2) {//干扰成功
                result = b.getName() + "   Status: " + status + "   Speed: " + speed + "   BombLeft: " + number + "   NextTarget：NoTarget" + "    第" + target + "批红军干扰成功";
            }
            if (flag == 0){
                result = b.getName() + "   Status: " + status + "   Speed: " + speed + "   BombLeft: " + number + "   NextTarget：NoTarget";
            }
        }
        else {
            if (flag == 1){
                result = b.getName() + "   Status: " + status + "   Speed: " + speed + "   BombLeft: " + number + "   NextTarget：" + target  + "    拦截第" + targetPre + "批红军成功";
            }
            if (flag == 2) {
                result = b.getName() + "   Status: " + status + "   Speed: " + speed + "   BombLeft: " + number + "   NextTarget：" + target + "    第" + targetPre + "批红军干扰成功";
            }
            if (flag == 0){
                result = b.getName() + "   Status: " + status + "   Speed: " + speed + "   BombLeft: " + number + "   NextTarget：" + target;
            }
        }
        if ( needToLaunch == 1){
            result += "    发射一枚导弹攻击第" + target + "批红军";
            needToLaunch = 0;
        }
        flag = 0;
        return result;
    }




}
