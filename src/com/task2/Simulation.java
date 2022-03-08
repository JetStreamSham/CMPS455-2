package com.task2;

import java.util.concurrent.Semaphore;

public class Simulation {

    public static int READER_MAX =5;
    public static int readerCount[];
    public static Semaphore readSemaphores[];
    public static Semaphore writeSemaphores[];
    public static String objectBuffers[];
    User users[];
    AccessList acl;

    public Simulation(int M, int N) {
        readerCount = new int[M + N];
        readSemaphores = new Semaphore[M + N];
        writeSemaphores = new Semaphore[M];
        objectBuffers = new String[M];

        for (int i = 0; i < M + N; i++) {
            readSemaphores[i] = new Semaphore(READER_MAX);
        }
        for (int i = 0; i < M; i++) {
            writeSemaphores[i] = new Semaphore(1);
            objectBuffers[i] = new String("Default Value");
        }
        acl = new AccessList(M, N);

        users = new User[N];

        for (int i = 0; i < N; i++) {
            users[i] = new User(i);
            users[i].start();
        }

        for (int i = 0; i < N; i++) {
            try {
                users[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Simulation() {
        int M = AccessList.M;
        int N = AccessList.N;
        readerCount = new int[M + N];
        readSemaphores = new Semaphore[M + N];
        writeSemaphores = new Semaphore[M];
        objectBuffers = new String[M];

        for (int i = 0; i < M + N; i++) {
            readSemaphores[i] = new Semaphore(1);
        }
        for (int i = 0; i < M; i++) {
            writeSemaphores[i] = new Semaphore(1);
            objectBuffers[i] = "Default Value";
        }
        acl = new AccessList();

        users = new User[N];

        for (int i = 0; i < N; i++) {
            users[i] = new User(i);
            users[i].start();
        }

        for (int i = 0; i < N; i++) {
            try {
                users[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
