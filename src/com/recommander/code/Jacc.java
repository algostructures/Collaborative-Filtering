package com.recommander.code;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.model.DataModel;


public class Jacc {
    DataModel dm;
    Jacc(DataModel dm){
        this.dm = dm;
    }
    public double getJaccardSimilarity(long U, long V) throws TasteException{
        FastIDSet fs_u = dm.getItemIDsFromUser(U);
        FastIDSet fs_v = dm.getItemIDsFromUser(V);
        long intersection = fs_u.intersectionSize(fs_v);
        fs_v.addAll(fs_u);
        long union= fs_v.size();
        return (intersection*1.0)/union;
    }
}
