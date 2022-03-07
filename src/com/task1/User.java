package com.task1;
import java.util.concurrent.*;
import java.util.Random;

public class User implements Runnable {

    Thread myThread;
    AccessMatrix myAccess;
    Object[] listOfObjects;
    public int myDomain;
    String myString;
    //False means we will read from an object: true means we will write to it
    IOPermission ReadWriteDecision;
    int randObj;
    int requestCount = 5;
    int numCompletions = 0;

    public User(String threadname, AccessMatrix inAM, int inDomain, Object[] inList){
        myThread = new Thread(this,threadname);
        myAccess = inAM;
        myDomain = inDomain;
        myString = threadname + " was here! ";
        listOfObjects = inList;
        System.out.println(threadname + " instantiated!");
        myThread.start();
    }

    public void run(){

        //Decide what operation, and on who:
        randObj = (int)(Math.random() * myAccess.matrix[0].length);

        if(randObj < myAccess.matrix[0].length - myAccess.matrix.length){
            Random rd = new Random();
            boolean rand = rd.nextBoolean();

            if(rand){
                ReadWriteDecision = IOPermission.R;
                System.out.println(myThread.getName() + " will read from Object " + (randObj+1) + ", permission is: " + myAccess.matrix[myDomain][randObj].name());
            }
            else{
                ReadWriteDecision = IOPermission.W;
                System.out.println(myThread.getName() + " will write to Object " + (randObj+1) + ", permission is: " + myAccess.matrix[myDomain][randObj].name());

            }
        }
        else if(randObj >= myAccess.matrix[0].length - myAccess.matrix.length){
            System.out.println(myThread.getName() + " will switch to Domain " + (((myAccess.matrix.length - myAccess.matrix[0].length) + randObj) + 1) + ", permission is: " + myAccess.matrix[myDomain][randObj].name());
        }

        Thread.yield();

        //Check for permission:

        boolean hasPermission = Arbitrate(this);

        //Perform operation
        if(hasPermission){

            if(randObj < myAccess.matrix[0].length - myAccess.matrix.length){
                if(ReadWriteDecision == IOPermission.R){
                    listOfObjects[randObj].Read(this);
                }
                else if(ReadWriteDecision == IOPermission.W){
                    listOfObjects[randObj].Write(this);
                }
            }
            else if(randObj >= myAccess.matrix[0].length - myAccess.matrix.length){
                myDomain = (myAccess.matrix.length - myAccess.matrix[0].length) + randObj;
                System.out.println(myThread.getName() + " has switched to Domain " + ((myAccess.matrix.length - myAccess.matrix[0].length) + randObj + 1));
            }

        }
        Thread.yield();

        numCompletions++;
        if(numCompletions < requestCount){

        }
        //Done!
    }





    public boolean Arbitrate(User user){
        boolean isPermitted = false;

        if(randObj < myAccess.matrix[0].length - myAccess.matrix.length){
            if(myAccess.matrix[myDomain][randObj] == ReadWriteDecision || myAccess.matrix[myDomain][randObj] == IOPermission.RW){
                System.out.println(user.myThread.getName() + " IO permission granted!");
                isPermitted = true;
            }
            else{
                System.out.println(user.myThread.getName() + " IO permission denied!");
            }

        }
        else if(randObj >= myAccess.matrix[0].length - myAccess.matrix.length){
            if(myAccess.matrix[myDomain][randObj] == IOPermission.Switch){
                System.out.println(user.myThread.getName() + " switch permission granted!");
                isPermitted = true;
            }
            else{
                System.out.println(user.myThread.getName() + " switch permission denied!");
            }
        }

        return isPermitted;
    }
}
