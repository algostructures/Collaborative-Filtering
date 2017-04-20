package com.recommander.code;

import java.util.List;
import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
//import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.google.common.collect.HashBasedTable;


public class Tester {
    public static void main(String arg[]) throws IOException, TasteException{
        DataModel dm = new FileDataModel(new File("data/movies.csv"));
        long startTime = System.currentTimeMillis();
        //BCPrecalculator bcp= new BCPrecalculator(dm);
        BCPrecalculator.dataModel = dm;
        User_pretable.dm = dm;
        User_pretable.preCalculator();
        User_pretable.table = HashBasedTable.create();
        BCPrecalculator.preCalculator();
        NearestBCItemSimilarity.model = dm;
        NearestBCItemSimilarity.NearestBCPrecalc(3);
        
        UserSimilarity similarity = new CustomSimilarity(dm);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, dm);
        
        UserBasedRecommender recommender = new GenericUserBasedRecommender(dm, neighborhood,  similarity);
        List<RecommendedItem> recommendations = recommender.recommend(2, 3);
        for (RecommendedItem recommendation : recommendations) {
          System.out.println(recommendation);
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime/1000);
    }
}
