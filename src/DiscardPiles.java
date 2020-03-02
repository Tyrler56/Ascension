import java.util.*;
/**
 * This is the discardpile class for all the other decks, this will mostly take care of 
 * shuffling itself before being shuffled back into the other decks, it will also use the 
 * methods of other classes to check wheter its cards need to be shuffled back into the 
 * appropirate deck. This class will have different constructors depending on the type of 
 * deck and gameplay chosen
 * @author (Benito Moreno-Garza & Grayson Drinkard)
 */
public class DiscardPiles
{
    //These are the private instance variables that will be the different disacrds only one
    //of them will be initialized depending on what was chosen from the constructor
    private int [] monsterDiscard;
    private int [] healthDiscard;
    private int [] attackDiscard;
    private String type;
    private Random random;
    private int numOfCards;
    public DiscardPiles(String pile)
    {
        numOfCards=0;
        if(pile.equals("Monster"))
        {
            monsterDiscard= new int [48];
            type = pile;
        }
        if(pile.equals("Attack"))
        {
            attackDiscard = new int[26];
            type = pile;
        }
        if(pile.equals("Health"))
        {
            healthDiscard = new int[13];
            type = pile;
        }
        random = new Random();
    }
    //adds cards that have been discarded to the discard pile
    //this methods accepts a array parameter
    public void addCards(int [] card)
    {
        if(type.equals("Monster"))
        {
            for(int i=0;i<card.length;i++){
                numOfCards++;
                monsterDiscard[numOfCards-1]=card[i];                 
            }
        }
        if(type.equals("Attack"))
        {
            for(int i=0;i<card.length;i++){
                numOfCards++;
                attackDiscard[numOfCards-1]=card[i];
            }
        }
        if(type.equals("Health"))
        {
            for(int i=0;i<card.length;i++){
                numOfCards++;
                healthDiscard[numOfCards-1]=card[i];                
            }
        }
    }
    //adds cards that have been discarded to the discard pile
    //this methods accepts a int parameter
    public void addCards(int card)
    {
        if(type.equals("Monster"))
        {
            numOfCards++;
            monsterDiscard[numOfCards-1]=card;                 
        }
        if(type.equals("Attack"))
        {          
            numOfCards++;
            attackDiscard[numOfCards-1]=card;       
        }
        if(type.equals("Health"))
        {
            numOfCards++;
            healthDiscard[numOfCards-1]=card;                
        }
    }
    //removes all the cards from the discard pile in order to shuffle
    //them back into the attack/health deck
    public int [] removeCards()
    {
        int [] copy;
        if(type.equals("Monster"))
        {
            copy= new int [48];
            for(int i=0;i<monsterDiscard.length;i++){
                copy[i]=monsterDiscard[i]; 
                monsterDiscard[i]=0;
            }
            numOfCards=0;
            return copy;
        }
        if(type.equals("Attack"))
        {

            int count =0;
            for(int i=0;i<attackDiscard.length;i++)
            {
                if(attackDiscard[i]!=0)
                    count++;           
            }
            copy= new int[count];
            int index=0;
            for(int i=0;i<copy.length;i++){
                if(attackDiscard[i]!=0) {
                    copy[index] = attackDiscard[i];
                    index++;
                    attackDiscard[i]=0;
                }

            }
            shuffleIntArray(copy);
            numOfCards=0;
            for(int i=0;i<copy.length;i++)
                System.out.print(copy[i]+" ");
            return copy;
        }
        if(type.equals("Health"))
        {
            int count =0;
            for(int i=0;i<healthDiscard.length;i++)
            {
                if(healthDiscard[i]!=0)
                    count++;           
            }
            copy= new int[count];
            for(int i=0;i<copy.length;i++){
                copy[i]=healthDiscard[i];
                healthDiscard[i]=0;
            }
            numOfCards=0;
            return copy;
        }
        return null;
    }
    public int[] getDiscardPile()
    {        
        if(type.equals("Monster"))
        {
            return monsterDiscard;
        }
        if(type.equals("Attack"))
        {            
            return attackDiscard;
        }
        if(type.equals("Health"))
        {  
            return healthDiscard;
        }
        return null;
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
