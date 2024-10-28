package com.impact.pokemon;

public class Pokemon {
    private String name;
    private String type;
    private int total;
    private int hitPoints;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
    private int generation;
    private boolean legendary;

    // Constructor
    public Pokemon(String name, String type, int total, int hitPoints, int attack, int defense,
                   int specialAttack, int specialDefense, int speed, int generation, boolean legendary) {
        this.name = name;
        this.type = type;
        this.total = total;
        this.hitPoints = hitPoints;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.generation = generation;
        this.legendary = legendary;
    }

    // Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public int getHitPoints() { return hitPoints; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }

    public void reduceHitPoints(int damage) {
        this.hitPoints = Math.max(this.hitPoints - damage, 0);
    }
}
