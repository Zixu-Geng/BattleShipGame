package edu.duke.zg73.battleship;

import edu.duke.zg73.battleship.Utils.CommunicateUtils;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Text;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.*;
import java.util.function.Function;

public class TextPlayer implements Serializable {

    public final Board theBoard;
    public final BoardTextView view;
    transient public BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    transient public PrintStream out =  System.out;
    final AbstractShipFactory<Character> shipFactory;
    final String name;
    final ArrayList<String> shipsToPlace;
    transient private HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;

    public HashMap<String, Integer> skill_count;
    final AILogic ai;
    final boolean is_ai;

    final boolean is_hardai;




    public TextPlayer(String name,
                      Board<Character> theBoard,
                      BufferedReader inputReader,
                      PrintStream out,
                      AbstractShipFactory<Character> shipFactory){

        this.name = name;
        this.theBoard = theBoard;
        this.inputReader = inputReader;
        this.out = out;
        this.shipFactory = shipFactory;
        this.view = new BoardTextView(theBoard);
        this.shipsToPlace = new ArrayList<String>();
        this.shipCreationFns = new HashMap<String, Function<Placement, Ship<Character>>>();
        this.skill_count = new HashMap<>();

        skill_count.put("M Move a ship to another square", 3);
        skill_count.put("S Sonar scan", 3);

        if(name == "AI") {
            this.is_ai = true;
            this.is_hardai = false;
            this.ai = new AILogic();
        }else if(name == "Hard AI"){
            this.is_ai = true;
            this.is_hardai = true;
            this.ai = new AILogic();

        }else{
            this.is_ai = false;
            this.is_hardai = false;
            this.ai = null;
        }

        setupShipCreationList();
        setupShipCreationMap();

    }


    public void reset_IO(){
        this.inputReader = new BufferedReader(new InputStreamReader(System.in));
        this.out = System.out;
    }


    private class AILogic implements Serializable{
        public HashSet<Coordinate> hitSuccess;
        public HashSet<Coordinate> hitFail;
        public HashMap<Coordinate, Integer> alreadyAttacked;
        private int currentRound = 0;

        public AILogic() {
            hitSuccess = new HashSet<>();
            hitFail = new HashSet<>();
            alreadyAttacked = new HashMap<>();

        }

        public int hammingDistance(Coordinate c1, Coordinate c2) {
            int res = Math.abs(c1.getColumn() - c2.getColumn()) + Math.abs(c1.getRow() - c2.getRow());
            return res;
        }

        public int heuristic(Coordinate c) {
            int baseScore = 100;

            if (alreadyAttacked.containsKey(c)) {
                int roundsSinceAttack = currentRound - alreadyAttacked.get(c);
                baseScore -= Math.max(0, 50 - roundsSinceAttack * 5);
            }

            for (Coordinate h : hitSuccess) {
                int distance = hammingDistance(c, h);
                baseScore -= 2 * distance;
            }
            for (Coordinate h : hitFail) {
                int distance = hammingDistance(c, h);
                baseScore += distance;
            }

            return baseScore;
        }

        public Coordinate next_attack() {
            currentRound++;
            int max_score = Integer.MIN_VALUE;
            Coordinate next = null;
            for (int i = 0; i < theBoard.getHeight(); i++) {
                for (int j = 0; j < theBoard.getWidth(); j++) {

                    Coordinate c = new Coordinate(i, j);
                    int currentScore = heuristic(c);
                    if (currentScore > max_score) {
                        next = c;
                        max_score = currentScore;
                    }
                }
            }



            alreadyAttacked.put(next, currentRound);

            return next;
        }
    }


    /**
     * Read a placement from the user
     * @param prompt is the string to prompt the user
     * @return the placement read from the user
     * @throws IOException
     */
    public Placement readPlacement(String prompt) throws IOException {

        out.println(prompt);
        String s = inputReader.readLine();
        if (s == null) {
            throw new EOFException();
        }
        return new Placement(s);
    }


    /**
     * Do one placement of a ship
     * @param shipName is the name of the ship to place
     * @param createFn is a function that creates a ship from a placement
     * @throws IOException
     */
    public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
        Placement placement;
        if(!is_ai){
            placement = readPlacement("Player " + this.name + " where would you like to place a "+shipName +"?");
        }else{
            placement = new Placement(ai_generatePlacement(shipName));
        }
        Ship<Character> s = createFn.apply(placement);
        String err = theBoard.tryAddShip(s);
        if(err != null){
            throw new IllegalArgumentException(err);
        }
        if(!is_ai){
            out.print(view.displayMyOwnBoard());
        }
    }

    /**
     * Do the placement phase of the game
     * @throws IOException
     */

    public void doPlacementPhase() throws IOException{
        if(!is_ai){
            view.displayMyOwnBoard();
            StringBuilder prompt = new StringBuilder("");
            out.print("--------------------------------------------------------------------------------\n");

            prompt.append("Player " + this.name + ":");
            prompt.append(
                    " you are going to place the following ships (which are all\n" +
                            "rectangular). For each ship, type the coordinate of the upper left\n" +
                            "side of the ship, followed by either H (for horizontal) or V (for\n" +
                            "vertical).  For example M4H would place a ship horizontally starting\n" +
                            "at M4 and going to the right.  You have\n\n"
                            + "2 \"Submarines\" ships that are 1x2\n" + "3 \"Destroyers\" that are 1x3\n"
                            + "3 \"Battleships\" that are 1x4\n" + "2 \"Carriers\" that are 1x6\n");
            out.print(prompt);

            out.print("--------------------------------------------------------------------------------\n");

        }




        for (String ship : this.shipsToPlace) {
            boolean placementSuccessful = false;
            while (!placementSuccessful) {
                try {
                    doOnePlacement(ship, this.shipCreationFns.get(ship));
                    placementSuccessful = true;
                } catch (IllegalArgumentException e) {
                    out.println(e.getMessage());

                }
            }
        }



    }

    /**
     * Set up the ship creation map
     */

    public void setupShipCreationMap(){
        this.shipCreationFns = new HashMap<>();
        shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
        shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
        shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
    }




    /**
     * Set up the ship creation list
     */
    protected void setupShipCreationList(){
        shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
        shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
        shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
    }

    /**
     * Print the turn header
     * @param enemyView
     */
    protected void printTurnHeader(BoardTextView enemyView){
        out.println("Player" + name +"'s turn");
        out.print(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Enemy's ocean"));
        out.println("---------------------------------------------------------------------------");
        out.println("Possible actions for Player " + name +":");
        out.println("F Fire at a square");
        for (String skill : skill_count.keySet()){
            out.println(skill + " (" + skill_count.get(skill) + " remaining)");
        }
        out.println("Player "+ name+", what would you like to do?");
        out.println("---------------------------------------------------------------------------");


    }

    /**
     *
     */

    protected String ai_generateCoords(){
        Random rand = new Random();

        int col = rand.nextInt(theBoard.getWidth());
        int row = rand.nextInt(theBoard.getHeight());

        char colLetter = (char) ('A' + col);


        return "" + colLetter + row;
    }

    protected String ai_generatePlacement(String shipname){
        if(shipname == "Submarine") {

            return ai_generateCoords() + "H";
        }else if(shipname == "Destroyer") {

            return ai_generateCoords() + "H";
        }else {

            return ai_generateCoords() + "R";
        }
    }


    /**
     * Do the action of firing at the enemy
     * @param enemyBoard is the board of the enemy
     * @param enemyView is the view of the enemy's board
     * @throws IOException
     */
    protected void doaction_F(Board<Character> enemyBoard, BoardTextView enemyView) throws IOException{
        while(true){
            try{
                String s;
                Coordinate attack_place;
                if(!is_ai){
                    out.println("Player " + name + ", Please input attack place: ");
                    s = inputReader.readLine();
                    attack_place = new Coordinate(s);
                }else{
                    if(!is_hardai){
                        s = ai_generateCoords();
                        attack_place = new Coordinate(s);
                    }else{
                        attack_place = ai.next_attack();
                    }

                }

                Character hit_display = enemyBoard.whatIsAtForSelf(attack_place);
                Ship<Character> hit_ship = enemyBoard.fireAt(attack_place);
                if(hit_ship == null){
                    if(is_ai && is_hardai){

                        ai.hitFail.add(attack_place);
                    }
                    out.println("hit nothing");

                }else{
                    if(hit_display == '*'){
                        out.println("Already Hit that Place!");
                    }else{
                        out.println("Hit " + hit_ship.getName() + "!");
                        if(is_ai && is_hardai){
                            ai.hitSuccess.add(attack_place);
                        }

                    }
                }
                if(!is_ai){
                    out.print(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Enemy's ocean"));
                }
                //out.print(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Enemy's ocean"));
                break;

            }catch (IllegalArgumentException e){
                out.println(e.getMessage());

            }
        }
    }

    protected void doaction_M(Board<Character> enemyBoard, BoardTextView enemyView) throws IOException{
        while(true){
            try{


                ArrayList<Ship<Character>> available_ships = theBoard.get_avaliable();
                if(available_ships.size() == 0){
                    out.println("No ship to move");
                    break;
                }

                out.println("Available ships to move: ");
                for(Ship s: available_ships){
                    out.println(s.getName());
                }

                out.println("Please input the ship you want to move: ");
                Coordinate chose_coord = new Coordinate(inputReader.readLine());
                Ship chose_ship = null;
                for(Ship s: available_ships){
                    if(s.occupiesCoordinates(chose_coord)){
                        chose_ship = s;
                        break;
                    }
                }
                if(chose_ship == null){

                    throw new IllegalArgumentException("No Ship in your input location");
                }
                Placement new_placement = readPlacement("Please input the new place and orientation: ");
                try{

                    theBoard.moveShip(chose_ship, new_placement);
                    skill_count.put("M Move a ship to another square", skill_count.get("M Move a ship to another square") - 1);
                    out.print(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Enemy's ocean"));
                }catch (IllegalArgumentException e){
                    out.println(e.getMessage());

                    theBoard.tryAddShip(chose_ship);
                    out.println("move ship again");
                    continue;
                }
                break;
            }catch (IllegalArgumentException e){

                out.println(e.getMessage());




            }
        }
    }

    protected void doaction_S(Board<Character> enemyBoard, BoardTextView enemyView) throws IOException{
        while(true){
            try{
                out.println("please enter the place where to scan");

                String s = inputReader.readLine();

                Coordinate scan_coord;
                scan_coord = new Coordinate(s);


                //Submarine, Destroyer, Battleship, Carrier
                int[] scan_result = {0,0,0,0};
                int centerRow = scan_coord.getRow();
                int centerColumn = scan_coord.getColumn();
                int maxDistance = 3;

                for (int i = centerRow - 3; i <= centerRow + 3; i++) {

                    int distanceFromCenterRow = Math.abs(i - centerRow);
                    int columns_dis = maxDistance - distanceFromCenterRow;

                    for (int j = centerColumn - columns_dis; j <= centerColumn + columns_dis; j++) {

                        if (i >= 0 && i < enemyBoard.getHeight() && j >= 0 && j < enemyBoard.getWidth()) {
                            Character result = enemyBoard.whatIsAtForSelf(new Coordinate(i, j));
                            Character result2 = enemyBoard.whatIsAtForEnemy(new Coordinate(i, j));


                            if (result == null) continue;

                            if(result == '*'){
                                result = result2;
                            }
                            switch (result) {
                                case 's':
                                    scan_result[0]++;
                                    break;
                                case 'd':
                                    scan_result[1]++;
                                    break;
                                case 'b':
                                    scan_result[2]++;
                                    break;
                                case 'B':
                                    scan_result[2]++;
                                    break;
                                case 'c':
                                    scan_result[3]++;
                                    break;
                                case 'C' :
                                    scan_result[3]++;
                                    break;

                            }
                        }
                    }
                }



                out.println("Submarines occupy " + scan_result[0] + " square(s)");
                out.println("Destroyer occupy " + scan_result[1] + " square(s)");
                out.println("Battleships occupy " + scan_result[2] + " square(s)");
                out.println("Carriers occupy " + scan_result[3] + " square(s)");
                skill_count.put("S Sonar scane", skill_count.get("S Sonar scan") - 1);

                break;

            }catch (IllegalArgumentException e){

                out.println(e.getMessage());

            }
        }
    }

    /**
     * Play one turn of the game
     * @param enemyBoard is the board of the enemy
     * @param enemyView is the view of the enemy's board
     * @throws IOException
     */

    public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView) throws IOException {

        printTurnHeader(enemyView);

        while(true){
            try {
                String action;
                if(is_ai){
                    action = "F";
                }else{
                    action = inputReader.readLine();
                    action = action.toUpperCase();
                }
                if(action.equals("F")){
                    doaction_F(enemyBoard, enemyView);
                }else if(action.equals("M") && skill_count.get("M Move a ship to another square") > 0){
                    doaction_M(enemyBoard, enemyView);
                }else if(action.equals("S") && skill_count.get("S Sonar scan") > 0){

                    doaction_S(enemyBoard, enemyView);
                }else{
                    throw new IllegalArgumentException("Invalid action");
                }

            }catch (IllegalArgumentException e){
                out.println(e.getMessage());
                continue;
            }
            break;
        }


    }

    public void win_message(){
        out.println("Player "+ name + ", Congradulation! you win!");
    }

}