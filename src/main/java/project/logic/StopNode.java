package project.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Clase Nodo
public class StopNode {

    private String idNodo;
    private String nombre;
    private double posX;
    private double posY;
    private List<Route> routes;

	public StopNode(String idNodo, String nombre, double posX, double posY) {
        super();
        this.idNodo = idNodo;
        this.nombre = nombre;
        this.posX = posX;
        this.posY = posY;
        this.routes = new ArrayList<>();
    }

    public void setIdNodo(String idNodo) {
        this.idNodo = idNodo;
    }


    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }


    public List<Route> getRoutes() {
        return routes;
    }


    public String getIdNodo() {
        return idNodo;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public double getPosX() {
        return posX;
    }


    public void setPosX(double posX) {
        this.posX = posX;
    }


    public double getPosY() {
        return posY;
    }


    public void setPosY(double posY) {
        this.posY = posY;
    }



}
