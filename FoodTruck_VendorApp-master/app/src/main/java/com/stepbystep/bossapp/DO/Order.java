package com.stepbystep.bossapp.DO;

public class Order {

    private String food_name;
    private String food_cost;
    private int food_number;

    public Order() {
    }

    public Order(String food_name, String food_cost, int food_number) {
        this.food_name = food_name;
        this.food_cost = food_cost;
        this.food_number = food_number;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_cost() {
        return food_cost;
    }

    public void setFood_cost(String food_cost) {
        this.food_cost = food_cost;
    }

    public int getFood_number() {
        return food_number;
    }

    public void setFood_number(int food_number) {
        this.food_number = food_number;
    }

    @Override
    public String toString() {
        return "Order{" +
                "food_name='" + food_name + '\'' +
                ", food_cost='" + food_cost + '\'' +
                ", food_number=" + food_number +
                '}';
    }
}
