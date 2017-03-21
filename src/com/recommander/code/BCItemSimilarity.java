package com.recommander.code;



import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.similarity.AbstractItemSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;

public class BCItemSimilarity extends AbstractItemSimilarity {
    
    protected BCItemSimilarity(DataModel dataModel) {
        super(dataModel);
        //this.dataModel = super.getDataModel();
    }
    

    @Override
    public double[] itemSimilarities(long arg0, long[] arg1) throws TasteException {
        
        return null;
    }

    @Override
    public double itemSimilarity(long Item1, long Item2) throws TasteException {
        DataModel dataModel = getDataModel();
        PreferenceArray Item1Pref = dataModel.getPreferencesForItem(Item1);
        PreferenceArray Item2Pref = dataModel.getPreferencesForItem(Item2);
        
        long totalItem1Ratings = Item1Pref.length();
        long totalItem2Ratings = Item2Pref.length();
        
        int ratingsItem1[] = new int[6];
        int ratingsItem2[] = new int[6];
        double result = 0;
        for(Preference p : Item1Pref){
            ratingsItem1[(int)p.getValue()]++;
        }
        
        for(Preference p : Item2Pref){
            ratingsItem2[(int)p.getValue()]++;
        }
        for(int i = 1; i < 6; i++){
            //System.out.println(ratingsItem1[i]+" "+totalItem1Ratings+" "+ratingsItem2[i]+" "+totalItem2Ratings);
            result += Math.sqrt(((ratingsItem1[i]*1.0)/totalItem1Ratings) *  ((ratingsItem2[i]*1.0)/totalItem2Ratings));
        }
        return result;
    }
    
    
}
