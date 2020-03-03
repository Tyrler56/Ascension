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
    private ArrayList <Integer>attackDeck;
    private int cardsLeft;
    public AttackDeck()
    {
        cardsLeft=12;
        attackDeck = new ArrayList<>();
        int [] tempDeck = new int [13];
        for(int i=0;i<13;i++)
            attackDeck.add(i+1);
        Collections.shuffle(attackDeck);
        //for(int i =0;i<tempDeck.length;i++)
          //System.out.print(tempDeck[i]+" ");
    }

    /*This method is used at the start of each turn to draw the
     * necessary amount cards. The numOfCards parameter will be sent from
     * the player class
     */
    public int[] drawCards(int numOfCard)
    {
        int [] drawnCards = new int [numOfCard];
        int count=0;
        for(int i=cardsLeft;i>0;i--)
        {
            if(count==numOfCard)
                break;
            if(attackDeck.get(i)!=0)
            {
                drawnCards[count]=attackDeck.get(i);
               // System.out.println(drawnCards[count]);
                attackDeck.remove(i);
                count++;
                cardsLeft--;

            }
        }
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
            if(pile[i]!=0)
            attackDeck.add(pile[i]);
        for(int i=0;i<pile.length;i++)
            if(pile[i]!=0)
                cardsLeft++;

    }

    public int cardsRemaining()
    {
        return cardsLeft;
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
