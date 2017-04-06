package com.recommander.code;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class EvaluateRecommender {
    class MyRecommenderBuilder implements RecommenderBuilder{
        
        @Override
        public Recommender buildRecommender(DataModel dm) throws TasteException {
            UserSimilarity similarity = new CustomSimilarity(dm);
            UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dm);
            return new GenericUserBasedRecommender(dm, neighborhood, similarity);//
            //return null;
        }
        
        
        
    }
    public static void main(String arg[]) throws IOException, TasteException{
        DataModel model = new FileDataModel(new File("data/movies.csv"));
        BCPrecalculator.dataModel = model;
        User_pre.dm = model;
        User_pre.preCalculator();
        BCPrecalculator.preCalculator();
        RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
        EvaluateRecommender ev = new EvaluateRecommender();
        RecommenderBuilder builder = ev.new MyRecommenderBuilder();
        double result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
        System.out.println(result); 
    }
    
}
