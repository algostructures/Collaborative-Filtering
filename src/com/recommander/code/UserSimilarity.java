package com.recommander.code;

import java.util.HashSet;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;

public class UserSimilarity {
    double ru;
    double rv;
    double sdu;
    double sdv;
    long u;
    long v;
    static DataModel dm;
    
    UserSimilarity(DataModel dm){
        UserSimilarity.dm = dm;
    }
    
    public double similarity(long uid, Long current) throws TasteException{
        this.u = uid;
        this.v = current;
        PreferenceArray u_pre = dm.getPreferencesFromUser(uid);
        PreferenceArray v_pre = dm.getPreferencesFromUser(current);
        
        NearestBCItemSimilarity NBCS = new NearestBCItemSimilarity(dm);
        BCItemSimilarity BCIS = new BCItemSimilarity(dm);
        
        ru = calculateUserAverage(u_pre);
        rv = calculateUserAverage(v_pre);
        sdu = calculateUserdeviation(u_pre);
        sdv = calculateUserdeviation(v_pre);
        
        double similarity = 0;
        for(Preference p_u : u_pre){
            HashSet<Long> neighbour_Set = NBCS.getNearestByBC(p_u.getItemID(), 10);
            for(Preference p_v : v_pre){
                if(neighbour_Set.contains(p_v.getItemID())){
                    similarity += BCIS.itemSimilarity(p_u.getItemID(), p_v.getItemID()) * 
                                   sim(p_u.getItemID(), p_v.getItemID());
                }
            }
        }
        return similarity;
    }

    private double calculateUserdeviation(PreferenceArray pre) {
        double mean = calculateUserAverage(pre);
        double sum = 0;
        for(Preference p : pre){
            sum += Math.pow((p.getValue()-mean), 2);
        }
        return Math.sqrt((sum*1.0)/pre.length());
    }

    private double calculateUserAverage(PreferenceArray pre) {
        long sum = 0;
        for(Preference p : pre){
            sum += p.getValue();
        }
        return Math.sqrt((sum * 1.0)/pre.length());
    }

    private double sim(long itemID1, long itemID2) throws TasteException {
        return ((dm.getPreferenceValue(u, itemID1) - this.ru) * (dm.getPreferenceValue(u, itemID1) - this.rv))/(this.sdv * this.sdu);
    }
}
