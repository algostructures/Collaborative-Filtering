package com.recommander.code;

import java.util.Collection;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.PreferenceInferrer;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;;
public class CustomSimilarity implements UserSimilarity {
    DataModel dm;
    CustomSimilarity(DataModel dm){
        this.dm = dm;
    }

    
    
    
    public double getCustomSimilarity(long U, long V) throws TasteException{
        User_Similarity us = new User_Similarity(dm);
        Jacc js = new Jacc(dm);
        return us.similarity(U, V)+js.getJaccardSimilarity(U, V); 
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
        return getCustomSimilarity(U,V);
    }
}
