package com.task2;

import java.util.Random;

public class Task2 {


    public static void Task2() {
        Random random  = new Random();
        int M = random.nextInt(3,7);
        int N = random.nextInt(3,7);
        Simulation sim = new Simulation(M,N);
        //Simulation sim = new Simulation();
    }


}
