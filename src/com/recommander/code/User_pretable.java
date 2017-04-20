package com.recommander.code;

import java.util.ArrayList;
import java.util.Collections;
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
    double um;
    double vm;
    double umd;
    double vmd;
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
            
            double[] uPre = new double[4];
            
            PreferenceArray uAr = dm.getPreferencesFromUser(user);
            double avg = calculateUserAverage(uAr);
            double sdv = calculateUserdeviation(uAr);
            double med = calculateUserMedian(uAr);
            double md = calculateMediandeviation(uAr);
            uPre[0] = avg;
            uPre[1] = sdv;
            uPre[2]	= med;
            uPre[3] = md;
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
        BCPrecalculator BCIS = new BCPrecalculator(dm);
        
        //BCPrecalculator BP = new BCPrecalculator(dm);
        
        double[] uR = map.get(uid);
        double[] vR = map.get(current);
        //System.out.println(ru);
        ru = uR[0];
        rv = vR[0];
        
        sdu = uR[1];
        sdv = vR[1];
        
        um = uR[2];
        vm = vR[2];
        
        umd = uR[3];
        vmd = vR[3];
        
        double similarity = 0;
        //int count = 0;
        for(Preference p_u : u_pre){
            
           HashSet<Long> neighbour_Set = NBCS.getNearest(p_u.getItemID(), 3);
            for(Preference p_v : v_pre){
                if(neighbour_Set.contains(p_v.getItemID())){
                    //System.out.println(User_Similarity.count++);
                    //System.out.println(BCIS.itemSimilarity(p_u.getItemID(), p_v.getItemID())* 
                      //      sim(p_u.getItemID(), p_v.getItemID()));
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
    
    private static double calculateMediandeviation(PreferenceArray pre) {
        double median = calculateUserMedian(pre);
        double sum = 0;
        for(Preference p : pre){
            sum += Math.pow((p.getValue()-median), 2);
        }
        return Math.sqrt(sum);
    }
    
    private static double calculateUserAverage(PreferenceArray pre) {
        long sum = 0;
        for(Preference p : pre){
            sum += p.getValue();
        }
        //System.out.println((sum*1.0)/pre.length());
        return (sum * 1.0)/pre.length();
    }
    
    private static double calculateUserMedian(PreferenceArray pre) {
        long sum = 0;
        ArrayList<Float> ar = new ArrayList<Float>();
        for(Preference p : pre){
            ar.add(p.getValue());
        }
        //System.out.println((sum*1.0)/pre.length());
        Collections.sort(ar);
        return ar.get(ar.size()/2);
    }
    
   

    private double sim(long itemID1, long itemID2) throws TasteException {
        //return 1+1;
       //System.out.println(u+" "+itemID1+" "+this.ru+" "+this.sdu);
       //System.out.println(((dm.getPreferenceValue(u, itemID1) - this.ru) * (dm.getPreferenceValue(u, itemID1) - this.rv))/(this.sdv * this.sdu));
       return ((dm.getPreferenceValue(u, itemID1) - this.um) * (dm.getPreferenceValue(v, itemID2) - this.vm))/(this.umd * this.vmd);
    }
}
