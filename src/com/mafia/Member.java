package com.mafia;

import java.util.*;
import java.util.stream.Collectors;

public class Member {
  private static int SURVEILLANCE_THREASHOLD = 9; //TODO quitarla fuera y meterla como dependencia
  private Date startDate;
  private String name;
  private Stack<Member> boss = new Stack<>(); // si no tiene boss es porque es el GodFather
  private List<Member> subordinates = new ArrayList<>();
  private boolean inJail = false;
  private Member current = this;
  private boolean specialSurveillance;

  public Member(Date startDate, String name) {
    this.startDate = startDate;
    this.name = name;
  }

  public Date getStartDate() {
    return startDate;
  }

  public String getName() {
    return name;
  }

  public Member getCurrentBoss() {
    return boss.peek();
  }

  public List<Member> getSubordinates() {
    return subordinates;
  }

  public void setSubordinates(List<Member> subordinates) {
    this.subordinates = subordinates;
  }

  public boolean isSpecialSurveillance() {
    return specialSurveillance;
  }

  public Member addSubordinate(Member subordinate) {
    if (!this.equals(subordinate)) {
      this.subordinates.add(subordinate);
      if (subordinate.boss.search(this) == -1) {
        subordinate.gotANewBoss(this);
      }
      //subordinate.getCurrentBoss().subordinates.remove(subordinate);
    }
    evaluateSpecialSurveillance();
    return this;
  }

  public Member gotANewBoss(Member newBoss) {
    boss.push(newBoss);
    return this;
  }

  public Member getSubordinateByName(String name) {
    return this.subordinates.stream().filter(sub -> sub.getName().equals(name)).findFirst().get();
  }

  public void gotInJail() {
    this.inJail = true;
    Member newBoss =
        findTheEldest(getCurrentBoss().getSubordinates())
            .orElseGet(() -> findTheEldest(getSubordinates()).get());
    moveMySubordinatesToAnotherBoss(newBoss);
    System.out.println(
        "----------------------------------------------------------------------------");
  }

  private Optional<Member> findTheEldest(List<Member> memberList) {
    return memberList.stream()
        .filter(mem -> !mem.equals(this))
        .sorted(Comparator.comparing(Member::getStartDate))
        .findFirst();
  }

  private void moveMySubordinatesToAnotherBoss(Member newBoss) {
    this.getSubordinates().stream().forEach(sub -> newBoss.addSubordinate(sub));
    // si el mas viejo tiene el mismo nivel que el que se va, entonces no hay nuevo jefe de este
    // nuevo
    // si el nuevo jefe es alguien ascendido => su nuevo jefe será el jefe del que se va a la
    // carcel.
    if (newBoss.getCurrentBoss() != this.getCurrentBoss()) {
      //            newBoss.gotANewBoss(this.getCurrentBoss(), false);
      this.getCurrentBoss().addSubordinate(newBoss);
    }
    // actualizar como subordinado del jefe del que se va a este nuevo jefe
    this.getCurrentBoss().getSubordinates().remove(this);
    removeSubordinate(this.getSubordinates());
  }

  private void removeSubordinate(List<Member> subordinate) {
    this.subordinates.removeAll(subordinate);
    evaluateSpecialSurveillance();
  }

  private void removeSubordinate(Member subordinate) {
    this.subordinates.remove(subordinate);
    evaluateSpecialSurveillance();
  }

  public void print(int level) {
    for (int i = 1; i < level; i++) {
      System.out.print("\t");
    }
    if (!this.inJail) {
      System.out.println(this.getName());
    }
    for (Member child : subordinates) {
      child.print(level + 1);
    }
  }

  public void gotOutJail() {
    this.inJail = false;
    /* busco dentro de los que tienen el mismo jefe que tenía johnny y obtengo todos los subordinados => luego
    filtro los que en su pila de jefes tienen a johnny y los vuelvo a resignar a johnny.
    en cuanto a la pila de jefes quito jefes hasta llegar al dueño que los esta reclamando.
    agrego como subordinado del GF a johnny nuevamente
    */

    /*busco todos los subordinados que pertenecían al jefe que salio de la carcel*/
    List<Member> myFormerSubordinates =
        this.getCurrentBoss().getSubordinates().stream()
            .flatMap(x -> x.getSubordinates().stream())
            .filter(sub -> sub.boss.search(current) != -1)
            .collect(Collectors.toList());
    myFormerSubordinates.forEach((formerSub)-> {
      /*if (formerSub.getCurrentBoss().boss.search(current) != -1) { // si el jefe del antiguo subordinado era subordinado tambien antes, => debe volver a ser subordinado
        subordinates.add(formerSub.getCurrentBoss()); // lo agrego nuevmente como subordinado al recien salido jefe
        formerSub.getCurrentBoss().getCurrentBoss().subordinates.remove(formerSub.getCurrentBoss()); // elimino al subodiJefe de la lista de subordinados de SU jefe
      }*/
      formerSub.getCurrentBoss().removeSubordinate(formerSub);
      formerSub.boss.pop();
      //addSubordinate(sub);
      addSubordinate(formerSub);
    });
    getCurrentBoss().addSubordinate(current);
  }

  private void evaluateSpecialSurveillance() {
    specialSurveillance = this.subordinates.size() > SURVEILLANCE_THREASHOLD;
  }
}
