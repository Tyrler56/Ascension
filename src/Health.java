import java.util.*;
/**
 * This class is in charge of the health deck or health for short it takes care of all 
 * interactions that involve health i.e. taking damage, adding health, and checking if 
 * dead.
 * @author (Benito Moreno-Garza & Grayson Drinkard)
 */
public class Health
{
    private int [] healthDeck;
    private int cardsRemaining;
    public Health()
    {
        //cardsRemaining is basically how much health is left but it doesnt
        //use the card values;
        cardsRemaining=12;
        healthDeck = new int[13];
        //initiates healthDeck and adds fourteen to the health card values for
        //simplicity since the gui can just convert them to the normal cards, 
        //but different suits
        // an ace is 13 and a "2" is 1 well in this class it would be 26
        for(int i=0;i<healthDeck.length;i++)
            healthDeck[i]=i+14;
        // for(int i=0;i<healthDeck.length;i++)
        // System.out.println(healthDeck[i]);   
    }

    public int [] takeDamage(int card)
    {
        //this array will be used for the other classes when adding to the 
        //attack discard pile
        int [] damageTaken = new int [2];
        if(card>10)
        {//if a face cards hits

            damageTaken[0]=healthDeck[cardsRemaining];
            healthDeck[cardsRemaining]=0;
            cardsRemaining--;
            if(cardsRemaining==0)
                return damageTaken;
            damageTaken[1]=healthDeck[cardsRemaining];
            healthDeck[cardsRemaining]=0;
            cardsRemaining--;
        }else if(card>0&&card<11)
        {//if any other card hits
            damageTaken[0]=healthDeck[cardsRemaining];
            cardsRemaining--;
        }
        return damageTaken;
    }
    //checks to see if the player is dead
    //a player is dead when both the wound discard pile and health decks are empty
    //this method will first check the healthdeck to see if its empty
    //then it will check if the discardPile is empty
    //if it is not empty then the addhealth method will be called and return false
    //if it is empty then the player is dead and it will return true
    public boolean isDead(int [] healthDiscardPile)
    {
        if(cardsRemaining==0)
        {
            if(healthDiscardPile[0]==0)
                return true;
            else
            {
                addHealth(healthDiscardPile);
                return false;
            }
        }
        return false;
    }
    //will add the discard pile in essence when called since health is only
    //added when all the cards in the health pile are gone
    public void addHealth(int [] discardPile)
    {
        int cards =0;
        for(int i=0;i<discardPile.length;i++)
        {
            healthDeck[i]=discardPile[i];
            if(discardPile[i]!=0)
                cards++;
        }
        cardsRemaining+=cards;
    }
    //returns the amount of health left, will most likely used by gui for displaying health.
    public int getHealth()
    {
        return cardsRemaining;
    }

    public int [] getHealthArray()
    {
        return healthDeck;
    }
}
