package com.task3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

class Domain extends Thread{

    Random rand = new Random();
    Semaphore[] requests;
    Semaphore[] objects;
    Semaphore[] domains;
    int id;
    String name;
    List<List<String>> capabilities;
    List<String> myCap;
    List<String> objCont;
    List<String> colors;

    public Domain(int id, String name,List<String> myCap, List<List<String>> capabilities, Semaphore[] requests, Semaphore[] objects, Semaphore[] domains, List<String> objCont, List<String> colors){
        this.id = id;
        this.myCap = myCap;
        this.capabilities = capabilities;
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
            if (x == 0) {
                int a = rand.nextInt(objects.length);
                boolean hold = false;
                try {
                    requests[id].acquire();
                    System.out.println("[Thread " + id + " (" + name + ")]    Attempting to read resource: F" + a);
                    for (int p = 0; p < myCap.size(); p++) {
                        String q = myCap.get(p);
                        if (q.contains("F" + a)) {
                            hold = true;
                        }
                    }
                    if (hold) {
                        objects[a].acquire();
                        System.out.println("[Thread " + id + " (" + name + ")]    Resource: F" + a + " contains " + objCont.get(a));
                        int yield = rand.nextInt(8) + 3;
                        for (int i = 0; i < yield; i++) {
                            Thread.yield();
                        }
                        objects[a].release();
                        int yield2 = rand.nextInt(8) + 3;
                        for (int i = 0; i < yield2; i++) {
                            Thread.yield();
                        }
                    } else {
                        System.out.println("[Thread " + id + " (" + name + ")]    Operation Failed, permission denied.");
                    }
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            if(x == 1){
                boolean hold = false;
                int b = rand.nextInt(objects.length);
                try {
                    requests[id].acquire();
                    System.out.println("[Thread " + id + " (" + name + ")]    Attempting to write resource:  F" + b);
                    for(int p = 0; p < myCap.size(); p++) {
                        String q = myCap.get(p);
                        if(q.contains("F"+b)){
                            hold = true;
                        }
                    }
                    if(hold){
                        objects[b].acquire();
                        int color = rand.nextInt(colors.size());
                        System.out.println("[Thread " + id + " (" + name + ")]    Writing " + colors.get(color) + " to resource F" + b);
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
                boolean hold = false;
                try {
                    requests[id].acquire();
                    System.out.println("[Thread " + id + " (" + name + ")]    Attempting to switch from " + name + " to D" + c);
                    for(int p = 0; p < myCap.size(); p++) {
                        String q = myCap.get(p);
                        if(q.contains("D"+c)){
                            hold = true;
                        }
                    }
                    if(hold){
                        domains[c].acquire();
                        myCap = capabilities.get(c);
                        name = "D"+c;
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

    public static void Task3() {

        System.out.println("\nAccess Control Scheme: Capability List\n");

        Random rd = new Random();
        int N = rd.nextInt(5)+3;
        int M = rd.nextInt(5)+3;
        Semaphore[] requests = new Semaphore[N];
        Semaphore[] objects = new Semaphore[M];
        Semaphore[] domains = new Semaphore[N];

        for(int i = 0; i < N; i++){
            requests[i] = new Semaphore(5);
            domains[i] = new Semaphore(1);
        }

        System.out.println("Domain Count: "+N);
        System.out.println("Object Count: "+M+"\n");

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
        List<List<String>> caps = new ArrayList<>();
        for(int i = 0; i < N; i++){
            StringBuilder list = new StringBuilder();
            for (int j = 0; j < M+ N; j++) {
                int x = rd.nextInt(2);
                String z = String.valueOf(x);
                list.append(z).append(",");
            }
            cap.add(i, list.toString());
        }

        for(int i = 0; i < N; i++){
            List<String> c = new ArrayList<>();
            String myCap = cap.get(i);
            String[]  myCapabilities = myCap.split(",");
            int h = 0;
            int total = 0;
            for(int j = 0; j < myCapabilities.length; j++){
                if(j < M){
                    if(myCapabilities[j].contains("1")){
                        String hold = "F" + j + ": R/W";
                        c.add(total,hold);
                        total++;
                    }
                }else{
                    if(myCapabilities[j].contains("1")){
                        String hold = "D" + h + ": Allowed";
                        c.add(total,hold);
                        total++;
                    }
                    h++;
                }
            }
            caps.add(i,c);
            System.out.print("D" + i + " --->  ");
            for(int k = 0; k < c.size(); k++){
                System.out.print(c.get(k) + ", ");
            }
            System.out.println();
        }

        System.out.println();

        for(int i = 0; i < N; i++){
            String name = "D" + i;
            Domain a = new Domain(i,name,caps.get(i),caps,requests,objects,domains,colors,objCont);
            a.start();
        }
    }
}
