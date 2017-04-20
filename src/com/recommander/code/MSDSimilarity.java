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
        if(fs_u.size()==0){
            return 0;
        }
        double sum = 0.0;
        for(long k : fs_u){
            sum = sum + Math.pow((dm.getPreferenceValue(U, k)-dm.getPreferenceValue(V, k)), 2);
        }
        //System.out.println(fs_u.size());
        sum = sum/fs_u.size();
        //System.out.println(1-sum+"   "+sum);
        return (1 - sum);
    }
}
