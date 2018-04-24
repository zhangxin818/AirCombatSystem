import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/12/28.
 */
public class AirWarSystem {
    List<Red> reds = new ArrayList<>();
    Blue blue1;
    Blue blue2;
    B b1;
    B b2;
    T t1;
    T t2;
    R r1;
    R r2;
    double currentTime;
    static int route1 = 0; //记录已经结束活动的红军机群批数
    static int route2 = 0;
    static int route3 = 0;


    AirWarSystem(){
        Random random = new Random();
        r1 = new R("R1");
        r2 = new R("R2");
        t1 = new T("T1");
        t2 = new T("T2");
        b1 = new B("B1");
        b2 = new B("B2");
        Red red1 = new Red(r1, t1, getAnEven(), random.nextInt(5), random.nextInt(3) + 1);
        Red red2 = new Red(r2, t1, getAnEven(), random.nextInt(5), random.nextInt(3) + 1);
        Red red3 = new Red(r2, t2, getAnEven(), 1.0, random.nextInt(3) + 1);
        reds.add(red1);
        reds.add(red2);
        reds.add(red3);
        blue1 = new Blue(b1, random.nextInt(7) + 2, random.nextInt(12) + 3, random.nextInt(3) + 1);
        blue2 = new Blue(b2, random.nextInt(7) + 2, random.nextInt(12) + 3, random.nextInt(3) + 1);
        currentTime = 0.0;
    }

    /**
     * @Author zhangxin
     * @Date 2017/12/31 16:37
     * @Description 返回系统当前所有变量的状态
     **/
    public String getStatus(String flag){
        String status = "current time: " + currentTime + System.getProperty("line.separator");
        for(int i = 0; i < reds.size(); i++){
            status += reds.get(i).toString() + System.getProperty("line.separator");
        }
        if(flag.equals("WithBlue")){
            status += blue1.toString() + System.getProperty("line.separator");
            status += blue2.toString() + System.getProperty("line.separator");
        }
        status += System.getProperty("line.separator");
        return  status;
    }

    /**
     * @Author zhangxin
     * @Date 2017/12/31 16:37
     * @Description  将当前系统状态写入文件
     **/
    public void writeStatusToFile(String fileName, String content){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir") + System.getProperty("file.separator") + fileName, true)));
            bw.write(content);
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @Author zhangxin
     * @Date 2017-12-28 10:57:38
     * @Desciption 无蓝军攻击
     **/
    public void actionWithoutBlue(){
       double timeSlot = 1.0;
       for(double time = 0.0; !isActionDone(reds); time += 1.0, currentTime +=1.0){
            for(int redIndex = 0; redIndex < reds.size(); redIndex++){
                reds.get(redIndex).changeXY(timeSlot, currentTime);
            }
            System.out.println(getStatus("WithOutBlue"));
            writeStatusToFile("resultWithOutBlue.txt", getStatus("WithOutBlue"));
       }

    }


    /**
     * @Author zhangxin
     * @Date 17/12/29 10:13
     * @Description 有蓝军攻击
     **/
    public void actionWithBlue(){
        double timeSlot = 1.0;
        for(double time = 0.0; !isActionDone(reds); time += 1.0, currentTime += 1.0){
            for(int redIndex = 0; redIndex < reds.size(); redIndex++){
                reds.get(redIndex).changeXY(timeSlot, currentTime);
            }
            //蓝军活动
            blue1.launchAction(currentTime, timeSlot, reds);
            blue2.launchAction(currentTime, timeSlot, reds);
            System.out.println(getStatus("WithBlue"));
            writeStatusToFile("resultWithBlue.txt", getStatus("WithBlue"));
        }
    }



    /**
     * @Author zhangxin
     * @Date 2017/12/31 16:51
     * @Description 返回一个2至8之间的偶数
     **/
    public int getAnEven(){
        Random random = new Random();
        int result;
        do{
            result = random.nextInt(7) + 2;
        }while (result % 2 != 0);
        return  result;
    }


    /**
     * @Author zhangxin
     * @Date 2017/12/28 20:08
     * @Description 判断轰炸活动是否结束
     **/
    public boolean isActionDone(List<Red> reds){
        for(int i = 0; i < reds.size(); i++){
            statusForRed status = reds.get(i).getStatus();
            if(status.equals(statusForRed.startR) || status.equals(statusForRed.flyToT) || status.equals(statusForRed.bombsuccess) || status.equals(statusForRed.flyToR)){
                return false;
            }
        }
        return true;
    }

    /**
     * @Author zhangxin
     * @Date 2017-12-27 21:30:56
     * @Desciption 计算两点间的距离
     **/
    public static double calTwoPointsLength(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }


    public static void main(String[] args){
       AirWarSystem airWarSystem = new AirWarSystem();
       airWarSystem.actionWithBlue();

    }

}
