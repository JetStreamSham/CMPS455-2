package com.OS455;

import java.util.Random;

public class Main {


    public static void main(String[] args) {
        Random random  = new Random();
        int M = random.nextInt(3,7);
        int N = random.nextInt(3,7);
        Simulation sim = new Simulation(M,N);
        //Simulation sim = new Simulation();
    }


}
