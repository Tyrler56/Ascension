import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
    public enum turnPhases {
        Draw,
        Discard,
        AttackChoose,
        Attack,
        DefendChoose,
        Defend;
    }

    // We keep track of the count, and label displaying the count:
    private Stage stage = new Stage();
    //This phase is what determines what the gui will display
    private int Phase = 0;
    //These are the different turns each one will be in charge of the turn phases in the game
    // private int Draw = 0;
    // private int Discard = 1;
    // private int Attack = 2;
    // private int Defend = 3;
    //the overall turn counter
    private turnPhases Turn = turnPhases.Draw;
    private Player p1;
    private MonsterDeck monsters;
    private DiscardPiles monsterDiscard;
    private ArrayList<Integer> monsterOnField, unblockableLane, cardsInHand;
    private int cardChosen, monsterChosen, Health, monstersLeft, indexM, indexP, drawTurn;
    private Label monsterDeck, monsterDiscardL, healthDeck, healthDiscard, attackDeck, attackDiscard, healthNum;
    private boolean isAlive;
    private GridPane pane;
    private ImageView card1, card2, card3, mon1, mon2;
    private Scene scene;
    private Button h1, h2, h3, m1, m2;

    public void create(String choice) {
        drawTurn = 0;
        isAlive = true;
        p1 = new Player(choice);
        monsters = new MonsterDeck();
        monsterDiscard = new DiscardPiles("Monster");
        cardsInHand = new ArrayList<>();
        monsterOnField = new ArrayList<>(2);
        unblockableLane = new ArrayList<>(2);
        monstersLeft = 47;
        Health = p1.getHealth();
    }
    public void draw() {
        switch (drawTurn) {
            case 0:
                p1.drawCard();
                for (int i = 0; i < 3; i++)
                    cardsInHand.add(p1.getHand().get(i));
                updateHand();
                mon1.setImage(new Image(monsters.getMonster(monstersLeft) + "M.png"));
                monsterOnField.add(monsters.getMonster(monstersLeft));
                monstersLeft--;
                mon2.setImage(new Image(monsters.getMonster(monstersLeft) + "M.png"));
                monsterOnField.add(monsters.getMonster(monstersLeft));
                monstersLeft--;
                Turn = turnPhases.Discard;
                drawTurn++;
                doTurns();
                break;
            case 1:
                p1.drawCard();
                updateHand();
                if (monsterOnField.get(0) == 0) ;
            {
                mon1.setImage(new Image(monsters.getMonster(monstersLeft) + "M.png"));
                monsterOnField.set(0, monsters.getMonster(monstersLeft));
                monstersLeft--;
            }
            if (monsterOnField.get(1) == 0) ;
            {
                mon2.setImage(new Image(monsters.getMonster(monstersLeft) + "M.png"));
                monsterOnField.set(1, monsters.getMonster(monstersLeft));
                monstersLeft--;
            }
            Turn = turnPhases.Discard;
            doTurns();

        }
    }

    public void updateHand() {
        for (int i = 0; i < 3; i++)
            cardsInHand.add(p1.getHand().get(i));
        card1.setImage(new Image(cardsInHand.get(0) + ".png"));
        card2.setImage(new Image(cardsInHand.get(1) + ".png"));
        card3.setImage(new Image(cardsInHand.get(2) + ".png"));
    }

    public void discard() {
        Turn = turnPhases.AttackChoose;
        doTurns();
    }

    public void battle() {
        if (cardChosen > monsterChosen) {
            monsterDiscard.addCards(monsterChosen);
            monsterOnField.set(indexM,0);
            p1.attack(indexP);
            updateHand();
            for (Integer num : monsterOnField)
                p1.takeDamage(num);
            Turn = turnPhases.DefendChoose;
            cardChosen=0;
            monsterChosen=0;
            updateHand();
            doTurns();
        }
    }

    public void defend() {
        if (cardChosen >= monsterChosen) {
            monsterDiscard.addCards(monsterChosen);
            monsterOnField.set(indexM,0);
            p1.defend(indexP);
            updateHand();
            for (int i=0;i<2;i++)
                if(monsterOnField.get(i)!=0)
                    p1.takeDamage(monsterOnField.get(i));
            Turn = turnPhases.Draw;
            updateHand();
            doTurns();
        }
    }

    private void card1() {
        switch (Turn) {
            case AttackChoose:
                cardChosen = p1.getHand().get(0);
                indexP = 0;
                break;
            case DefendChoose:
                cardChosen = p1.getHand().get(0);
                indexP = 0;
                break;
        }
    }

    private void card2() {
        switch (Turn) {
            case AttackChoose:
                cardChosen = p1.getHand().get(1);
                indexP = 1;
                break;
            case Defend:
                cardChosen = p1.getHand().get(1);
                indexP = 1;
                break;
        }
    }

    private void card3() {
        switch (Turn) {
            case AttackChoose:
                cardChosen = p1.getHand().get(2);
                indexP = 2;
                break;
            case DefendChoose:
                cardChosen = p1.getHand().get(2);
                indexP = 2;
                break;
        }
    }

    private void monster1() {
        switch (Turn) {
            case AttackChoose:
                monsterChosen = monsterOnField.get(0);
                indexM = 0;
                Turn = turnPhases.Attack;
                doTurns();
                break;
            case DefendChoose:
                monsterChosen = monsterOnField.get(0);
                indexM = 0;
                Turn = turnPhases.Defend;
                doTurns();
                break;
        }
    }

    private void monster2() {
        switch (Turn) {
            case AttackChoose:
                monsterChosen = monsterOnField.get(1);
                indexM = 1;
                Turn = turnPhases.Attack;
                doTurns();
                break;
            case DefendChoose:
                monsterChosen = monsterOnField.get(1);
                indexM = 1;
                Turn = turnPhases.Defend;
                doTurns();
                break;
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
        Phase++;

        start(stage);
    }

    public void start(Stage holder) {
        if (Phase == 0) {
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
            pane.add(myButton, 9, 8);

            // JavaFX must have a Scene (window content) inside a Stage (window)
            scene = new Scene(pane, 500, 300);
            stage.setTitle("Ascension");
            stage.setScene(scene);

            // Show the Stage (window)
            stage.show();
        }
        if (Phase == 1) {
            Label options = new Label("Choose Your Class");
            Button b1 = new Button("Mage");
            Button b2 = new Button("Warrior");
            Button b3 = new Button("Rogue");
            Button b4 = new Button("Paladin");
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
            GridPane pane = new GridPane();
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setMinSize(300, 300);
            pane.setVgap(10);
            pane.setHgap(10);
            pane.add(options, 4, 5);
            pane.add(b1, 1, 10);
            pane.add(b2, 3, 10);
            pane.add(b3, 5, 10);
            pane.add(b4, 7, 10);
            scene = new Scene(pane, 400, 200);
            stage.setTitle("Ascension");
            stage.setScene(scene);
            stage.show();
        }
        if (Phase == 2) {
            monsterDeck = new Label("Monster Deck");
            monsterDiscardL = new Label("Monster Discard Pile");
            healthDeck = new Label("Health Deck");
            healthDiscard = new Label("Health Discard Pile");
            attackDeck = new Label("Attack Deck");
            attackDiscard = new Label("Attack Discard Pile");
            healthNum = new Label("Health Remaining: " + Health);
            m1 = new Button("Monster 1");
            m2 = new Button("Monster 2");
            h1 = new Button("Card 1");
            h2 = new Button("Card 2");
            h3 = new Button("Card 3");
            card1 = new ImageView();
            card2 = new ImageView();
            card3 = new ImageView();
            mon1 = new ImageView();
            mon2 = new ImageView();
            pane = new GridPane();
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
            pane.add(m1, 15, 5);
            pane.add(m2, 20, 5);
            pane.add(h1, 10, 20);
            pane.add(h2, 15, 20);
            pane.add(h3, 20, 20);
            pane.add(card1, 10, 25);
            pane.add(card2, 15, 25);
            pane.add(card3, 20, 25);
            pane.add(mon1, 15, 7);
            pane.add(mon2, 20, 7);
            scene = new Scene(pane, 800, 700);
            stage.setTitle("Ascension");
            stage.setScene(scene);
            stage.show();

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
            doTurns();
        }
    }

    private void doTurns() {
        switch (Turn) {
            case Draw:
                System.out.println("Draw");
                draw();
                break;
            case Discard:
                System.out.println("Discard");
                updateHand();
                discard();
                break;
            case Attack:
                System.out.println("attack");
                updateHand();
                battle();
                break;
            case Defend:
                System.out.println("Defend");
                updateHand();
                defend();
                break;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
