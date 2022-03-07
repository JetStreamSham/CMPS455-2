package com.task1;


public class Task1 {

    public static void Task1() {

        //Generate number of domains/objects
        int numObj = (int)(Math.random() * 5 + 3);
        int numDomains = (int)(Math.random() * 5 + 3);

        AccessMatrix myMatrix = new AccessMatrix(numObj, numDomains);
        System.out.println("This run's access matrix is: ");
        System.out.println(myMatrix.toString());

        //Generate objects
        Object[] objects = new Object[numObj];
        for(int i = 0; i < objects.length; i++){
            objects[i] = new Object("Object" + ( i + 1));
        }

        //Generate users
        User[] users = new User[numDomains];
        for(int i = 0; i < numDomains; i++){
            users[i] = new User("User" + (i + 1), myMatrix, i, objects);
        }

        //Run each user 5 times.
        for(int i = 0; i < 5; i++){
            for(User j : users){
                    j.run();
            }
        }
        System.out.println();
        System.out.println("FINAL STATE OF OBJECTS: ");
        for(Object i : objects){
            System.out.println(i.name + " says: " + i.buffer);
        }

        System.out.println("Done!");
    }
}
