package com.recommander.code;

import java.util.List;
import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


/**
 * The Tester class in main class, 
 * which recommends items.
 * @author Saurabh
 */
public class Tester {
    
    /**
     *main function for recommend items. 
     */
    public static void main(String arg[]) throws IOException, TasteException{
        DataModel dm = new FileDataModel(new File("data/movies.csv"));
        //precalculator(dm);
        UserSimilarity similarity = new BCFJaccardSimilarity(dm);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, dm);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(dm, neighborhood,  similarity);
        
        List<RecommendedItem> recommendations = recommender.recommend(2, 3);
        
        for (RecommendedItem recommendation : recommendations) {
          System.out.println(recommendation);
        }
    }
    
    static void precalculator(DataModel dm) throws TasteException{
        Bhattacharyya_Distance.dataModel = dm;
        User_Similarity.dm = dm;
        User_Similarity.preCalculator();
        Bhattacharyya_Distance.preCalculator(3);
    }
}
