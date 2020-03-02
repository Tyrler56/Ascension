import java.util.*;
/**
 * MonsterDeck is going to take care of creating the deck of mosnters
 *
 * @author (Benito Moreno-Garza & Grayson Drinkard)
 * @version (a version number or a date)
 */
public class MonsterDeck
{
    private Random random = new Random();//for the shuffle method
    private int[] monsterDeck= new int[48];//Will store the whole monster deck once complete
    private int[] numberDeck = new int [36];//stores the numbers w/o the royalty and is then used later
    public MonsterDeck()
    {
        int count;
        count =1;
        //creates the numberDeck by giving number 2-10 inclusive
        for(int i=0;i<numberDeck.length;i++)
        {
            numberDeck[i]=count;
            if(count ==9)
                count=1;
            else
                count++;
        }
        //shuffle the deck of numbers
        shuffleIntArray(numberDeck);
        count = 10;
        int correction =0;
        //creates the monster deck by seting the royalty every four cards and
        //sets the cards in between from the cards in the numberDeck
        for (int i=0;i<monsterDeck.length;i++)
        {

            if(i%4==0)
            {
                monsterDeck[i]=count;
                correction++;
            }else
                monsterDeck[i]=numberDeck[i-correction];
            if(count ==12)
                count=10;
            else
                count++;
        }
    }
    //used for checking if it works
    public void debugMonster()
    {
        for(int i= 0;i<monsterDeck.length;i++)
            System.out.print(monsterDeck[i]+" ");
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
    //is going to be used by lanes to set out the monsters when they are 
    //"spawned"
    public int getMonster(int index)
    {
        int copy = monsterDeck[index];
        return copy;
    }
    public void removeMonster(int index)
    {
        monsterDeck[index-1]=0;
    }
}

