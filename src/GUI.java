import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;

/**
 * All the stuff for the GUI comes from here:https://docs.oracle.com/javase/8/javafx/get-started-tutorial/hello_world.htm
 * IntelliJ IDEA "auto-correct"/auto insert code
 * https://www.geeksforgeeks.org/tag/javafx/
 * Raphael is the one that introduced this and he helped me with the
 * switch statements. https://www.baeldung.com/java-enum-simple-state-machine
 * Andrew also helped me with the images as he is the one that told how to use ImageView
 *
 * @author (Benito Moreno - Garza & Grayson Drinkard)
 */
public class GUI extends Application {
    //I am
    public enum turnPhases {
        Draw,
        Discard,
        AttackChoose,
        Attack,
        DefendChoose,
        Defend
    }

    // We keep track of the count, and label displaying the count:
    private Stage stage = new Stage();
    //This phase is what determines what the gui will display
    private int Phase = 0;
    private turnPhases Turn = turnPhases.Draw;
    private Player p1;
    private MonsterDeck monsters;
    private DiscardPiles monsterDiscard;
    private ArrayList<Integer> monsterOnField, cardsInHand;
    private int cardChosen, monsterChosen, Health, monstersLeft, indexM, indexP, drawTurn;
    private Label monsterDeck, monsterDiscardL, healthDeck, healthDiscard, attackDeck, attackDiscard, healthNum, currentPhase;
    private GridPane pane;
    private ImageView card1, card2, card3, mon1, mon2;
    private Scene scene;
    private Button h1, h2, h3, m1, m2, nextPhase, discardHand;

    //The constructor will handle all the player creation things and field creation
    public void create(String choice) {
        drawTurn = 0;
        p1 = new Player(choice);
        monsters = new MonsterDeck();
        monsterDiscard = new DiscardPiles("Monster");
        cardsInHand = new ArrayList<>(3);
        for (int i = 0; i < 3; i++)
            cardsInHand.add(0);
        monsterOnField = new ArrayList<>(2);
        monstersLeft = 47;
        Health = p1.getHealth() ;
    }

    /*This method is called whenever the draw phase begins
     * it calls players draw method then copys the cards drawn into the gui's card list
     * it also calls the monsterdeck's get monster which "Spawns" the monster
     */
    public void draw() {
        if (drawTurn == 0) {
            p1.drawCard();
            updateHand();
            mon1.setImage(new Image(monsters.getMonster(monstersLeft) + "M.png"));
            monsterOnField.add(monsters.getMonster(monstersLeft));
            monstersLeft--;
            mon2.setImage(new Image(monsters.getMonster(monstersLeft) + "M.png"));
            monsterOnField.add(monsters.getMonster(monstersLeft));
            monstersLeft--;
            drawTurn = 1;

        }
        if (drawTurn == 1) {
            p1.drawCard();
            updateHand();
            if (monsterOnField.get(0) == 0) {
                monsterOnField.set(0, monsters.getMonster(monstersLeft));
                monstersLeft--;
                updateMonster();
            }
            if (monsterOnField.get(1) == 0) {
                monsterOnField.set(1, monsters.getMonster(monstersLeft));
                monstersLeft--;
                updateMonster();
            }
        }
        attackDeck.setText("Attack Deck\nCards Remaining: "+p1.attack.cardsRemaining());
    }

    /*The two following methods update the field so they get their data from the two array list for monster and hand
     *
     */
    public void updateHand() {
        for (int i = 0; i < 3; i++)
            cardsInHand.set(i, p1.getHand().get(i));
        card1.setImage(new Image(cardsInHand.get(0) + ".png"));
        card2.setImage(new Image(cardsInHand.get(1) + ".png"));
        card3.setImage(new Image(cardsInHand.get(2) + ".png"));
    }

    public void updateMonster() {
        monsterDeck.setText("Monster Deck \nMonsters Left: "+monstersLeft);
        mon1.setImage(new Image(monsterOnField.get(0) + "M.png"));
        mon2.setImage(new Image(monsterOnField.get(1) + "M.png"));
    }

    /*When working properly it will allow the player to discard their whole hand and draw one card
     *or to discard 1 card and draw no cards
     */
    public void discard() {


    }

    /*This method takes care of the attack phase
     *
     */
    public void battle() {
        if (cardChosen >= monsterChosen && cardChosen != 0) {
            monsterDiscard.addCards(monsterChosen);
            monsterOnField.set(indexM, 0);
            p1.attack(indexP);
            updateHand();
            updateMonster();

        }


    }

    /*This method takes care of the defending phase and has safeguards against not choosing a card
     *
     */
    public void defend() {
        if (cardChosen == 0 && monsterChosen == 0)
            for (int i = 0; i < 2; i++)
                if (monsterOnField.get(i) != 0) {
                    p1.takeDamage(monsterOnField.get(i));
                    healthNum.setText("Health Remaining: " + (p1.getHealth()));
                }

        if (cardChosen >= monsterChosen && cardChosen != 0) {
            monsterDiscard.addCards(monsterChosen);
            monsterOnField.set(indexM, 0);
            p1.defend(indexP);
            updateHand();
            for (int i = 0; i < 2; i++)
                if (monsterOnField.get(i) != 0)
                    p1.takeDamage(monsterOnField.get(i));
            updateHand();
            updateMonster();

        }
        if(checkVictory()==0) {
            Phase = 3;
            start(stage);
        }
        if(checkVictory()==1){
            Phase=4;
            start(stage);
        }

    }
    private int checkVictory()
    {
        if(p1.isDead())
            return 0;
        if(monstersLeft==0)
            return 1;
        return 2;
    }
    /*This is the action event for the button in charge of the phase changes
     *it will also update the current stage in the currentPhase label
     */
    private void setNextPhase() {

        switch (Turn) {
            case Draw:
                Turn = turnPhases.Discard;
                currentPhase.setText("Current Phase is: " + Turn);
                discardHand.setVisible(true);
                break;
            case Discard:
                cardChosen = 0;
                monsterChosen = 0;
                indexP = -1;
                indexM = -1;
                Turn = turnPhases.AttackChoose;
                currentPhase.setText("Current Phase is: " + Turn);
                m1.setVisible(true);
                m2.setVisible(true);
                h1.setVisible(true);
                h2.setVisible(true);
                h3.setVisible(true);
                discardHand.setVisible(false);
                break;
            case AttackChoose:
                Turn = turnPhases.Attack;
                currentPhase.setText("Current Phase is: " + Turn);
                m1.setVisible(false);
                m2.setVisible(false);
                h1.setVisible(false);
                h2.setVisible(false);
                h3.setVisible(false);
                doTurns();
                break;
            case Attack:
                Turn = turnPhases.DefendChoose;
                currentPhase.setText("Current Phase is: " + Turn);
                m1.setVisible(true);
                m2.setVisible(true);
                h1.setVisible(true);
                h2.setVisible(true);
                h3.setVisible(true);
                cardChosen = 0;
                monsterChosen = 0;
                indexP = -1;
                indexM = -1;
                break;
            case DefendChoose:
                Turn = turnPhases.Defend;
                currentPhase.setText("Current Phase is: " + Turn);
                m1.setVisible(false);
                m2.setVisible(false);
                h1.setVisible(false);
                h2.setVisible(false);
                h3.setVisible(false);
                doTurns();
                break;
            case Defend:
                Turn = turnPhases.Draw;
                currentPhase.setText("Current Phase is: " + Turn);
                doTurns();
                break;
        }
    }

    /*The following methods are all just action events for the buttons created
     * at the moment they do not do much more than change the stage but the class choices are what allow different classes to be played.
     */
    private void DiscardHand() {
        p1.discardCards();
        updateHand();
        attackDeck.setText("Attack Deck\nCards Remaining: "+p1.attack.cardsRemaining());
        setNextPhase();
    }

    private void card1() {
        if (p1.getHand().get(0) != 0) {
            cardChosen = p1.getHand().get(0);
            indexP = 0;
        } else {
            cardChosen = 0;
            indexP = -1;
        }
    }

    private void card2() {
        if (p1.getHand().get(1) != 0) {
            cardChosen = p1.getHand().get(1);
            indexP = 1;
        } else {
            cardChosen = 0;
            indexP = -1;
        }
    }

    private void card3() {
        if (p1.getHand().get(2) != 0) {
            cardChosen = p1.getHand().get(2);
            indexP = 2;
        } else {
            cardChosen = 0;
            indexP = -1;
        }
    }

    private void monster1() {
        if (monsterOnField.get(0) != 0) {
            monsterChosen = monsterOnField.get(0);
            indexM = 0;
        } else {
            monsterChosen = 0;
            indexM = -1;
        }
    }

    private void monster2() {
        if (monsterOnField.get(1) != 0) {
            monsterChosen = monsterOnField.get(1);
            indexM = 1;
        } else {
            monsterChosen = 0;
            indexM = -1;
        }
    }

    private void ClassMage() {
        Phase++;
        create("Mage");
        start(stage);
    }

    private void ClassWarrior() {
        Phase++;
        create("Warrior");
        start(stage);
    }

    private void ClassRogue() {
        Phase++;
        create("Rogue");
        start(stage);
    }

    private void ClassPaladin() {
        Phase++;
        create("Paladin");
        start(stage);
    }

    private void classChoice() {
        Phase=2;
        create("generic");
        start(stage);
    }

    /*This method is the stage creator the GUI. Depending on the stage it will create a different scene
     *The scene that is used for gameplay is phase 2. In that phase all the buttons and label are initialized for use later
     *
     *
     */
    public void start(Stage holder) {
        if (Phase == 0) {
            Label instrucitons = new Label("Welcome to Ascension! So that Turn Phases goes in the order Draw, Discard,\nAttack Choose, Attack, Defend Choose, and Defend. " +
                    "Draw is done automatically \nfor you. Discard allows for the option to discard your whole hand and draw 1 \ncard. AttackChoose allows you to choose your target. " +
                    "Same with DefendChoose. \nAttack and Defend show the results of your choices. In order to switch \nphases you must click the change phase button at the bottom of the screen." +
                    "\nSo those are the basics. To Win you must defeat all monsters in the monster \ndeck. But watch out if your health hits 0, it's game over."+"\nHAVE FUN!");
            // Create a Button or any control item
            Button myButton = new Button("start");
            myButton.setPrefSize(300, 100);
            // Create a new grid pane
            GridPane pane = new GridPane();
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setMinSize(200, 200);
            pane.setVgap(10);
            pane.setHgap(10);

            //set an action on the button using method reference
            myButton.setOnAction(action -> {
                try {
                    classChoice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Add the button and label into the pane
            pane.add(myButton, 5, 5);
            pane.add(instrucitons,5,8);

            // JavaFX must have a Scene (window content) inside a Stage (window)
            scene = new Scene(pane, 500, 400);
            stage.setTitle("Ascension");
            stage.setScene(scene);

            // Show the Stage (window)
            stage.show();
        }
        if (Phase == 1) {
            //creating the new buttons and labels
            Label options = new Label("Choose Your Class");
            Button b1 = new Button("Mage");
            Button b2 = new Button("Warrior");
            Button b3 = new Button("Rogue");
            Button b4 = new Button("Paladin");
            //setting the actions fro the buttons
            b1.setOnAction(action -> {
                try {
                    ClassMage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            b2.setOnAction(action -> {
                try {
                    ClassWarrior();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            b3.setOnAction(action -> {
                try {
                    ClassRogue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            b4.setOnAction(action -> {
                try {
                    ClassPaladin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //creating a new grid pane
            GridPane pane = new GridPane();
            //adding all the pieces to the grid pane
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setMinSize(300, 300);
            pane.setVgap(10);
            pane.setHgap(10);
            pane.add(options, 4, 5);
            pane.add(b1, 1, 10);
            pane.add(b2, 3, 10);
            pane.add(b3, 5, 10);
            pane.add(b4, 7, 10);
            //adding the pane to the scene then showing it
            scene = new Scene(pane, 400, 200);
            stage.setTitle("Ascension");
            stage.setScene(scene);
            stage.show();
        }
        if (Phase == 2) {
            //creating all the labels and buttons
            monsterDeck = new Label("\nMonster Deck");
            monsterDeck.setText("Monster Deck \nMonsters Left: "+monstersLeft);
            monsterDeck.setGraphic(new ImageView(new Image("backOfCard.png")));
            monsterDiscardL = new Label("\nMonster Discard Pile");
            monsterDiscardL.setGraphic(new ImageView(new Image("backOfCard.png")));
            healthDeck = new Label("\nHealth Deck");
            healthDeck.setGraphic(new ImageView(new Image("backOfCard.png")));
            healthDiscard = new Label("\nHealth Discard Pile");
            healthDiscard.setGraphic(new ImageView(new Image("backOfCard.png")));
            attackDeck = new Label("\nAttack Deck");
            attackDeck.setGraphic(new ImageView(new Image("backOfCard.png")));
            attackDiscard = new Label("\nAttack Discard Pile");
            attackDiscard.setGraphic(new ImageView(new Image("backOfCard.png")));
            healthNum = new Label("Health Remaining: " + Health);
            currentPhase = new Label("Current Phase is:" + Turn);
            m1 = new Button("Monster 1");
            m2 = new Button("Monster 2");
            h1 = new Button("Card 1");
            h2 = new Button("Card 2");
            h3 = new Button("Card 3");
            discardHand = new Button("Discard Hand");
            nextPhase = new Button("Next Phase");
            card1 = new ImageView();
            card2 = new ImageView();
            card3 = new ImageView();
            mon1 = new ImageView();
            mon2 = new ImageView();
            pane = new GridPane();
            //creating the grid pane
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setMinSize(300, 300);
            pane.setVgap(10);
            pane.setHgap(10);
            pane.add(monsterDeck, 10, 5);
            pane.add(monsterDiscardL, 5, 5);
            pane.add(healthDeck, 5, 15);
            pane.add(healthDiscard, 10, 15);
            pane.add(healthNum, 5, 20);
            pane.add(attackDiscard, 20, 15);
            pane.add(attackDeck, 25, 15);
            pane.add(currentPhase, 25, 5);
            pane.add(m1, 15, 5);
            pane.add(m2, 20, 5);
            pane.add(h1, 12, 20);
            pane.add(h2, 15, 20);
            pane.add(h3, 20, 20);
            pane.add(nextPhase, 20, 27);
            pane.add(card1, 12, 25);
            pane.add(card2, 15, 25);
            pane.add(card3, 20, 25);
            pane.add(mon1, 15, 7);
            pane.add(mon2, 20, 7);
            pane.add(discardHand, 25, 27);
            //adding the pane to the scene and then showing the scene
            scene = new Scene(pane, 1150, 750);
            stage.setTitle("Ascension");
            stage.setScene(scene);
            stage.show();
            //adding actions to the buttons
            discardHand.setOnAction(action -> {
                try {
                    DiscardHand();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            nextPhase.setOnAction(action -> {
                try {
                    setNextPhase();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            m1.setOnAction(action -> {
                try {
                    monster1();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            m2.setOnAction(action -> {
                try {
                    monster2();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            h1.setOnAction(action -> {
                try {
                    card1();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            h2.setOnAction(action -> {
                try {
                    card2();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            h3.setOnAction(action -> {
                try {
                    card3();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //setting some of the buttons visibility to false so that they are not clicked during a phase where
            //they are not needed
            discardHand.setVisible(false);
            m1.setVisible(false);
            m2.setVisible(false);
            h1.setVisible(false);
            h2.setVisible(false);
            h3.setVisible(false);
            doTurns();
        }
        if(Phase==3){
            Button myB = new Button("");
            myB.setGraphic(new ImageView(new Image("Defeat.png")));
            myB.setPrefSize(300, 100);
            pane = new GridPane();
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setMinSize(300, 300);
            pane.setVgap(10);
            pane.setHgap(10);
            // Add the button and label into the pane
            pane.add(myB, 9, 8);

            // JavaFX must have a Scene (window content) inside a Stage (window)
            scene = new Scene(pane, 500, 300);
            stage.setTitle("Ascension");
            stage.setScene(scene);
            // Show the Stage (window)
            stage.show();
        }
        if(Phase==4){
            Button myB = new Button("Victory");
            myB.setPrefSize(300, 100);
            pane = new GridPane();
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setMinSize(300, 300);
            pane.setVgap(10);
            pane.setHgap(10);
            // Add the button and label into the pane
            pane.add(myB, 9, 8);

            // JavaFX must have a Scene (window content) inside a Stage (window)
            scene = new Scene(pane, 500, 300);
            stage.setTitle("Ascension");
            stage.setScene(scene);

            // Show the Stage (window)
            stage.show();
        }

    }

    //This method is the one in charge of calling all the other turn methods that were created above
    private void doTurns() {
        switch (Turn) {
            case Draw:
                updateHand();
                currentPhase.setText("Current Phase is: " + Turn);
                draw();
                break;
            case Discard:
                updateHand();
                currentPhase.setText("Current Phase is: " + Turn);
                discard();
                break;
            case Attack:
                updateHand();
                currentPhase.setText("Current Phase is: " + Turn);
                battle();
                break;
            case Defend:
                updateHand();
                currentPhase.setText("Current Phase is: " + Turn);
                defend();
                break;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}