import java.util.*;
/**
 * This Class Will take care of player specific jobs. i.e. cards in hand,
 * class, and all the different decks/discardPiles that it has. 
 *
 * @author (Benito Moreno-Garza & Grayson Drinkard)
 */
public class Player
{
    private ArrayList<Integer> hand;
    private int cardsInHand = 0;
    private AttackDeck attack;
    private DiscardPiles attackDiscard;
    private DiscardPiles healthDiscard;
    private Health health;
    private String Class;
    public Player(String choice)
    {
        Class = choice;
        hand = new ArrayList<>(3);
        attack = new AttackDeck();
        attackDiscard = new DiscardPiles("Attack");
        healthDiscard= new DiscardPiles ("Health");
        health = new Health();
        for(int i=0;i<3;i++)
            hand.add(0);

        // for(Integer num:hand)
        // System.out.println(num);
    }
    //draws cards from the attack deck and puts them into the player's
    //hand
    public void drawCard()
    {
        int index=0;
        int [] drawnCards = attack.drawCards(3-cardsInHand);
        for(int i=0;i<hand.size();i++)
        {
            if(hand.get(i)==0) {
                hand.set(i, drawnCards[index]);
                index++;
                cardsInHand++;
            }

        }
        if(attack.cardsRemaining()==0)
            attack.addCards(attackDiscard.removeCards());
    }
    //returns hand so that the GUI can display it later
    public  ArrayList<Integer> getHand()
    {
        return hand;
    }

    //This is the special option that allows a player to discard their whole
    //hand and draw only one card from the attack deck
    //Still WIP
    public void discardCards()
    {
        int[] discardedCards= new int[3];
        for(int i=0;i<hand.size();i++)
        {
            discardedCards[i]=hand.get(i);
            hand.set(i,0);
        }
        attackDiscard.addCards(discardedCards);
    }
    //This method is for doing an attack
    public void attack(int index)
    {
        cardsInHand--;
        int cardUsed = hand.get(index);
        attackDiscard.addCards(cardUsed);
        hand.set(index,0);
        
    }
    //This method will call in the take damage method from the health deck
    public void takeDamage(int cardNumber)
    {
        health.takeDamage(cardNumber);
        boolean death = health.isDead(healthDiscard.getDiscardPile());
    }
    //this method is the same as doing an attack except that it does
    //not kill the monster
    public int defend(int index)
    {   cardsInHand--;
        int cardUsed = hand.get(index);
        hand.set(index,0);
        return cardUsed;
    }
    public boolean isDead()
    {
        return health.isDead(healthDiscard.getDiscardPile());
    }
    public int getHealth()
    {
        return health.getHealth();
    }

}
