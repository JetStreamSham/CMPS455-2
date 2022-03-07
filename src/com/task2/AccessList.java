package com.task2;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Random;

public class AccessList {
    //objects
    static int M = 8;
    //domains
    static int N = 8;

    public static AccessList acl;

    LinkedList<DomainRights> accessList[];

    public AccessList(int object, int domains) {
        M = object;
        N = domains;
        InitACL();
        PrintInfo();
        PrintTable();

        acl = this;
    }

    public AccessList() {
        InitACL();
        PrintInfo();
        PrintTable();
        acl = this;
    }

    public void InitACL() {
        Random random = new Random();


        //array of linked list
        //each linked list if a column in an
        //access matrix
        accessList = new LinkedList[M + N];

        //populate list of linked list
        //with domain rights to objects
        for (int i = 0; i < M; i++) {
            LinkedList<DomainRights> linkedList = new LinkedList<DomainRights>();

            //create domain rights for every domain
            for (int j = 0; j < N; j++) {
                DomainRights rights = new DomainRights();
                //specify that this a file not a domain
                rights.object = ObjectType.FILE;
                rights.domain = j;

                //set the rights randomly
                //1/4 chance for 0:R 1:W 2:RW 3:NA
                int hasRights = random.nextInt(4);
                String permissions = "";
                switch (hasRights) {
                    case 0:
                        permissions = "R";
                        break;
                    case 1:
                        permissions = "W";
                        break;
                    case 2:
                        permissions = "RW";
                        break;
                    case 3:
                        permissions = "_";
                        break;
                }

                rights.permissions = permissions;

                linkedList.add(rights);
            }
            accessList[i] = linkedList;

        }

        //populate list of linked list
        //with domain rights to domains
        for (int i = 0; i < N; i++) {
            LinkedList<DomainRights> linkedList = new LinkedList<DomainRights>();

            //create domain rights for every domain
            for (int j = 0; j < N; j++) {

                DomainRights rights = new DomainRights();
                //specify that this a file not a domain
                rights.object = ObjectType.DOMAIN;
                rights.domain = j;
                // check if adding domain rights to same domain
                if (i != j) {
                    //set the rights randomly
                    //1/2 chance for 0:allow 1:NA
                    int hasRights = random.nextInt(2);
                    String permissions = "";
                    switch (hasRights) {
                        case 0:
                            permissions = "a";
                            break;
                        case 1:
                            permissions = "_";
                            break;
                    }


                    rights.permissions = permissions;
                } else {
                    rights.permissions = "_";
                }
                linkedList.add(rights);
            }
            accessList[i + M] = linkedList;
        }

    }

    public void PrintInfo() {
        System.out.println("Access Control Scheme: Access List");
        System.out.println("Objects:" + M);
        System.out.println("Domains:" + N);


    }

    public void PrintTable() {
        System.out.print("\t");

        for (int i = 0; i < M ; i++) {
                System.out.print("F" + i + "\t");

        }
        for(int i = 0; i < N; i++){
            System.out.print("D" + i  + "\t");

        }
        System.out.println();

        for (int i = 0; i < N; i++) {
            System.out.print("D" + i + "\t");
            for (int j = 0; j < M + N; j++) {
                LinkedList<DomainRights> domain = accessList[j];
                DomainRights dr = domain.get(i);
                System.out.print(dr.permissions + "\t");
            }
            System.out.println();

        }

    }

    public void PrintColumns() {
        for (int i = 0; i < M + N; i++) {
            System.out.println("F" + i);
            for (int j = 0; j < accessList[i].size(); j++) {
                DomainRights dr = accessList[i].get(j);
                System.out.println("D" + j + ":" + dr.permissions);
            }
        }

    }

    public LinkedList<DomainRights> GetObjectAccessList(int index) {
        return accessList[index];
    }
}
