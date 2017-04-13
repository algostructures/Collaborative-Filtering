package com.recommander.code;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class User_pretable {
    double ru;
    double rv;
    double sdu;
    double sdv;
    long u;
    long v;
    static DataModel dm;
    static int count = 0;
    static HashMap<Long , double[]> map = new HashMap<Long, double[]>();
    static Table<Long, Long, Double> table;
    User_pretable(DataModel dm){
        User_pre.dm = dm;
    }
    
    public static void preCalculator() throws TasteException{
        LongPrimitiveIterator k = dm.getUserIDs();
        
        while(k.hasNext()){
            long user = k.next();
            
            double[] uPre = new double[2];
            
            PreferenceArray uAr = dm.getPreferencesFromUser(user);
            double avg = calculateUserAverage(uAr);
            double sdv = calculateUserdeviation(uAr);
            uPre[0] = avg;
            uPre[1] = sdv;
            map.put(user, uPre);
        }
    }
    
    public double similarity(long uid, Long current) throws TasteException{
        this.u = uid;
        this.v = current;
        if(table.contains(u, v)){
            //System.out.println("here");
            return table.get(u, v);
            
        }
        else if(table.contains(v, u)){
            return table.get(v, u);
        }
        PreferenceArray u_pre = dm.getPreferencesFromUser(uid);
        PreferenceArray v_pre = dm.getPreferencesFromUser(current);
        
        NearestBCItemSimilarity NBCS = new NearestBCItemSimilarity(dm);
        BCItemSimilarity BCIS = new BCItemSimilarity(dm);
        
        //BCPrecalculator BP = new BCPrecalculator(dm);
        
        double[] uR = map.get(uid);
        double[] vR = map.get(current);
        //System.out.println(ru);
        ru = uR[0];
        rv = vR[0];
        sdu = uR[1];
        sdv = vR[1];
        
        double similarity = 0;
        //int count = 0;
        for(Preference p_u : u_pre){
            HashSet<Long> neighbour_Set = NBCS.getNearestByBC(p_u.getItemID(), 3);
            for(Preference p_v : v_pre){
                if(neighbour_Set.contains(p_v.getItemID())){
                    //System.out.println(User_Similarity.count++);
                    similarity += BCIS.itemSimilarity(p_u.getItemID(), p_v.getItemID()) * 
                            sim(p_u.getItemID(), p_v.getItemID());
                    
                }
            }
           
        }
        table.put(u, v, similarity);
        table.put(v, u, similarity);
        
        //System.out.println(uid+" "+current);
        return similarity;
    }

    private static double calculateUserdeviation(PreferenceArray pre) {
        double mean = calculateUserAverage(pre);
        double sum = 0;
        for(Preference p : pre){
            sum += Math.pow((p.getValue()-mean), 2);
        }
        return Math.sqrt((sum*1.0)/pre.length());
    }

    private static double calculateUserAverage(PreferenceArray pre) {
        long sum = 0;
        for(Preference p : pre){
            sum += p.getValue();
        }
        return Math.sqrt((sum * 1.0)/pre.length());
    }

    private double sim(long itemID1, long itemID2) throws TasteException {
        //return 1+1;
       return ((dm.getPreferenceValue(u, itemID1) - this.ru) * (dm.getPreferenceValue(u, itemID1) - this.rv))/(this.sdv * this.sdu);
    }
}
