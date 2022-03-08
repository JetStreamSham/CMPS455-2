package com;

import com.task3.CapabilityList;
import com.task2.Task2;
import com.task1.Task1;

//Main class was written by Cristopher Russ (C00238157)
public class Main {
    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("Incorrect commandline args");
            System.out.println("One of the following letters is required: M/L/C");
            return;
        }
        String arg1 = args[0];

        switch (arg1) {
            case "M":
                //Task 1 was written by Matthew Boudreaux (C00269064)
                Task1.Task1();
                break;
            case "L":
                //Task 2 was written by Cristopher Russ (C00238157)
                Task2.Task2();
                break;
            case "C":
                //Task 3 was written by Matthew Arroyo (C00411381)
                CapabilityList.Task3();
                break;
            default: {
                System.out.println("Incorrect commandline args");
                System.out.println("Use one of the following: M/L/C");
                break;
            }
        }
    }
}
