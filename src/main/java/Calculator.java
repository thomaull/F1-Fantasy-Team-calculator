import java.util.ArrayList;
import java.util.Scanner;

public class Calculator {
    //Lists of all available drivers and constructors
    static ArrayList<Driver> allDrivers = new ArrayList<>();
    static ArrayList<Constructor> allConstructors = new ArrayList<>();
    
    static int budget = 118800000; //MONEY TO SPEND!!!

    //User defined settings for predefined drivers and constructor
    static ArrayList<Driver> setDrivers = new ArrayList<>();
    static String setConstructor = null; 

    //Teams proposed by the program
    static ArrayList<Team> teams = new ArrayList<>();
    
    public static void main(String[] args) throws Exception {
        fillArrays();
        
        System.out.println("Welcome to the F1 Fantasy Team Calculator!");
        userInputLoop();
        long start = System.nanoTime();
        createTeams();
        long end = System.nanoTime();
        System.out.println("Generating teams took " + ((end-start)/1000000) + " ms");

        ArrayList<Team> starTeams = new ArrayList<>();
        for(Team team : teams){
            for(Driver driver : team.drivers){
                if(driver.price<18000000){
                    Team clone = cloneTeam(team);
                    clone.starDriver = driver;
                    clone.pointsAvg += driver.avgPoints;
                    starTeams.add(clone);
                }
            }
        }

        for(Team t : starTeams) teams.add(t);

        start = System.nanoTime();
        teams.sort((a, b) -> Integer.compare(b.pointsAvg, a.pointsAvg));
        end = System.nanoTime();
        System.out.println("Sorting teams took " + ((end-start)/1000000) + " ms");

        for(int i = 0; i < 5; i++){
            Team team = teams.get(i);
            System.out.println("\nTeam " + (i+1) + ":");
            team.printTeamInfo();
        }
    }

    public static void createTeams(){
        for(Constructor cons : allConstructors){
            Team myTeam = new Team();
            myTeam.addCons(cons);
            for(Driver driver : setDrivers){
                myTeam.addDriver(driver); 
            }
            if(setConstructor != null) myTeam.addCons(findConsByName(setConstructor));
            recursion(myTeam, 0);
            if(setConstructor != null) break;
        }
    }

    private static void recursion(Team myTeam, int filled){
        if(myTeam.drivers.size()==5 && myTeam.constructors == 1){
            if(myTeam.price <= budget) teams.add(cloneTeam(myTeam));
            return;
        }

        for (int i = filled; i < allDrivers.size(); i++) {
            myTeam.addDriver(allDrivers.get(i));
            recursion(myTeam, i + 1);
            myTeam.removeDriver(myTeam.drivers.get(myTeam.drivers.size()-1));
        }
    }

    private static Team cloneTeam(Team original) {
        Team copy = new Team();
        copy.constructor = original.constructor; 
        copy.constructors = original.constructors;
        copy.price = original.price;
        copy.pointsAvg = original.pointsAvg;

        for (Driver d : original.drivers) {
            copy.drivers.add(d);
        }

        return copy;
    }

    private static Constructor findConsByName(String name){
        for(Constructor cons : allConstructors){
            if(cons.name.toLowerCase().equals(name)) return cons;
        }

        return allConstructors.get(0);
    }

    private static Driver findDriverByName(String name){
        for(Driver driver : allDrivers){
            if(driver.name.toLowerCase().equals(name)) return driver;
        }

        return allDrivers.get(0);
    }

    private static void userInputLoop(){
        Scanner input = new Scanner(System.in);

        while(true){
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("****************Configuration****************");
            System.out.println("1: Run with current settings (none default)");
            System.out.println("2: Change spending budget");
            System.out.println("3: Change owned drivers");
            System.out.println("4: Change owned constructor");
            System.out.println("5: Change unavailable drivers");
            System.out.println("6: Change unavailable constructor");
            System.out.println("q: Terminate the program");

            String mode = input.nextLine();

            if(mode.equals("1")){
                System.out.print("\033[H\033[2J");
                System.out.flush();
                break;
            }
            else if(mode.equals("2")){
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("Enter new budget (format: 100000000 or 100 000 000 or 100.000.000):");
                //Make formating its own method?
                String spending = input.nextLine();
                budget = Integer.parseInt(spending.replaceAll("[\\s.]", ""));
            }
            else if(mode.equals("3")){
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("Enter owned drivers (format: lastnames separated by spaces):");
                //Make formating its own method?
                String[] owned = input.nextLine().toLowerCase().replaceAll("\\s+", " ").split(" ");
                for(String name : owned){
                    setDrivers.add(findDriverByName(name));
                    allDrivers.remove(findDriverByName(name));
                }
            }
            else if(mode.equals("4")){
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("Enter owned constructor:");
                String owned = input.nextLine().toLowerCase();
                setConstructor = owned;
            }
            else if(mode.equals("5")){
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("Enter unavailable drivers (format: lastnames separated by spaces):");
                
                String[] locked = input.nextLine().toLowerCase().replaceAll("\\s+", " ").split(" "); 
                int lockedLen = locked.length;
                ArrayList<Driver> driversToRemove = new ArrayList<>();

                for(int i = 0; i < lockedLen; i++) driversToRemove.add(findDriverByName(locked[i]));
                for(Driver driver : driversToRemove) allDrivers.remove(driver);
            }
            else if(mode.equals("6")){
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("Enter locked constructor:");

                String locked = input.nextLine().toLowerCase();
                allConstructors.remove(findConsByName(locked));
            }
            else if(mode.equals("q")){
                input.close();
                System.out.print("Goodbye!");
                System.exit(0);
            }
        }
        input.close();
    }
    
    public static void fillArrays(){
        allDrivers.add(new Driver("Norris", 22700000, 125));
        allDrivers.add(new Driver("Piastri", 25300000, 130));
        allDrivers.add(new Driver("Leclerc", 23900000, 153));
        allDrivers.add(new Driver("Russell", 24700000, 150));
        allDrivers.add(new Driver("Verstappen", 24200000, 139));
        allDrivers.add(new Driver("Hamilton", 27000000, 165));
        allDrivers.add(new Driver("Alonso", 14700000, 90));
        allDrivers.add(new Driver("Albon", 11900000, 94));
        allDrivers.add(new Driver("Hulkenberg", 12600000, 99));
        allDrivers.add(new Driver("Lawson", 20000000, 140));
        allDrivers.add(new Driver("Antonelli", 29900000, 179));
        allDrivers.add(new Driver("Hadjar", 19700000, 126));
        allDrivers.add(new Driver("Bortoleto", 12500000, 127));
        allDrivers.add(new Driver("Gasly", 19700000, 134));
        allDrivers.add(new Driver("Bearman", 18800000, 120));
        allDrivers.add(new Driver("Ocon", 14400000, 128));
        allDrivers.add(new Driver("Stroll", 8400000, 80));
        allDrivers.add(new Driver("Sainz", 16000000, 122));
        allDrivers.add(new Driver("Lindblad", 10500000, 121));
        allDrivers.add(new Driver("Colapinto", 12300000, 137));
        allDrivers.add(new Driver("Perez", 8400000, 97));
        allDrivers.add(new Driver("Bottas", 4800000, 83));


        allConstructors.add(new Constructor("McLaren", 22600000, 121));
        allConstructors.add(new Constructor("Ferrari", 26200000, 155));
        allConstructors.add(new Constructor("Mercedes", 28700000, 165));
        allConstructors.add(new Constructor("Red Bull", 21900000, 122));
        allConstructors.add(new Constructor("Racing Bulls", 16500000, 113));
        allConstructors.add(new Constructor("Aston Martin", 8800000, 76));
        allConstructors.add(new Constructor("Audi", 12400000, 98));
        allConstructors.add(new Constructor("Williams", 14200000, 94));
        allConstructors.add(new Constructor("Haas", 12100000, 108));
        allConstructors.add(new Constructor("Alpine", 18500000, 121));
        allConstructors.add(new Constructor("Cadillac", 6400000, 79));


    }
}