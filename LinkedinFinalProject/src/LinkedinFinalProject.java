import com.sun.jdi.Value;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkedinFinalProject {
    public static void main(String[] args) {
        JsonReader.readJsonFile();
        //User.print();
        Graph graph = new Graph();
        graph.addVertices();
        Menu.mainMenu(graph);

    }
}
class JsonReader{
    static Object object;
    static {
        try {
            object = new JSONParser().parse(new FileReader("D:\\JAVA\\Term 3\\LinkedinProject\\project-final-farnazin\\users1.json"));
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }catch (ParseException exception){
            throw new RuntimeException(exception);
        }
    }
    static void readJsonFile(){
        JSONArray jsonArray = (JSONArray) object;
        Iterator<JSONObject> jsonObjectIterator = jsonArray.iterator();
        while (jsonObjectIterator.hasNext()){
            JSONObject jsonObject = jsonObjectIterator.next();
            String userID = (String) jsonObject.get("id");
            String name = (String) jsonObject.get("name");
            String dateOfBirth = (String) jsonObject.get("dateOfBirth");
            String universityLocation = (String) jsonObject.get("universityLocation");
            String field = (String) jsonObject.get("field");
            String workPlace = (String) jsonObject.get("workplace");
            String email = (String) jsonObject.get("email");
            User user = new User(userID, name, email, dateOfBirth, universityLocation, field, workPlace);
            JSONArray specialties = (JSONArray) jsonObject.get("specialties");
            JSONArray connectionsID = (JSONArray) jsonObject.get("connectionId");
            user.specialities.addAll(specialties);
            user.connectionID.addAll(connectionsID);
            User.users.add(user);
            User.connectIdToUser.put(userID, user);
        }
    }
}
abstract class Menu
{
    static int ID = 2000;
    static User loggedUser = null ;

    public static void mainMenu(Graph graph)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("           ***           WELCOME TO NEW VERSION OF LINKEDIN!           ***           ");
        System.out.println("                         1 --> LOG INTO EXISTING ACCOUNT");
        System.out.println("                         2 --> CREATE NEW ACCOUNT");
        System.out.println("                         3 --> SHOW ALL USERS");
        System.out.println("                         4 --> EXIT");
        int request = sc.nextInt();
        switch (request)
        {
            case 1:
                loginMenu(graph);
                break;
            case 2:
                createAccountMenu(graph);
                break;
            case 3:
                showAllUsers(graph);
            case 4:
                return;
            default:
                System.out.println("                         please enter the number correctly");
                mainMenu(graph);
        }
    }
    public static void loginMenu(Graph graph)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("           ***           WELCOME TO LOGIN MENU           ***           ");

        System.out.println("                         Please enter your ID");
        String ID = sc.next();
        boolean found = false;
        for (User temp : User.users)
            if (temp.userID.equals(ID))
            {
                found = true;
                loggedUser = temp;
                System.out.println("                         Logged in successfully.");
                UserCommands.userCommandsMenu(graph);
            }
        if (!found)
        {
            System.out.println("                         The ID is not correct!");
            loginMenu(graph);
        }
    }
    public static void createAccountMenu(Graph graph)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("           ***           WELCOME TO SIGN UP PANEL           ***           ");
//        System.out.println("                         Please enter your userID");

        String userID = String.valueOf(ID + 1);
        System.out.println("                         Please enter your name");
        String name = sc.next();
        System.out.println("                         Please enter your email");
        String email = sc.next();
        String emailRegex = "[A-Za-z0-9+_.-]+@(.+)$";
        Pattern patternEmail = Pattern.compile(emailRegex);
        Matcher matcher = patternEmail.matcher(email);
        if (!(matcher.matches()))
        {
            System.out.println("                         Your entered email is not valid!");
            createAccountMenu(graph);
        }
        System.out.println("                         Please enter your date of birth");
        String dateOfBirth = sc.next();
        System.out.println("                         Please enter your university location");
        String universityLocation = sc.next();
        System.out.println("                         Please enter your field");
        String field = sc.next();
        System.out.println("                         Please enter your work place");
        String workPlace = sc.next();
        System.out.println("                         How many specialities do you have?");
        String number = sc.next();
        System.out.println("                         Please enter your specialities");
        String specialities = "";
        for (int i = 0; i < Integer.parseInt(number); i++) {
            specialities += sc.next() + " ";
        }
        String[] splitSpecialities = specialities.split(" ");
        ID++;
        User newUser = new User(userID, name, email, dateOfBirth, universityLocation, field, workPlace);
        newUser.specialities.addAll(Arrays.asList(splitSpecialities));
        User.users.add(newUser);
        graph.list.add(newUser);
        User.connectIdToUser.put(userID, newUser);
        System.out.println("                         New account was created successfully.");
        System.out.println("                         Your userID: " + newUser.userID);
        Menu.loginMenu(graph);
    }
    public static void showAllUsers(Graph graph){
        System.out.println("                         All users: ");
        for (int i = 0; i < User.users.size(); i++) {
            System.out.println("                         ID: " + User.users.get(i).userID + "\n" +
                    "                         name: " + User.users.get(i).name+ "\n" +
                    "                         date of birth: " + User.users.get(i).dateOfBirth + "\n" +
                    "                         university connection: " + User.users.get(i).universityLocation + "\n" +
                    "                         field: " + User.users.get(i).field + "\n" +
                    "                         work place: " + User.users.get(i).workPlace);
            System.out.println("                         -----------------------------                         ");

        }
        mainMenu(graph);
    }
}
class UserCommands
{
    static Scanner sc = new Scanner(System.in);
    static User interactedUser = null;
    static void userCommandsMenu(Graph graph)
    {
        System.out.println("           ***           WELCOME TO USER COMMAND MENU           ***           ");
        System.out.println("                         Where menu do you want to go now?");
        System.out.println("                         1 -- > HOME");
        System.out.println("                         2 -- > ACTIVITY");
        System.out.println("                         3 -- > BACK TO MAIN MENU");
        int request = sc.nextInt();
        switch (request)
        {
            case 1:
                homeMenu(graph);
                break;
            case 2:
                activityMenu(graph);
                break;
            case 3:
                Menu.mainMenu(graph);
                break;
            default:
                System.out.println("                         Please enter the correct number!");
        }
    }
    static void homeMenu(Graph graph){
        System.out.println("           ***           WELCOME TO HOME MENU           ***           ");
        System.out.println("                         What do you want to see now?");
        System.out.println("                         1 -- > YOUR PROFILE");
        System.out.println("                         2 -- > YOUR CONNECTIONS");
        System.out.println("                         3 -- > BACK TO PREVIOUS MENU");
        int request = sc.nextInt();
        switch (request)
        {
            case 1:
                showProfile(graph);
                break;
            case 2:
                showConnections(graph);
                break;
            case 3:
                userCommandsMenu(graph);
                break;
            default:
                System.out.println("                         Please enter the correct number!");
        }
    }
    static void showProfile(Graph graph){
        System.out.println("                         Your Profile: ");
        System.out.println("                         ID: " + Menu.loggedUser.userID + "\n" +
        "                         name: " + Menu.loggedUser.name+ "\n" +
        "                         email: " + Menu.loggedUser.email+ "\n" +
        "                         date of birth: " + Menu.loggedUser.dateOfBirth + "\n" +
        "                         university connection: " + Menu.loggedUser.universityLocation + "\n" +
        "                         field: " + Menu.loggedUser.field + "\n" +
        "                         work place: " + Menu.loggedUser.workPlace + "\n" +
        "                         specialities: ");
        for (int i = 0; i < Menu.loggedUser.specialities.size(); i++) {
            System.out.print("                         " + Menu.loggedUser.specialities.get(i) + "\n" );
        }
        homeMenu(graph);
    }
    static void showConnections(Graph graph){
        System.out.println("                         Your Connections: ");
        for (int i = 0; i < Menu.loggedUser.connectedUsers.size(); i++) {
            System.out.println("                         name: " + Menu.loggedUser.connectedUsers.get(i).name
            + "\n" + "                         ID: " + Menu.loggedUser.connectedUsers.get(i).userID);
            System.out.println("                         -----------------------------                         ");
        }
        homeMenu(graph);
    }
    static void activityMenu(Graph graph){
        System.out.println("           ***           WELCOME TO ACTIVITY MENU           ***           ");
        System.out.println("                         What do you want to do now?");
        System.out.println("                         1 -- > SEARCH A USER");
        System.out.println("                         2 -- > SEE YOUR CONNECTION SUGGESTION");
        System.out.println("                         3 -- > BACK TO PREVIOUS MENU");
        int request = sc.nextInt();
        switch (request)
        {
            case 1:
                searchUser(graph);
                break;
            case 2:
                seeConnectionSuggestionHelpingMethod(graph);
                break;
            case 3:
                userCommandsMenu(graph);
                break;
            default:
                System.out.println("                         Please enter the correct number!");
        }
    }
    static void searchUser(Graph graph){
        System.out.println("                         Please enter an ID to search a user.");
        String ID = sc.next();
        boolean found = false;
        for (User temp : User.users)
            if (temp.userID.equals(ID))
            {
                found = true;
                interactedUser = temp;
                System.out.println("                         The user you've searched: \n" + "                         name: " + interactedUser.name + "\n" +
                        "                         userID: " + interactedUser.userID + "\n" +
                        "                         university location: " + interactedUser.universityLocation + "\n" +
                        "                         field: " + interactedUser.field + "\n" +
                        "                         work space: " + interactedUser.workPlace);
                System.out.println("                         specialities: ");
                for (int i = 0; i < interactedUser.specialities.size(); i++) {
                    System.out.print("                         " + interactedUser.specialities.get(i) + "\n");
                }
                System.out.println("                         -----------------------------                         ");
                System.out.println("                         Do you want to connect to them?");
                System.out.println("                         1 -- > YES");
                System.out.println("                         2 -- > NO");
                int request = sc.nextInt();
                switch (request)
                {
                    case 1:
                        Menu.loggedUser.connectionID.add(interactedUser.userID);
                        Menu.loggedUser.connectedUsers.add(interactedUser);
                        interactedUser.connectionID.add(Menu.loggedUser.userID);
                        interactedUser.connectedUsers.add(Menu.loggedUser);
                        System.out.println("                         You are successfully connected to " + interactedUser.name);
                        activityMenu(graph);
                        break;
                    case 2:
                        activityMenu(graph);
                        break;
                    default:
                        System.out.println("                         Please enter the correct number!");
                }
            }
        if (!found)
        {
            System.out.println("                         The ID you entered do not exist!");
            activityMenu(graph);
        }

    }
    static void seeConnectionSuggestionHelpingMethod(Graph graph){
        Map<User, Integer> levelOfUsers = Menu.loggedUser.printLevelsWithBFS(graph , 5);

        System.out.println("                         The standard priority is: ");
        System.out.println("                         1 --> Specialities " + "\n" +
                "                         2 --> Field " + "\n" +
                "                         3 --> University location = Work space " + "\n" +
                "                         4 --> Level of distance ");
        System.out.println("                         Do you want to change it? ");
        System.out.println("                         1 -- > YES");
        System.out.println("                         2 -- > NO");
        int request = sc.nextInt();
        switch (request)
        {
            case 1:
                System.out.println("                         Enter the priority of each element in this format: " + "\n" +
                       "                         FIRST, SECOND, THIRD, ...");
                System.out.println("                         Specialities: ");
                String specialities = sc.next();
                System.out.println("                         Field: ");
                String field = sc.next();
                System.out.println("                         University location: ");
                String universityLocation = sc.next();
                System.out.println("                         Work space: ");
                String workSpace = sc.next();
                System.out.println("                         Level of distance: ");
                String levelOfDistance = sc.next();
                Map<User, Integer> priorityRankedUsers = Menu.loggedUser.ranking(levelOfUsers, graph, true, User.Priority.valueOf(specialities), User.Priority.valueOf(field),
                        User.Priority.valueOf(universityLocation), User.Priority.valueOf(workSpace), User.Priority.valueOf(levelOfDistance));
                seeConnectionSuggestion(graph, priorityRankedUsers, levelOfUsers);
                break;
            case 2:
                Map<User, Integer> rankedUsers = Menu.loggedUser.ranking(levelOfUsers, graph, false, User.Priority.FIFTH, User.Priority.FIFTH,
                        User.Priority.FIFTH,User.Priority.FIFTH ,User.Priority.FIFTH);
                seeConnectionSuggestion(graph, rankedUsers, levelOfUsers);
                break;
            default:
                System.out.println("                         Please enter the correct number!");
        }



    }
    static void seeConnectionSuggestion(Graph graph, Map<User, Integer> rankedUsers, Map<User, Integer> levelOfUsers){
        ArrayList<User> sortedConnectionSuggestion;
        sortedConnectionSuggestion = Menu.loggedUser.sortUsers(rankedUsers);
        System.out.println("                         Your profile: \n" + "                         name: " + Menu.loggedUser.name + "\n" +
                "                         userID: " + Menu.loggedUser.userID + "\n" +
                "                         university location: " + Menu.loggedUser.universityLocation + "\n" +
                "                         field: " + Menu.loggedUser.field + "\n" +
                "                         work space: " + Menu.loggedUser.workPlace);
        System.out.println("                         specialities: ");
        for (int i = 0; i < Menu.loggedUser.specialities.size(); i++) {
            System.out.print("                         " + Menu.loggedUser.specialities.get(i) + "\n");
        }

        if (sortedConnectionSuggestion.isEmpty()){
            Map<User, Integer> rankedAllUsers = Menu.loggedUser.rankingAllUsers(graph);
            ArrayList<User> sortedAllConnectionSuggestion;
            sortedAllConnectionSuggestion = Menu.loggedUser.sortUsers(rankedAllUsers);
            for (int i = 0; i < 20; i++) {
                System.out.println("      (" + (i + 1) + ") : " + "\n" +
                        "                         name: " + sortedAllConnectionSuggestion.get(i).name + "\n" +
                        "                         userID: " + sortedAllConnectionSuggestion.get(i).userID + "\n" +
                        "                         university location: " + sortedAllConnectionSuggestion.get(i).universityLocation + "\n" +
                        "                         field: " + sortedAllConnectionSuggestion.get(i).field + "\n" +
                        "                         work space: " + sortedAllConnectionSuggestion.get(i).workPlace + "\n" +
                        "                         level: " + levelOfUsers.get(sortedAllConnectionSuggestion.get(i)));
                System.out.println("                         specialities: ");
                for (int j = 0; j < sortedAllConnectionSuggestion.get(i).specialities.size(); j++) {
                    System.out.print("                         " + sortedAllConnectionSuggestion.get(i).specialities.get(j) + "\n");
                }
                System.out.println("                         -----------------------------                         ");
            }
        }
        else if (sortedConnectionSuggestion.size() < 20){
            System.out.println("\n                         -----------------------------                         ");
            System.out.println("                         Your top 20 suggestions to connect:");
            System.out.println("                         -----------------------------                         ");
            int counter = 0;
            for (int i = 0; i < sortedConnectionSuggestion.size(); i++) {
                System.out.println("      (" + (i + 1) + ") : " + "\n" +
                        "                         name: " + sortedConnectionSuggestion.get(i).name + "\n" +
                        "                         userID: " + sortedConnectionSuggestion.get(i).userID + "\n" +
                        "                         university location: " + sortedConnectionSuggestion.get(i).universityLocation + "\n" +
                        "                         field: " + sortedConnectionSuggestion.get(i).field + "\n" +
                        "                         work space: " + sortedConnectionSuggestion.get(i).workPlace + "\n" +
                        "                         level: " + levelOfUsers.get(sortedConnectionSuggestion.get(i)));
                System.out.println("                         specialities: ");
                for (int j = 0; j < sortedConnectionSuggestion.get(i).specialities.size(); j++) {
                    System.out.print("                         " + sortedConnectionSuggestion.get(i).specialities.get(j) + "\n");
                }
                System.out.println("                         -----------------------------                         ");
                counter = i + 1;
            }
            Map<User, Integer> rankedAllUsers = Menu.loggedUser.rankingAllUsers(graph);
            ArrayList<User> sortedAllConnectionSuggestion;
            sortedAllConnectionSuggestion = Menu.loggedUser.sortUsers(rankedAllUsers);
            int difference = 20 - sortedConnectionSuggestion.size();
            for (int i = 0; i < sortedAllConnectionSuggestion.size(); i++) {
                if (difference != 0){
                    for (int j = 0; j < sortedConnectionSuggestion.size(); j++) {
                        if (!sortedAllConnectionSuggestion.get(i).equals(sortedConnectionSuggestion.get(j))){
                            System.out.println("      (" + (++counter) + ") : " + "\n" +
                                    "                         name: " + sortedAllConnectionSuggestion.get(i).name + "\n" +
                                    "                         userID: " + sortedAllConnectionSuggestion.get(i).userID + "\n" +
                                    "                         university location: " + sortedAllConnectionSuggestion.get(i).universityLocation + "\n" +
                                    "                         field: " + sortedAllConnectionSuggestion.get(i).field + "\n" +
                                    "                         work space: " + sortedAllConnectionSuggestion.get(i).workPlace);
                            System.out.println("                         specialities: ");
                            for (int k = 0; k < sortedAllConnectionSuggestion.get(i).specialities.size(); k++) {
                                System.out.print("                         " + sortedAllConnectionSuggestion.get(i).specialities.get(k) + "\n");
                            }
                            System.out.println("                         -----------------------------                         ");
                            difference--;
                        }
                    }
                }
                else
                    break;
            }
        }
        else {
            System.out.println("\n                         -----------------------------                         ");
            System.out.println("                         Your top 20 suggestions to connect:");
            System.out.println("                         -----------------------------                         ");
            for (int i = 0; i < 20; i++) {
                System.out.println("      (" + (i + 1) + ") : " + "\n" +
                        "                         name: " + sortedConnectionSuggestion.get(i).name + "\n" +
                        "                         userID: " + sortedConnectionSuggestion.get(i).userID + "\n" +
                        "                         university location: " + sortedConnectionSuggestion.get(i).universityLocation + "\n" +
                        "                         field: " + sortedConnectionSuggestion.get(i).field + "\n" +
                        "                         work space: " + sortedConnectionSuggestion.get(i).workPlace + "\n" +
                        "                         level: " + levelOfUsers.get(sortedConnectionSuggestion.get(i)));
                System.out.println("                         specialities: ");
                for (int j = 0; j < sortedConnectionSuggestion.get(i).specialities.size(); j++) {
                    System.out.print("                         " + sortedConnectionSuggestion.get(i).specialities.get(j) + "\n");
                }
                System.out.println("                         -----------------------------                         ");
            }
        }
        System.out.println("                         Do you want to connect to them?");
        System.out.println("                         1 -- > YES");
        System.out.println("                         2 -- > NO");
        int request = sc.nextInt();
        switch (request)
        {
            case 1:
                System.out.println("                         Enter the ID of the user you want to connect to.");
                String ID = sc.next();
                interactedUser = User.connectIdToUser.get(ID);
                Menu.loggedUser.connectionID.add(interactedUser.userID);
                Menu.loggedUser.connectedUsers.add(interactedUser);
                interactedUser.connectionID.add(Menu.loggedUser.userID);
                interactedUser.connectedUsers.add(Menu.loggedUser);
                System.out.println("                         You are successfully connected to " + interactedUser.name);
                activityMenu(graph);
                break;
            case 2:
                activityMenu(graph);
                break;
            default:
                System.out.println("                         Please enter the correct number!");
        }
    }

}
class User{
    enum Priority{
        FIFTH, FORTH, THIRD, SECOND, FIRST;
    }
    static Map<String,User> connectIdToUser = new HashMap<>();
    String userID;
    String name;
    String email;
    String dateOfBirth;
    String universityLocation;
    String field;
    String workPlace;

    Priority priorityOfSpecialities;
    Priority priorityOfField;
    Priority priorityOfUniversityLocation;
    Priority priorityOfWorkSpace;
    Priority priorityOfLevel;


    ArrayList<String> specialities = new ArrayList<>();
    ArrayList<String> connectionID = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();
    ArrayList<User> connectedUsers = new ArrayList<>();
    ArrayList<User> fiveLevelDistance = new ArrayList<>();

    public User(String userID, String name,String email, String dateOfBirth, String universityLocation, String field, String workPlace) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.universityLocation = universityLocation;
        this.field = field;
        this.workPlace = workPlace;
    }
    Map<User, Integer> printLevelsWithBFS(Graph graph , int endOFLevel) {
        User currentUser = this;
        Map<User, String> visited = new HashMap<>();
        Map<User, Integer> level = new HashMap<>();

        for (int i = 0; i < graph.list.size(); i++) {
            visited.put(graph.list.get(i), "false");
            level.put(graph.list.get(i), -1);
        }
        Queue<User> queue = new LinkedList<User>();
        queue.add(currentUser);
        level.put(currentUser, 0);

        visited.put(currentUser, "true");
        while (queue.size() > 0) {
            currentUser = queue.peek();
            queue.remove();
            for (int i = 0; i < currentUser.connectedUsers.size(); i++) {
                User nextUser = currentUser.connectedUsers.get(i);
                if (visited.get(nextUser).equals("false")){
                    queue.add(nextUser);
                    level.put(nextUser, level.get(currentUser) + 1);
                    if (level.get(nextUser) > 1 && level.get(nextUser) <= endOFLevel){
                        fiveLevelDistance.add(nextUser);
                    }
                    visited.put(nextUser, "true");
                }
            }
        }
        return level;
    }
    Map<User, Integer> ranking(Map<User, Integer> levelOfUsers , Graph graph, boolean havePriority, Priority priorityOfSpecialities, Priority priorityOfField,
                               Priority priorityOfUniversityLocation, Priority priorityOfWorkSpace,Priority priorityOfLevel) {
        User currentUser = this;

        Map<User, Integer> rankedUsers = new HashMap<>();

        if (!havePriority) {
            for (int i = 0; i < fiveLevelDistance.size(); i++) {
                int numberOfSameSpecialities = 0;
                for (int j = 0; j < fiveLevelDistance.get(i).specialities.size(); j++) {
                    for (int k = 0; k < currentUser.specialities.size(); k++) {
                        if (fiveLevelDistance.get(i).specialities.get(j).equals(currentUser.specialities.get(k)))
                            numberOfSameSpecialities++;
                    }
                }
                rankedUsers.put(fiveLevelDistance.get(i), numberOfSameSpecialities * 100);
                if (fiveLevelDistance.get(i).field.equals(currentUser.field)) {
                    Integer oldPoint = rankedUsers.get(fiveLevelDistance.get(i));
                    rankedUsers.put(fiveLevelDistance.get(i), oldPoint + 200);
                }
                if (fiveLevelDistance.get(i).universityLocation.equals(currentUser.universityLocation)) {
                    Integer oldPoint = rankedUsers.get(fiveLevelDistance.get(i));
                    rankedUsers.put(fiveLevelDistance.get(i), oldPoint + 100);
                }
                if (fiveLevelDistance.get(i).workPlace.equals(currentUser.workPlace)) {
                    Integer oldPoint = rankedUsers.get(fiveLevelDistance.get(i));
                    rankedUsers.put(fiveLevelDistance.get(i), oldPoint + 100);
                }
                Integer oldPoint = rankedUsers.get(fiveLevelDistance.get(i));
                rankedUsers.put(fiveLevelDistance.get(i), oldPoint + 1 / levelOfUsers.get(fiveLevelDistance.get(i)) * 100);
            }
        } else {
            for (int i = 0; i < fiveLevelDistance.size(); i++) {
                int numberOfSameSpecialities = 0;
                for (int j = 0; j < fiveLevelDistance.get(i).specialities.size(); j++) {
                    for (int k = 0; k < currentUser.specialities.size(); k++) {
                        if (fiveLevelDistance.get(i).specialities.get(j).equals(currentUser.specialities.get(k)))
                            numberOfSameSpecialities++;
                    }
                }
                rankedUsers.put(fiveLevelDistance.get(i), numberOfSameSpecialities * (priorityOfSpecialities.ordinal()+1) * 100);
                if (fiveLevelDistance.get(i).field.equals(currentUser.field)) {
                    Integer oldPoint = rankedUsers.get(fiveLevelDistance.get(i));
                    rankedUsers.put(fiveLevelDistance.get(i), oldPoint + ((priorityOfField.ordinal()+1) * 100));
                }
                if (fiveLevelDistance.get(i).universityLocation.equals(currentUser.universityLocation)) {
                    Integer oldPoint = rankedUsers.get(fiveLevelDistance.get(i));
                    rankedUsers.put(fiveLevelDistance.get(i), oldPoint + ((priorityOfUniversityLocation.ordinal()+1) * 100));
                }
                if (fiveLevelDistance.get(i).workPlace.equals(currentUser.workPlace)) {
                    Integer oldPoint = rankedUsers.get(fiveLevelDistance.get(i));
                    rankedUsers.put(fiveLevelDistance.get(i), oldPoint + ((priorityOfWorkSpace.ordinal()+1) * 100));
                }
                Integer oldPoint = rankedUsers.get(fiveLevelDistance.get(i));
                rankedUsers.put(fiveLevelDistance.get(i), oldPoint + ((priorityOfLevel.ordinal()+1) * (1 / levelOfUsers.get(fiveLevelDistance.get(i)) * 100)));
            }
        }
        return rankedUsers;
    }
    Map<User, Integer> rankingAllUsers(Graph graph){
        User currentUser = this;
        Map<User, Integer> rankedUsers = new HashMap<>();

        for (int i = 0; i < users.size(); i++) {
            int numberOfSameSpecialities = 0;
            for (int j = 0; j < users.get(i).specialities.size(); j++) {
                for (int k = 0; k < currentUser.specialities.size(); k++) {
                    if (users.get(i).specialities.get(j).equals(currentUser.specialities.get(k)))
                        numberOfSameSpecialities++;
                }
            }
            rankedUsers.put(users.get(i) , numberOfSameSpecialities * 100);
            if (users.get(i).field.equals(currentUser.field)){
                Integer oldPoint = rankedUsers.get(users.get(i));
                rankedUsers.put(users.get(i) , oldPoint + 200);
            }
            if (users.get(i).universityLocation.equals(currentUser.universityLocation)){
                Integer oldPoint = rankedUsers.get(users.get(i));
                rankedUsers.put(users.get(i) , oldPoint + 100);
            }
            if (users.get(i).workPlace.equals(currentUser.workPlace)){
                Integer oldPoint = rankedUsers.get(users.get(i));
                rankedUsers.put(users.get(i) , oldPoint + 100);
            }
        }
        return rankedUsers;
    }
    ArrayList<User> sortUsers(Map<User, Integer> rankedUsers) {
        List<Map.Entry<User, Integer>> list =
                new LinkedList<Map.Entry<User, Integer>>(rankedUsers.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<User, Integer>>() {
            public int compare(Map.Entry<User, Integer> o1,
                               Map.Entry<User, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        ArrayList<User> sortedRankedUsers = new ArrayList<>();
        for (Map.Entry<User, Integer> entry : list) {
            sortedRankedUsers.add(entry.getKey());
        }
        return sortedRankedUsers;
    }
    static void print(){
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i).userID);
            System.out.println(users.get(i).name);
            System.out.println(users.get(i).dateOfBirth);
            System.out.println(users.get(i).universityLocation);
            System.out.println(users.get(i).field);
            System.out.println(users.get(i).workPlace);
            for (int j = 0; j < users.get(i).specialities.size(); j++) {
                System.out.print(users.get(i).specialities.get(j));
            }
            System.out.println();
            for (int j = 0; j < users.get(i).connectionID.size(); j++) {
                System.out.print(users.get(i).connectionID.get(j));
            }
            System.out.println("----------------------------------------");
        }
    }
}

class Graph{
    ArrayList<User> list = new ArrayList<>();
//    ArrayList<User>
    void addVertices()
    {
        for (int i = 0; i < User.users.size(); i++) {
            list.add(User.users.get(i));
            for (int j = 0; j < User.users.get(i).connectionID.size(); j++) {
                User user = User.connectIdToUser.get(User.users.get(i).connectionID.get(j));
                User.users.get(i).connectedUsers.add(user);
            }
        }
    }
}