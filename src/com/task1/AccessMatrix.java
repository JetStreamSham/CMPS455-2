package com.task1;
import java.util.Random;
import java.util.concurrent.*;

enum IOPermission{
    N,
    R,
    W,
    RW,
    Switch,
    NS,
    __,
}

public class AccessMatrix {

    public IOPermission[][] matrix;

    public AccessMatrix(int numObj, int numDomains){
        matrix = new IOPermission[numDomains][numObj + numDomains];

        for(int i = 0; i < numDomains; i++){

            for(int j = 0; j < numObj; j++){
                //randomly generate a permission for matrix[i][j] from
                //None, Read, Write, ReadWrite
                int rand = (int)(Math.random() * 4);
                IOPermission inPerm = IOPermission.values()[rand];
                matrix[i][j] = inPerm;
            }

            for(int k = numObj; k < numObj + numDomains ; k++){
                IOPermission inPerm;
                Random rd = new Random();
                boolean rand = rd.nextBoolean();
                if(i == k - numObj){
                    inPerm = IOPermission.__;
                }
                else if(!rand){
                    inPerm = IOPermission.NS;
                }
                else{
                    inPerm = IOPermission.Switch;
                }

                matrix[i][k] = inPerm;

            }
        }
    }

    public String toString(){
        String outString = "";
        for(int i = 0; i < (matrix[0].length - matrix.length); i++){
            if(i == 0){
                System.out.printf("%-7s", "");
            }
            System.out.printf("%-7s", "F" + (i + 1));
        }
        for(int i = 0; i < matrix.length; i++){
            System.out.printf("%-7s", "D" + (i + 1));
        }

        System.out.println();

        for(int i = 0; i < matrix.length; i++){
            System.out.printf("%-7s", "D" + (i + 1) + ": ");
            for(int j = 0; j < matrix[0].length; j++){
                System.out.printf("%-7s", matrix[i][j].name());
            }
            System.out.println();
        }
        return outString;
    }
}
