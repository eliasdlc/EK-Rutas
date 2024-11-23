package project.logic;

import java.time.Duration;

// Clase Arista
public class Route {

    private StopNode origin;
    private StopNode destination;
    private String name;
    private double distance;
    private Duration time;
    private double cost;
    private int trasbordo;

	public Route(StopNode origin, StopNode destination, String name, double distance, Duration time, double cost, int trasbordo) {
        super();
        this.origin = origin;
        this.destination = destination;
        this.name = name;
        this.distance = distance;
        this.time = time;
        this.cost = cost;
        this.trasbordo = trasbordo;
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

    public Duration getTime() {
        return time;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
    
    public int getTrasbordo() {
		return trasbordo;
	}

	public void setTrasbordo(int trasbordo) {
		this.trasbordo = trasbordo;
	}

}
