package com.recommander.code;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;

public class NearestBCItemSimilarity {
    DataModel model;
    NearestBCItemSimilarity(DataModel model){
        this.model = model;
    }
    public HashSet<Long> getNearestByBC(long ItemID, int N) throws TasteException{
        LongPrimitiveIterator Ids = model.getItemIDs();
        ArrayList<ItemRating> ar = new ArrayList<ItemRating>();
        //BCItemSimilarity bs = new BCItemSimilarity(model);
        BCPrecalculator bcp = new BCPrecalculator(model);
        
        //System.out.println("here");
        //int count = 20;
        while(Ids.hasNext()){
            //count--;
            Long current = Ids.next();
            if(ItemID != current){
                ItemRating temp = new ItemRating(current);
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
