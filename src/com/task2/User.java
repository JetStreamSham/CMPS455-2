package com.task2;

import java.util.LinkedList;
import java.util.Random;

public class User extends Thread {
    //index of domain
    int domainIndex;
    int threadIndex;
    Random random;
    static AccessList acl;

    public User(int threadIndex) {
        acl = AccessList.acl;
        random = new Random();
        this.domainIndex = threadIndex;
        this.threadIndex = threadIndex;
    }


    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            //randomly select task
            //0 read
            //1 write
            //2 switch
            int task = random.nextInt(3);

            switch (task) {
                case 0:
                    Read();
                    break;
                case 1:
                    Write();
                    break;
                case 2:
                    Switch();
                    break;
            }
        }

        System.out.println(ThreadInfo() + " has ended");


    }

    String ThreadInfo() {
        return "[Thrd " + threadIndex + "(D" + domainIndex + ")]";
    }

    public static boolean GetRights(int domain, int objIndex, String op) {
        String rights = "_";

        LinkedList<DomainRights> objACL = acl.GetObjectAccessList(objIndex);
        DomainRights domainRights = objACL.get(domain);
        boolean hasRights = domainRights.permissions.contains(op);
        return hasRights;
    }


    void Read() {
        //randomly select object
        int objectIndex = random.nextInt(AccessList.M);


        try {


            //check for read permissions
            System.out.println(ThreadInfo() + " attempting to read from F" + objectIndex);
            boolean hasRights = GetRights(domainIndex, objectIndex, "R");
            if (hasRights) {

                Simulation.readSemaphores[objectIndex].acquire();
                Simulation.readerCount[objectIndex]++;
                if (Simulation.readerCount[objectIndex] == 1) {
                    Simulation.writeSemaphores[objectIndex].acquire();
                }
                Yield();
                Simulation.readSemaphores[objectIndex].release();

                String objBuffer = Simulation.objectBuffers[objectIndex];
                System.out.println(ThreadInfo() + " read from F" + objectIndex + ":" + objBuffer + " ");

                Simulation.readSemaphores[objectIndex].acquire();
                Simulation.readerCount[objectIndex]--;
                if (Simulation.readerCount[objectIndex] == 0) {
                    Simulation.writeSemaphores[objectIndex].release();

                }
                Simulation.readSemaphores[objectIndex].release();

            } else {
                System.out.println(ThreadInfo() + " does not have read rights for F" + objectIndex);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void Write() {
        //randomly select object
        int objectIndex = random.nextInt(AccessList.M);

        try {

            //check for write permissions
            boolean hasRights = GetRights(domainIndex, objectIndex, "W");
            System.out.println(ThreadInfo() + " attempting to write to F" + objectIndex);
            if (hasRights) {
                Simulation.writeSemaphores[objectIndex].acquire();
                System.out.println(ThreadInfo() + " writes to F" + objectIndex + ":" + ThreadInfo() + " wrote here last ");
                Simulation.objectBuffers[objectIndex] = ThreadInfo() + " wrote here last";
                //yield a random amount
                Yield();
                Simulation.writeSemaphores[objectIndex].release();
            } else {
                System.out.println(ThreadInfo() + "does not have write rights to F" + objectIndex);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void Yield() {
        int yieldCount = random.nextInt(3, 7);
        do {
            this.yield();
        } while (yieldCount-- > 0);
    }

    void Switch() {
        //randomly select object
        int objectIndex = -1;
        int N = AccessList.N;
        //keep cycling until a domain other than current is selected
        do {
            objectIndex = random.nextInt(AccessList.N);

        } while (objectIndex == domainIndex);
//        System.out.println(domainIndex+","+objectIndex);

        //offset index by the amount of non domain objects M
        objectIndex += AccessList.M;


        try {

            //check for switch permissions
            System.out.println(ThreadInfo() + " attempting to switch to D" + objectIndex % N);
            boolean hasRights = GetRights(domainIndex, objectIndex, "a");

            if (hasRights) {
                Simulation.readSemaphores[objectIndex].acquire();
                LinkedList<DomainRights> objACL = acl.GetObjectAccessList(objectIndex);
                DomainRights domainRights = objACL.get(domainIndex);
                //swap domains
                domainIndex = objectIndex % N;
                System.out.println(ThreadInfo() + " switched to D" + objectIndex % N);
            } else {
                System.out.println(ThreadInfo() + "does not have switch rights to D" + objectIndex % N);
            }
            //yield a random amount
            int yieldCount = random.nextInt(3, 7);
            do {
                this.yield();
            } while (yieldCount-- > 0);
            Simulation.readSemaphores[objectIndex].release();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
