package edu.duke.zg73.Gui;

import edu.duke.zg73.battleship.*;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

public class AiPlayer {

    public final Board theBoard;
    public final BoardTextView view;

    final V2ShipFactory shipFactory;
    String name;
    final ArrayList<String> shipsToPlace;
    transient private HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;

    public HashMap<String, Integer> skill_count;

    final AILogic ai;

    protected void setupShipCreationList(){
        shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
        shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
        shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
    }

    public void setupShipCreationMap(){
        this.shipCreationFns = new HashMap<>();
        shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
        shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
        shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
    }

    public AiPlayer(){
        this.name = "AI";
        this.theBoard = new BattleShipBoard<>(10, 20, 'X');
        this.shipFactory = new V2ShipFactory();
        this.view = new BoardTextView(theBoard);
        this.shipsToPlace = new ArrayList<>();
        this.shipCreationFns = new HashMap<String, Function<Placement, Ship<Character>>>();
        this.skill_count = new HashMap<>();
        this.ai  =new AILogic();
        skill_count.put("M Move a ship to another square", 3);
        skill_count.put("S Sonar scan", 3);

        setupShipCreationList();
        setupShipCreationMap();
    }


    public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
        Placement placement = new Placement(ai_generatePlacement(shipName));
        Ship<Character> newShip = createFn.apply(placement);
        String err = theBoard.tryAddShip(newShip);
        if(err != null) {
            throw new IOException(err);
        }

    }

    public void doPlacementPhase(){
        for (String ship : this.shipsToPlace) {
            boolean placementSuccessful = false;
            while (!placementSuccessful) {
                try {
                    doOnePlacement(ship, this.shipCreationFns.get(ship));
                    placementSuccessful = true;
                } catch (IllegalArgumentException | IOException e) {
                }
            }
        }
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

    protected String ai_generateCoords(){
        Random rand = new Random();

        int col = rand.nextInt(theBoard.getWidth());
        int row = rand.nextInt(theBoard.getHeight());

        char colLetter = (char) ('A' + col);


        return "" + colLetter + row;
    }

    protected void doaction_S(Board<Character> enemyBoard, BoardTextView enemyView) throws IOException{}

    protected void doaction_M(Board<Character> enemyBoard, BoardTextView enemyView) throws IOException{}

    public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView) throws IOException {
        boolean isTest = true;
        while(isTest){
//            isTest = false;
            try{
                String action;
                action = "F";

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
                System.out.println(e.getMessage());
                break;
            }
            break;
        }

    }






    protected void doaction_F(Board<Character> enemyBoard, BoardTextView enemyView) {
        while(true){
            try{
                String s;
                Coordinate attack_place;
                attack_place = ai.next_attack();
                System.out.println("AI attacking at " + attack_place);
                Character hit_display = enemyBoard.whatIsAtForSelf(attack_place);
                Ship<Character> hit_ship = enemyBoard.fireAt(attack_place);
                if(hit_ship == null){
                    ai.attackFail(attack_place);
                    System.out.println("AI missed at " + attack_place);
                }else{
                    if(hit_display == '*'){
                        System.out.println("Already Hit that Place!");
                    }else{
                        ai.attackSuccess(attack_place, hit_display);
                        System.out.println("AI hit at " + attack_place);
                    }
                }
                break;


            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }


    public class AILogic implements Serializable {
        public HashMap<Character, HashSet<Coordinate>> hitSuccess;
        public HashSet<Coordinate> hitFail;
        public HashMap<Coordinate, Integer> alreadyAttacked;

        public HashMap<Character, ShipAttackPattern> attackPatterns;

        private int currentRound = 0;
        private int AttackMode = 0;

        public AILogic() {
            hitSuccess = new HashMap<>();
            hitFail = new HashSet<>();
            alreadyAttacked = new HashMap<>();
            attackPatterns = new HashMap<>();
            initializeAttackPatterns();
        }

        private void initializeAttackPatterns() {

            attackPatterns.put('s', new SubmarineAttackPattern());
            attackPatterns.put('b', new BattleshipAttackPattern());

        }

        public int hammingDistance(Coordinate c1, Coordinate c2) {
            return Math.abs(c1.getColumn() - c2.getColumn()) + Math.abs(c1.getRow() - c2.getRow());
        }

        public int heuristic(Coordinate c) {
            int baseScore = 100;



            for(Character ship : hitSuccess.keySet()){
                for (Coordinate h : hitSuccess.get(ship)){
                    int distance = hammingDistance(c, h);
                    baseScore -= 2 * distance;
                }
            }

            for (Coordinate h : hitFail) {
                int distance = hammingDistance(c, h);
                baseScore += distance;
            }



            return baseScore;
        }

        public Coordinate randomSearch() {
            currentRound++;
            int maxScore = Integer.MIN_VALUE;
            Coordinate next = null;
            for (int i = 0; i < theBoard.getHeight(); i++) {
                for (int j = 0; j < theBoard.getWidth(); j++) {
                    Coordinate c = new Coordinate(i, j);
                    int currentScore = heuristic(c);
                    if (currentScore > maxScore && !alreadyAttacked.containsKey(c)) {
                        next = c;
                        maxScore = currentScore;
                    }
                }
            }
            alreadyAttacked.put(next, currentRound);
            return next;
        }

        public Coordinate Attacking() {
            for (Map.Entry<Character, HashSet<Coordinate>> entry : hitSuccess.entrySet()) {
                ShipAttackPattern attackPattern = attackPatterns.get(entry.getKey());
                if (attackPattern != null) {
                    List<Coordinate> potentialTargets = attackPattern.generateAttackTargets(entry.getValue());
                    for (Coordinate target : potentialTargets) {
                        if (isValidTarget(target)) {
                            alreadyAttacked.put(target, currentRound);
                            return target;
                        }
                    }
                }
            }
            AttackMode = 0;
            return randomSearch();
        }



        public Coordinate next_attack() {
            if(AttackMode == 0){
                return randomSearch();
            }else{
                return Attacking();
            }
        }

        public void attackSuccess(Coordinate c, Character ship){
            hitSuccess.putIfAbsent(ship, new HashSet<>());
            hitSuccess.get(ship).add(c);
            AttackMode = 1;
        }

        public void attackFail(Coordinate c){
            hitFail.add(c);
            alreadyAttacked.put(c, currentRound);
        }



        private boolean isValidTarget(Coordinate c) {
            return c.getRow() >= 0 && c.getRow() < theBoard.getHeight() &&
                    c.getColumn() >= 0 && c.getColumn() < theBoard.getWidth() &&
                    !alreadyAttacked.containsKey(c);
        }


    }

}
