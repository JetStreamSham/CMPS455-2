package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

class Domain extends Thread{

    Random rand = new Random();
    Semaphore mutex = new Semaphore(1);
    Semaphore[] requests;
    Semaphore[] objects;
    Semaphore[] domains;
    int id;
    String name;
    List<String> capability;
    List<String> objCont;
    List<String> colors;

    public Domain(int id, String name, List<String> capabilities, Semaphore[] requests, Semaphore[] objects, Semaphore[] domains, List<String> objCont, List<String> colors){
        this.id = id;
        this.capability = capabilities;
        this.requests = requests;
        this.objects = objects;
        this.domains = domains;
        this.name = name;
        this.objCont = objCont;
        this.colors = colors;
    }

    public void run(){
        while(requests[id].availablePermits() > 0) {
            int x = rand.nextInt(3);
            if(x == 0){
                int a  = rand.nextInt(objects.length);
                try {
                    List<String> nameContain = Arrays.asList(name.split(" "));
                    List<String> myCapabilities = Arrays.asList(capability.get(Integer.parseInt(nameContain.get(1))).split(","));
                    //System.out.println("I am thread #" + id + " and I have this list of capabilities " + capability.get(Integer.parseInt(nameContain.get(1))) + " and I have " + requests[id].availablePermits() + " requests left.     THIS IS AN ATTEMPT AT READING");
                    requests[id].acquire();
                    String target = myCapabilities.get(a);
                    int targetObj = Integer.parseInt(target);
                    System.out.println("[Thread " + id + " (" + name + ")]    Attempting to read resource:  F" + (a+1));
                    if(targetObj == 1){
                        objects[a].acquire();
                        System.out.println("[Thread " + id + " (" + name + ")]    Resource:  F" + (a+1) + " contains " + objCont.get(a));
                        int yield = rand.nextInt(8) + 3;
                        for(int i = 0; i < yield; i++ ){
                            Thread.yield();
                        }
                        objects[a].release();
                        int yield2 = rand.nextInt(8) + 3;
                        for(int i = 0; i < yield2; i++ ){
                            Thread.yield();
                        }
                    }else{
                        System.out.println("[Thread " + id + " (" + name + ")]    Operation Failed, permission denied.");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(x == 1){
                int b = rand.nextInt(objects.length);
                try {
                    List<String> nameContain = Arrays.asList(name.split(" "));
                    List<String> myCapabilities = Arrays.asList(capability.get(Integer.parseInt(nameContain.get(1))).split(","));
                    //System.out.println("I am thread #" + id + " and I have this list of capabilities " + capability.get(Integer.parseInt(nameContain.get(1))) + " and I have " + requests[id].availablePermits() + " requests left.     THIS IS AN ATTEMPT AT WRITING");
                    requests[id].acquire();
                    String target = myCapabilities.get(b);
                    int targetObj = Integer.parseInt(target);
                    System.out.println("[Thread " + id + " (" + name + ")]    Attempting to write resource:  F" + (b+1));
                    if(targetObj == 1){
                        objects[b].acquire();
                        int color = rand.nextInt(colors.size());
                        System.out.println("[Thread " + id + " (" + name + ")]    Writing " + colors.get(color) + " to resource F" + (b+1));
                        objCont.set(b, colors.get(color));
                        int yield = rand.nextInt(8) + 3;
                        for(int i = 0; i < yield; i++ ){
                            Thread.yield();
                        }
                        objects[b].release();
                        int yield2 = rand.nextInt(8) + 3;
                        for(int i = 0; i < yield2; i++ ){
                            Thread.yield();
                        }
                    }else{
                        System.out.println("[Thread " + id + " (" + name + ")]    Operation Failed, permission denied.");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(x == 2){
                int c = rand.nextInt(domains.length);
                int d = c + objects.length;
                try {
                    List<String> nameContain = Arrays.asList(name.split(" "));
                    List<String> myCapabilities = Arrays.asList(capability.get(Integer.parseInt(nameContain.get(1))).split(","));
                    //System.out.println("I am thread #" + id + " and I have this list of capabilities " + capability.get(Integer.parseInt(nameContain.get(1))) + " and I have " + requests[id].availablePermits() + " requests left.     THIS IS AN ATTEMPT AT DOMAIN SWITCHING");
                    requests[id].acquire();
                    while(Integer.parseInt(nameContain.get(1)) == c){
                        c = rand.nextInt(domains.length);
                        d = c + objects.length;
                    }
                    String target = myCapabilities.get(d);
                    int targetObj = Integer.parseInt(target);
                    System.out.println("[Thread " + id + " (" + name + ")]    Attempting to switch from " + name + " to D " + c);
                    if(targetObj == 1){
                        domains[c].acquire();
                        name = "D " + c;
                        System.out.println("[Thread " + id + " (" + name + ")]    Switched to " + name);
                        int yield = rand.nextInt(8) + 3;
                        for(int i = 0; i < yield; i++ ){
                            Thread.yield();
                        }
                        domains[c].release();
                        int yield2 = rand.nextInt(8) + 3;
                        for(int i = 0; i < yield2; i++ ){
                            Thread.yield();
                        }
                    }else{
                        System.out.println("[Thread " + id + " (" + name + ")]    Operation Failed, permission denied.");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


public class CapabilityList {

    public static void main(String[] args) {

        Random rd = new Random();
        int N = 7;
        int M = 3;
        Semaphore[] requests = new Semaphore[N];
        Semaphore[] objects = new Semaphore[M];
        Semaphore[] domains = new Semaphore[N];

        for(int i = 0; i < N; i++){
            requests[i] = new Semaphore(10);
            domains[i] = new Semaphore(1);
        }

        List<String> colors = new ArrayList<>();
        colors.add(0, "Red");
        colors.add(1, "Blue");
        colors.add(2, "Green");
        colors.add(3, "Orange");
        colors.add(4, "Yellow");
        colors.add(5, "Purple");
        colors.add(6, "Brown");

        List<String> objCont = new ArrayList<>();
        for(int i = 0; i < M; i++){
            int a = rd.nextInt(7);
            String b = colors.get(a);
            objCont.add(i,b);
            objects[i] = new Semaphore(1);
        }

        List<String> cap = new ArrayList<>();
        for(int i = 0; i < N; i++){
            StringBuilder list = new StringBuilder();
            for (int j = 0; j < M+ N; j++) {
                int x = rd.nextInt(2);
                String z = String.valueOf(x);
                list.append(z).append(",");
            }
            cap.add(i, list.toString());
            System.out.println(cap.get(i));
        }

        for(int i = 0; i < N; i++){
            String name = "D " + i;
            Domain a = new Domain(i,name,cap,requests,objects,domains,colors,objCont);
            a.start();
        }
    }
}
