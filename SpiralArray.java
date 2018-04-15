/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiralarray;

import java.util.*;

/**
 *
 * @author GAURAV
 */
public class SpiralArray {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the elements(press 0 to exit");
        ArrayList<Integer> elems = new ArrayList<>();
        int e = sc.nextInt();
        //int index=0;
        while (e != 0) {
            elems.add(e);
            e = sc.nextInt();
        }
        int curr = 0;   //denotes correct position in the array
        int len = (int) Math.round(Math.sqrt((double) elems.size()));
        if(len*len>elems.size()){
            len--;
        }
        int n = len - 1;
        int[][] arr = new int[len][len];
        int iter = 0; //no. of spiral loop counts
        int k = 0;    //element index        
        try {
            while (true) {
                for (int i = curr; i <= n; i++) {
                    arr[curr][i] = elems.get(k++);
                }
                curr++;
                for (int i = curr; i <= n; i++) {
                    arr[i][n] = elems.get(k++);
                    curr = i;
                }
                for (int i = curr - 1; i >= iter; i--) {
                    arr[n][i] = elems.get(k++);
                    curr = i;
                }
                n = n - 1;
                for (int i = n; i >= curr + 1; i--) {
                    arr[i][curr] = elems.get(k++);
                }
                curr++;
                iter++;
                //n=n-1;
            }
        } catch (Exception exp) {
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    System.out.print(arr[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

}
