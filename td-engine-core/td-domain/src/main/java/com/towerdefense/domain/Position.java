package com.towerdefense.domain;

public record Position(double x, double y) {

    public double distanceTo(Position other) {
        double dx = other.x - x;
        double dy = other.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}