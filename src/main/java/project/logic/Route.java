package project.logic;

import java.time.LocalTime;

// Clase Arista
public class Route {

    private StopNode origin;
    private StopNode destination;
    private String name;
    private String id;
    private double distance;
    private LocalTime time;
    private double cost;

    public Route(StopNode origin, StopNode destination, String name, String id, double distance, LocalTime time, double cost) {
        super();
        this.origin = origin;
        this.destination = destination;
        this.name = name;
        this.id = id;
        this.distance = distance;
        this.time = time;
        this.cost = cost;
    }

    public StopNode getOrigin() {
        return origin;
    }

    public void setOrigin(StopNode origin) {
        this.origin = origin;
    }

    public StopNode getDestination() {
        return destination;
    }

    public void setDestination(StopNode destination) {
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

}
