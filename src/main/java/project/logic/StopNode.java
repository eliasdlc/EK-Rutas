package project.logic;

// Clase Nodo
public class StopNode {

    private String idNodo;
    private String nombre;
    private double posX;
    private double posY;

	public StopNode(String idNodo, String nombre, double posX, double posY) {
        super();
        this.idNodo = idNodo;
        this.nombre = nombre;
        this.posX = posX;
        this.posY = posY;
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
