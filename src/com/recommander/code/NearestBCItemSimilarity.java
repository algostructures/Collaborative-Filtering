package com.recommander.code;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;

public class NearestBCItemSimilarity {
    static DataModel model;
    
    static HashMap<Long, HashSet<Long>> map = new HashMap<Long, HashSet<Long>>();
    NearestBCItemSimilarity(DataModel model){
        NearestBCItemSimilarity.model = model;
    }
    
    public static void NearestBCPrecalc(int k) throws TasteException{
        LongPrimitiveIterator ids1 = model.getItemIDs();
        while(ids1.hasNext()){
            long id = ids1.next();
            HashSet<Long> ni = new HashSet<Long>();
            ni = NearestBCItemSimilarity.getNearestByBC(id, k);
            map.put(id, ni);
        }
    }
    public static HashSet<Long> getNearestByBC(long ItemID, int N) throws TasteException{
        LongPrimitiveIterator Ids = model.getItemIDs();
        ArrayList<ItemRating> ar = new ArrayList<ItemRating>();
        //BCItemSimilarity bs = new BCItemSimilarity(model);
        BCPrecalculator bcp = new BCPrecalculator(model);
        NearestBCItemSimilarity x = new NearestBCItemSimilarity(model);
        //System.out.println("here");
        //int count = 20;
        while(Ids.hasNext()){
            //count--;
            Long current = Ids.next();
            if(ItemID != current){
                ItemRating temp = x.new ItemRating(current);
                temp.rating = bcp.itemSimilarity(ItemID, current);
                ar.add(temp);
            }
        }
        Collections.sort(ar);
        //long list[] = new long[N];
        HashSet<Long> neighbour_Set = new HashSet<Long>();
        for(int i = 0; i < N; i++){
             neighbour_Set.add(ar.get(i).itemId);
        }
        return neighbour_Set;
    }
    
    public HashSet<Long> getNearest(long ItemID,int N) throws TasteException{
        
        return map.get(ItemID);
    }
    
    class ItemRating implements Comparable<ItemRating>{
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
