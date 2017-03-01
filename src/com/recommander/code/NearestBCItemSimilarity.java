package com.recommander.code;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;

public class NearestBCItemSimilarity {
    public long[] getNearestByBC(long ItemID, int N, DataModel model) throws TasteException{
        LongPrimitiveIterator Ids = model.getItemIDs();
        ArrayList<ItemRating> ar = new ArrayList<ItemRating>();
        BCItemSimilarity bs = new BCItemSimilarity(model);
        while(Ids.hasNext()){
            Long current = Ids.next();
            if(ItemID != current){
                ItemRating temp = new ItemRating(current);
                temp.rating = bs.itemSimilarity(ItemID, current);
                ar.add(temp);
            }
        }
        Collections.sort(ar);
        long list[] = new long[N];
        for(int i = 0; i < N; i++){
            list[i] = ar.get(i).itemId;
        }
        return list;
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
