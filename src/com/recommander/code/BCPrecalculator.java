package com.recommander.code;

import java.util.HashMap;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;

public class BCPrecalculator {
    static DataModel dataModel;
    BCPrecalculator(DataModel dm){
        dataModel = dm;
    }
    static HashMap<Long, int[]> map = new HashMap<Long, int[]>();
    public static void preCalculator() throws TasteException{
        LongPrimitiveIterator k = dataModel.getItemIDs();
        while(k.hasNext()){
            long item = k.next();
            PreferenceArray itemAr = dataModel.getPreferencesForItem(item);
            int[] ratingsItem = new int[7];
            for(Preference p : itemAr){
                //System.out.println(p.getValue());
                ratingsItem[(int)p.getValue()]++;
            }
            ratingsItem[6] = itemAr.length();
            map.put(item, ratingsItem);
        }
    }
    
    public double itemSimilarity(long Item1, long Item2) throws TasteException {
        int ratingsItem1[] = map.get(Item1);
        int ratingsItem2[] = map.get(Item2);
        double result = 0;
        for(int i = 1; i < 6; i++){
            //System.out.println(ratingsItem1[i]+" "+totalItem1Ratings+" "+ratingsItem2[i]+" "+totalItem2Ratings);
            result += Math.sqrt(((ratingsItem1[i]*1.0)/ratingsItem1[6]) *  ((ratingsItem2[i]*1.0)/ratingsItem2[6]));
        }
        return result;
    }
}
