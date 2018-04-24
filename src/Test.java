import com.sun.corba.se.impl.orb.ParserTable;

import java.util.*;


public class Test {
    int time ;


    Test(){

    }

    Test(int t){
        time = t;
    }

    public void setT(int t){
        this.time = t;
    }

    public void change( List<A> a){
        a.get(0).change();
    }

    public void ttt(List<A> a){
        change(a);
    }

    public void lala(double d){
        d = 3.0;
    }


    public static void rotateMatrix(int[][] mat,int n){
        int[][] mat_rotate = new int[n][n];
        for(int i = 0;i<n;i++){
            for(int j = 0;j<n;j++){
                mat_rotate[j][n-1-i]=mat[i][j];
            }
        }
       for(int i = 0;i<n;i++){
            for(int j = 0;j<n;j++){
               System.out.print(mat_rotate[i][j]+" ");
             }
        }
   }


    public static void moveZeroes(int[] nums) {
        int size = nums.length;
        int startIndex = 0;
// 0元素开始的位置
        int endIndex = 0;
// 0元素结束的位置
        int currentNum;
        int i= 0;
        // 第一步：找到第一个0元素开始的位置
        // 并将第一个0元素的游标赋值给startIndex&endIndex
        while(i < size){
            currentNum = nums[i];
            if (currentNum == 0) {
                startIndex = i;
                endIndex = i;
                break;
            }
            ++i;
        }
        // 如果当前数组中没有找到0元素，则推出
        if (nums[endIndex] != 0)
            return;

        // 将当前i的值加1；直接从刚才0元素位置的后一位置开始循环
        ++i;
        while (i < size) {
            currentNum = nums[i];
            if (currentNum == 0){//如果当前元素等于0，则将i值赋值给endIndex
                endIndex = i;
            } else {
                // 如果不为0
                //则将当前元素赋值给nums[startIndex]
                // 并将当前位置的元素赋值为0
                // startIndex和endIndex都加1；
                nums[startIndex] = currentNum;
                nums[i] = 0;
                ++startIndex;
                ++endIndex;
            }
            ++i;
        }
    }



    public static void main(String[] args){
        int[] arr = {0,1,2,0,3,0,0,5,6,0};
        int[][] arr1 = {{1,2,3,4}, {5,6,7,8}, {9,10,11,12},{13,14,15,16}};
        Test.rotateMatrix(arr1,4);
//        Test.moveZeroes(arr);
//        for(int i = 0; i < arr.length; i++){
//            System.out.println(arr[i]);
//        }

    }
}
