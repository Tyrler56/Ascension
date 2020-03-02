import java.util.*;
/**
 * attackDeck is the deck that holds all of the attack cards
 * and all of the discarded wound/health cards. This class is also
 * responsible for supplying the player with the cards that they
 * need by sending them their DrawnCards useing the drawCards() method.
 * @author (Benito Moreno-Garza & Grayson Drinkard)
 */ 
public class AttackDeck
{
    private Random random = new Random();
    private int [] attackDeck;
    private int numOfCards;
    public AttackDeck()
    {
        numOfCards=13;
        attackDeck = new int [25];
        int [] tempDeck = new int [13];
        for(int i=0;i<13;i++)
            tempDeck[i]=i+1;
        shuffleIntArray(tempDeck);
        //for(int i =0;i<tempDeck.length;i++)
          //  System.out.println(tempDeck[i]);
        for(int i=0;i<tempDeck.length;i++)
            attackDeck[i]=tempDeck[i];
    }

    /*This method is used at the start of each turn to draw the
     * necessary amount cards. The numOfCards parameter will be sent from
     * the player class
     */
    public int[] drawCards(int numOfCard)
    {
        int [] drawnCards = new int [numOfCard];
        int count=0;
        for(int i=24;i>0;i--)
        {
            if(count==numOfCard)
                break;
            if(attackDeck[i]!=0)
            {
                drawnCards[count]=attackDeck[i];
               // System.out.println(drawnCards[count]);
                attackDeck[i]=0;
                count++;

            }
        }
        numOfCards=-numOfCard;
        return drawnCards;
    }

    /*
     * this method will add cards back into the deck. in other words
     * it will shuffle the attack discard pile back into the attack
     * deck
     */
    public void addCards(int [] pile)
    {
        for(int i=0;i<pile.length;i++)
            attackDeck[i]=pile[i];
        for(int i=0;i<pile.length;i++)
            if(pile[i]!=0)
                numOfCards++;
    }

    public int cardsRemaining()
    {
        return numOfCards;
    }

    /* Thanks Ms. Warnes
     * Shuffle Method!!
     */
    public void shuffleIntArray(int arr[]) {
        int size = arr.length;
        int swap, temp;
        for (int idx = size-1; idx > 0; idx--) {
            swap = random.nextInt(idx);
            temp = arr[swap];
            arr[swap] = arr[idx];
            arr[idx] = temp;
        }
    }
}
