package com.recommander.code;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.model.DataModel;


public class MSDSimilarity {
    DataModel dm;
    MSDSimilarity(DataModel dm){
        this.dm = dm;
    }
    public double getMSDSimilarity(long U, long V) throws TasteException{
        FastIDSet fs_u = dm.getItemIDsFromUser(U);
        FastIDSet fs_v = dm.getItemIDsFromUser(V);
        fs_u.retainAll(fs_v);
        double sum = 0.0;
        for(long k : fs_u){
            sum = sum + Math.pow((dm.getPreferenceValue(U, k)-dm.getPreferenceValue(V, k)), 2);
        }
        sum = sum/fs_u.size();
        return 1 - sum;
    }
}
