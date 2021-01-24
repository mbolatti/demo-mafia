package com.mafia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    private static List<Member> organizationMembers = new ArrayList<>();

    public static void main(String[] args) {

        Member godFather = new Member(new Date(), "Michelle");
        organizationMembers.add(godFather);

        addMember(godFather, "Johnny", 1);
        addMember(godFather.getSubordinateByName("Johnny0"), "J", 10);

        Member johnny = godFather.getSubordinateByName("Johnny0");

        addMember(johnny, "Tony", 1);
        addMember(johnny.getSubordinateByName("Tony0"), "T", 5);

        addMember(godFather, "Sully", 1);
        addMember(godFather.getSubordinateByName("Sully0"), "S", 7);

        addMember(godFather, "Vitto", 1);
        addMember(godFather.getSubordinateByName("Vitto0"), "V", 9);

        /*System.out.println("----------------------------BEGIN-------------------------------");
        System.out.println("ANTES CARCEL");
        printCompleteStructure();
        System.out.println("----------------------------");
        Member johnny = godFather.getSubordinateByName("Johnny0");
        johnny.gotInJail();
        System.out.println("EN LA CARCEL");
        printCompleteStructure();
        System.out.println("----------------------------");
        johnny.gotOutJail();
        System.out.println("SALIO DE LA CARCEL");
        printCompleteStructure();
        System.out.println("------------------------------END---------------------------------");*/

        System.out.println("----------------------------BEGIN-------------------------------");
        System.out.println("ANTES CARCEL");
        printCompleteStructure();
        printUnderSurveillance();
        System.out.println("----------------------------");
        johnny = godFather.getSubordinateByName("Johnny0");
        gotToJail(johnny);

        Member vitto = godFather.getSubordinateByName("Vitto0");
        gotToJail(vitto);

        Member sully = godFather.getSubordinateByName("Sully0");
        gotToJail(sully);

        gotOutJail(johnny);
        /*System.out.println("----------------------------");
        vitto.gotOutJail();
        System.out.println("Vitto0 - SALIO DE LA CARCEL");
        printCompleteStructure();
        System.out.println("----------------------------");
        sully.gotOutJail();
        System.out.println("Sully0 - SALIO DE LA CARCEL");
        printCompleteStructure();*/
        System.out.println("------------------------------END---------------------------------");

    }

  private static void printUnderSurveillance() {
    System.out.println("----------------------------");
    System.out.println("MIEMBROS BAJO VIGILANCIA");
    organizationMembers.stream()
        .filter((member -> member.isSpecialSurveillance()))
        .forEach(member -> System.out.println(member.getName()));
  }
    private static void printCompleteStructure() {
        organizationMembers.get(0).print(0);
    }

    public static void gotToJail(Member member) {
        member.gotInJail();
        System.out.println(member.getName() + " - EN LA CARCEL");
        printCompleteStructure();
        printUnderSurveillance();
        System.out.println("----------------------------");
    }
    public static void gotOutJail(Member member) {
        member.gotOutJail();
        System.out.println(member.getName() + " - SALIO DE LA CARCEL");
        printCompleteStructure();
        printUnderSurveillance();
        System.out.println("----------------------------");
    }

    private static Member addMember(Member boss, String prefixNameSubordinate, int membersQuantity) {
        Member member = null;
        for (int i=0 ; i<membersQuantity ; i++) {
            member = new Member(new Date(), prefixNameSubordinate + i);
            organizationMembers.add(member);
            boss.addSubordinate(member);
        }
        return boss;
    }

}
