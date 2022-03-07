package com.company;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.*;

/*The Object class uses a ReadWriteLock: it consists of two separate locks
that are mutually exclusive: a Read lock that allows any number of entrants
and a Write lock that allows only one user, and only when there are no
readers. Readers can enter when there are other Readers, but not when there
is a Writer. Writers can only enter if there are no other Readers or Writers.
The ReentrantReadWriteLock implementation of interface ReadWriteLock
is used to provide synchronization for accessing the Object class*/

public class Object {
    public String buffer;
    public String name;
    public ReadWriteLock lock;
    Lock readLock;
    Lock writeLock;

    public Object(String _name){
        name = _name;
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    public void Read(User user){
        lock.readLock().lock();
        System.out.println(name + " says: " + buffer);

        int tempRand = (int)(Math.random() * 5 + 3);
        System.out.println(user.myThread.getName() + " is yielding for " + tempRand + " cycles.");

        for(int i = 0; i < tempRand; i++){
            Thread.yield();
        }

        lock.readLock().unlock();
    }

    public void Write(User user){
        lock.writeLock().lock();
        System.out.println(user.myThread.getName() + " is attempting to write to " + name + "... ");
        buffer = user.myString;

        int tempRand = (int)(Math.random() * 5 + 3);
        System.out.println(user.myThread.getName() + " is yielding for " + tempRand + " cycles.");


        for(int i = 0; i < tempRand; i++){
            Thread.yield();
        }

        System.out.println(user.myThread.getName() + "'s write was successful!");
        lock.writeLock().unlock();
    }

}
