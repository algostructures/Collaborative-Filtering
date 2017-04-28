package com.recommander.code;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.model.DataModel;

/**
 * The MSDSimilarity class represents an object, 
 * which uses Mean Squared Differences for user similarity 
 * (https://en.wikipedia.org/wiki/Mean_squared_error) a version of it.
 * @author Saurabh
 */
public class MSDSimilarity {
    DataModel dm;
    
    /**
     * Parameterized constructor.
     * @param DataModel datamodel for the recommendations.   
     */
    MSDSimilarity(DataModel dm){
        this.dm = dm;
    }
    
    /**
     * @param long UserID
     * @param long UserID
     * @return double Mean Squared Difference between users
     */
    public double getSimilarity(long U, long V) throws TasteException{
        
        FastIDSet fs_u = dm.getItemIDsFromUser(U);
        FastIDSet fs_v = dm.getItemIDsFromUser(V);
        
        fs_u.retainAll(fs_v);
        
        if(fs_u.size()==0)
            return 0;
 
        double sum = 0.0;
        
        for(long k : fs_u){
            sum = sum + Math.pow((dm.getPreferenceValue(U, k)-dm.getPreferenceValue(V, k)), 2);
        }
        sum = sum/fs_u.size();
        return (1 - sum);
    }
}
