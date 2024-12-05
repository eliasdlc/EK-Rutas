package project.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapController implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private List<Graph> graphs;
    private List<User> users;
    private static MapController myMap = null;
    private static User loginUser;
    private static final String FILE_PATH = "EK_Routes.dat"; // file path

    public MapController() {
        super();
        graphs = new ArrayList<>();
        users = new ArrayList<>();
    }

    public static MapController getInstance() {
        if(myMap == null) {
            myMap = new MapController();
        }return myMap;
    }

    public static void saveData(){
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            out.writeObject(myMap);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            myMap = (MapController) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found. Creating a new MapController instance.");
            myMap = new MapController();
        } catch (StreamCorruptedException e) {
            System.err.println("Invalid file format. Recreating the data file.");
            new File(FILE_PATH).delete();
            myMap = new MapController();
            saveData(); // Save an empty MapController instance
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public List<Graph> getGraphs() {
        return graphs;
    }

    public List<User> getUsers() { return users; }

    public void addGraph(Graph graph) {
        graphs.add(graph);
    }

    public Graph searchGraphById(String id){
        for(Graph aux : graphs) {
            if(aux.getGraphId().equals(id)) {
                return aux;
            }
        }
        return null;
    }

    public void updateGraph(Graph graph) {
        int index = graphs.indexOf(graph);
        if(index != -1) {
            graphs.set(index, graph);
        }
    }

    private boolean confirmLogin(String username, String password) {
        boolean login = false;
        for(User user : users) {
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loginUser = user;
                login = true;
            }
        }
        return login;
    }

    public User getConfirmedUser(String username, String password) {
        if(confirmLogin(username, password)) {
            return loginUser;
        }
        return null;
    }


}
