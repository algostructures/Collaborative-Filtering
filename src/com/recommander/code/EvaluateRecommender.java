package com.recommander.code;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;


/**
 * The Tester class in main class, 
 * which evaluates the filtering algorithm
 * @author Saurabh
 */
public class EvaluateRecommender {    
    class MyRecommenderBuilder implements RecommenderBuilder{    
        @Override
        public Recommender buildRecommender(DataModel dm) throws TasteException {
            UserSimilarity similarity = new BCFJMSDSimilarity(dm);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, dm);
            return new GenericUserBasedRecommender(dm, neighborhood, similarity);//
        }
    }
    
    
    /**
     *main function for evaluation 
     */
    public static void main(String arg[]) throws IOException, TasteException, ClassNotFoundException{
        DataModel model = new FileDataModel(new File("data/movies.csv"));
        RandomUtils.useTestSeed();
        //precalculator(model);
        RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
        EvaluateRecommender ev = new EvaluateRecommender();
        RecommenderBuilder builder = ev.new MyRecommenderBuilder();
        double result = evaluator.evaluate(builder, null, model, 0.3, 1.0); 
        System.out.println(result); 
    }
    
    static void precalculator(DataModel dm) throws TasteException{
        Bhattacharyya_Distance.dataModel = dm;
        User_Similarity.dm = dm;
        User_Similarity.preCalculator();
        Bhattacharyya_Distance.preCalculator(3);
    }
}
