package com.recommander.code;

import java.util.Collection;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.PreferenceInferrer;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;;
public class CustomSimilarity implements UserSimilarity {
    static Table<Long, Long, Double> table = HashBasedTable.create();
    DataModel dm;
    CustomSimilarity(DataModel dm){
        this.dm = dm;
    }

    
    
    
    public double getCustomSimilarity(long U, long V) throws TasteException{
        User_pretable us = new User_pretable(dm);
        Jacc js = new Jacc(dm);
        double cs = us.similarity(U, V);
        double ks = js.getJaccardSimilarity(U, V);
        //System.out.println(U+" "+V+" "+cs+" "+ks);
        return cs+ks; 
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
        double answer = getCustomSimilarity(U,V);
        table.put(U, V, answer);
        table.put(V, U, answer);
        return answer;
    }
}
