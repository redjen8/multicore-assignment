package proj1.problem2;

import java.util.*;

class MatrixThread extends Thread {

    private int thread_no = 0;
    private int thread_size = 0;
    static int[][] a;
    static int[][] b;
    static int[][] result;

    MatrixThread(int num, int size) {
        this.thread_no = num;
        this.thread_size = size;
    }

    @Override
    public void run () {
        int m = a.length;
        int n = a[0].length;
        int p = b[0].length;
        int t = thread_no;
        while (t < m*p) {
            int eachCellResult = 0;
            for (int i = 0; i<n; i++) {
                eachCellResult += a[t/m][i] * b[i][t%p];
            }
            result[t/m][t%p] = eachCellResult;
            t += thread_size;
        }
    }

}
// command-line execution example) java MatmultD 6 < mat500.txt
// 6 means the number of threads to use
// < mat500.txt means the file that contains two matrices is given as standard input
//
// In eclipse, set the argument value and file input by using the menu [Run]->[Run Configurations]->{[Arguments], [Common->Input File]}.

// Original JAVA source code: http://stackoverflow.com/questions/21547462/how-to-multiply-2-dimensional-arrays-matrix-multiplication
public class MatMultD
{
    private static Scanner sc = new Scanner(System.in);
    public static void main(String [] args)
    {
        int thread_no=0;
        if (args.length==1) thread_no = Integer.valueOf(args[0]);
        else thread_no = 1;
            
        int a[][]=readMatrix();
        int b[][]=readMatrix();

        long startTime = System.currentTimeMillis();
        int[][] c=multMatrix(a,b, thread_no);
        long endTime = System.currentTimeMillis();

        //printMatrix(a);
        //printMatrix(b);    
        printMatrix(c);

        //System.out.printf("thread_no: %d\n" , thread_no);
        //System.out.printf("Calculation Time: %d ms\n" , endTime-startTime);

        System.out.printf("[thread_no]:%2d , [Time]:%4d ms\n", thread_no, endTime-startTime);
    }

   public static int[][] readMatrix() {
       int rows = sc.nextInt();
       int cols = sc.nextInt();
       int[][] result = new int[rows][cols];
       for (int i = 0; i < rows; i++) {
           for (int j = 0; j < cols; j++) {
              result[i][j] = sc.nextInt();
           }
       }
       return result;
   }

    public static void printMatrix(int[][] mat) {
        System.out.println("Matrix["+mat.length+"]["+mat[0].length+"]");
        int rows = mat.length;
        int columns = mat[0].length;
        int sum = 0;
        for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
            System.out.printf("%4d " , mat[i][j]);
            sum+=mat[i][j];
        }
        System.out.println();
        }
        System.out.println();
        System.out.println("Matrix Sum = " + sum + "\n");
    }

    public static int[][] multMatrix(int a[][], int b[][], int thread_no){//a[m][n], b[n][p]
        if(a.length == 0) return new int[0][0];
        if(a[0].length != b.length) return null; //invalid dims

        int n = a[0].length;
        int m = a.length;
        int p = b[0].length;
        int ans[][] = new int[m][p];
        
        MatrixThread.a = a;
        MatrixThread.b = b;
        MatrixThread.result = ans;
        
        MatrixThread[] threadList = new MatrixThread[thread_no];
        for (int i = 0; i<thread_no; i++) {
            threadList[i] = new MatrixThread(i, thread_no);
            threadList[i].start();
        }

        try{
            for (int i = 0; i<thread_no; i++) {
                threadList[i].join();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ans;
    }
}