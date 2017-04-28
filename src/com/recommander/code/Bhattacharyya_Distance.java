package com.recommander.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;


/**
 * The Bhattacharyya_Distance class represents an object, 
 * which use the Bhattacharyya Distance
 * (https://en.wikipedia.org/wiki/Bhattacharyya_distance)
 * between two items.
 * 
 * @author Saurabh
 */
public class Bhattacharyya_Distance {
    
    static DataModel dataModel;
    static HashMap<Long, int[]> map1 = new HashMap<Long, int[]>();
    static HashMap<Long, HashSet<Long>> map = new HashMap<Long, HashSet<Long>>();
    
    
    /**
     * Parameterized constructor.
     * @param DataModel datamodel for the recommendations.   
     */
    
    Bhattacharyya_Distance(DataModel dm){
        dataModel = dm;
    }
    
    
    /**
     * Precalculates nearest K neighbours of every
     * item in the data model and stores them in a map.
     * @param int K nearest neighbours
     */
    public static void preCalculator(int k) throws TasteException{
        LongPrimitiveIterator itemList = dataModel.getItemIDs();
        while(itemList.hasNext()){
            
            long item = itemList.next();
            PreferenceArray itemAr = dataModel.getPreferencesForItem(item);
            int[] ratingsItem = new int[7];
            
            for(Preference p : itemAr){
                ratingsItem[(int)p.getValue()]++;
            }
            
            ratingsItem[6] = itemAr.length();
            map1.put(item, ratingsItem);
        }
        
        LongPrimitiveIterator ist = dataModel.getItemIDs();
        while(ist.hasNext()){
            long id = ist.next();
            HashSet<Long> ni = new HashSet<Long>();
            ni = nearestNeighbours(id, k);
            map.put(id, ni);
        }
    }
    
    
    /**
     * @return double Bhattacharrya distance between two items
     * @param long itemId of first item
     * @param long itemId of second item
     */
    double distance(long Item1, long Item2) throws TasteException {
        
        int ratingsItem1[] = map1.get(Item1);
        int ratingsItem2[] = map1.get(Item2);
        double result = 0;
        
        for(int i = 1; i < 6; i++){
            result += Math.sqrt(((ratingsItem1[i]*1.0)/ratingsItem1[6]) *  ((ratingsItem2[i]*1.0)/ratingsItem2[6]));
        }
        return result;
    }
    
    
    /**
     *@param long ItemID of the time for which nearest neighbours needed
     *@param int n nearest neighbours
     *@return HashSet<Long> consisting n nearest neighbours by distance
     **/
    private static HashSet<Long> nearestNeighbours(long ItemID, int N) throws TasteException{
        
        LongPrimitiveIterator Ids = dataModel.getItemIDs();
        ArrayList<ItemRating> ar = new ArrayList<ItemRating>();
        HashSet<Long> neighbour_Set = new HashSet<Long>();
        Bhattacharyya_Distance bcp = new Bhattacharyya_Distance(dataModel);
        
        while(Ids.hasNext()){
            Long current = Ids.next();
            if(ItemID != current){
                ItemRating temp = bcp.new ItemRating(current);
                temp.rating = bcp.distance(ItemID, current);
                ar.add(temp);
            }
        }
        
        Collections.sort(ar);
        
        for(int i = 0; i < N; i++){
             neighbour_Set.add(ar.get(i).itemId);
        }
        return neighbour_Set;
    }
    
    
    /**
     *@param long ItemID of the time for which nearest neighbours needed
     *@param int n nearest neighbours
     *@return HashSet<Long> consisting n nearest neighbours according to distance
     **/
    public HashSet<Long> getNearestItemNeighbours(long ItemID,int N) throws TasteException{
        return map.get(ItemID);
    }
    
    
    /**
     * The ItemRating class represents an object, 
     * which contains item Id and item rating
     * @author Saurabh
     */
    private class ItemRating implements Comparable<ItemRating>{
        long itemId;
        double rating;
        ItemRating(long itemId){
            this.itemId = itemId;
        }
        @Override
        public int compareTo(ItemRating o) {
            return Double.compare(o.rating, this.rating);
        }
        
    }
}
