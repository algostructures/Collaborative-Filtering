package com.recommander.code;

import java.util.Collection;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.PreferenceInferrer;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * The BCFJMSDSimilarity class represents an object, 
 * which uses weighted(BCF) User_Similarity, Jaccard_Similarity 
 * and MSD_Similarity.
 * @author Saurabh
 */
public class BCFJMSDSimilarity implements UserSimilarity{
    
    static Table<Long, Long, Double> table = HashBasedTable.create();
    DataModel dm;
    
    
    /**
     * Parameterized constructor.
     * @param DataModel datamodel for the recommendations.   
     */
    BCFJMSDSimilarity(DataModel dm){
        this.dm = dm;
    }

  
    
    /**
     * @param long userId 
     * @param long userId
     * @return double similarity(100*User_Similarity+((JaccardSimilarity * MSDSimilarity))
     **/ 
    private double getSimilarity(long U, long V) throws TasteException{
        
        User_Similarity us = new User_Similarity(dm);
        MSDSimilarity ms = new MSDSimilarity(dm);
        JaccardSimilarity js = new JaccardSimilarity(dm);
        
        return 100*us.similarity(U, V)+(ms.getSimilarity(U, V)*js.getSimilarity(U, V)); 
    }


    @Override
    public void refresh(Collection<Refreshable> arg0) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void setPreferenceInferrer(PreferenceInferrer arg0) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public double userSimilarity(long U, long V) throws TasteException {
        if(table.contains(U, V)){
            return table.get(U, V);
        }
        if(table.contains(V, U)){
            return table.get(V, U);
        }
        double answer = getSimilarity(U,V);
        table.put(U, V, answer);
        table.put(V, U, answer);
        return answer;
    }
}
