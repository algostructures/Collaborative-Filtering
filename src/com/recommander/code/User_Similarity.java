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


/**
 * The BCFJaccardSimilarity class represents an object, 
 * which uses weighted(BCF) User_Similarity and JaccardSimilarity
 * @author Saurabh
 */
public class User_Similarity {

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
    static HashMap<Long , double[]> map = new HashMap<Long, double[]>();
    static Table<Long, Long, Double> table = HashBasedTable.create();;


    /**
     * Parameterized constructor.
     * @param DataModel datamodel for the recommendations.   
     */
    User_Similarity(DataModel dm){
        User_Similarity.dm = dm;
    }
    

    /**
     * Precalculates user's average, median, 
     * standard deviation, median deviation
     * stores them in a map
     */
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


    /**
     *@param long UserID
     *@param long UserID
     *@return double UserSimilarity 
     **/
    double similarity(long uid, long current) throws TasteException{
        this.u = uid;
        this.v = current;

        if(table.contains(u, v))
            return table.get(u, v);    
        else if(table.contains(v, u))
            return table.get(v, u);

        PreferenceArray u_pre = dm.getPreferencesFromUser(uid);
        PreferenceArray v_pre = dm.getPreferencesFromUser(current);

        Bhattacharyya_Distance BCIS = new Bhattacharyya_Distance(dm);

        double[] uR = map.get(uid);
        double[] vR = map.get(current);

        ru = uR[0];
        rv = vR[0];

        sdu = uR[1];
        sdv = vR[1];

        um = uR[2];
        vm = vR[2];

        umd = uR[3];
        vmd = vR[3];

        double similarity = 0;
        for(Preference p_u : u_pre){

            HashSet<Long> neighbour_Set = BCIS.getNearestItemNeighbours(p_u.getItemID(), 3);
            for(Preference p_v : v_pre){
                if(neighbour_Set.contains(p_v.getItemID())){
                    similarity += BCIS.distance(p_u.getItemID(), p_v.getItemID()) * 
                            sim(p_u.getItemID(), p_v.getItemID());

                }
            }

        }

        table.put(u, v, similarity);
        table.put(v, u, similarity);

        return similarity;
    }

    
    /**
     * @param PreferenceArray
     * @return double standard deviation of PreferenceArray 
     **/
    private static double calculateUserdeviation(PreferenceArray pre) {
        double mean = calculateUserAverage(pre);
        double sum = 0;

        for(Preference p : pre){
            sum += Math.pow((p.getValue()-mean), 2);
        }

        return Math.sqrt((sum*1.0)/pre.length());
    }


    /**
     * @param PreferenceArray 
     * @return double median deviation of PreferenceArray
     **/
    private static double calculateMediandeviation(PreferenceArray pre) {
        double median = calculateUserMedian(pre);
        double sum = 0;

        for(Preference p : pre){
            sum += Math.pow((p.getValue()-median), 2);
        }

        return Math.sqrt(sum);
    }

    
    /**
     *@param PreferenceArray
     *@return double average of PreferenceArray
     */
    private static double calculateUserAverage(PreferenceArray pre) {
        long sum = 0;

        for(Preference p : pre){
            sum += p.getValue();
        }

        return (sum * 1.0)/pre.length();
    }
    
    
    /**
     *@param PreferenceArray
     *@return double Median of the PreferenceArray
     */
    private static double calculateUserMedian(PreferenceArray pre) {
        ArrayList<Float> ar = new ArrayList<Float>();

        for(Preference p : pre)
            ar.add(p.getValue());    

        Collections.sort(ar);

        return ar.get(ar.size()/2);
    }

    
    /**
     *@param PreferenceArray
     *@return similarity between user ratings
     */
    private double sim(long itemID1, long itemID2) throws TasteException {
        return ((dm.getPreferenceValue(u, itemID1) - this.um) * (dm.getPreferenceValue(v, itemID2) - this.vm))/(this.umd * this.vmd);
    }
}
