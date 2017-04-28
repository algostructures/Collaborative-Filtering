package com.recommander.code;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.model.DataModel;

/**
 * The JaccardSimilarity class represents an object, 
 * which uses Jaccard Index for user similarity 
 * (https://en.wikipedia.org/wiki/Jaccard_index)
 * @author Saurabh
 */
public class JaccardSimilarity {
    
    DataModel dm;

    /**
     * Parameterized constructor.
     * @param DataModel datamodel for the recommendations.   
     */
    JaccardSimilarity(DataModel dm){
        this.dm = dm;
    }
    
    /**
     * @param long UserID
     * @param long UserID
     * @return double Jaccrad Index(Intersection over Union) of users rating sets.
     */
    public double getSimilarity(long U, long V) throws TasteException{
        
        FastIDSet fs_u = dm.getItemIDsFromUser(U);
        FastIDSet fs_v = dm.getItemIDsFromUser(V);
        
        long intersection = fs_u.intersectionSize(fs_v);
        fs_v.addAll(fs_u);
        long union= fs_v.size();
        
        return (intersection*1.0)/union;
    }
}
