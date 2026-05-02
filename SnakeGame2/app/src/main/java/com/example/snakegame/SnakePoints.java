package com.example.snakegame;

public class SnakePoints {

    private int positionX;
    private int positionY;

    public int getPositionX() {
        return positionX;
    }
    public int getPositionY(){
        return positionY;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public SnakePoints(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
}

