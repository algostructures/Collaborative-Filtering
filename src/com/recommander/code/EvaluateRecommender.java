package com.recommander.code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
import org.apache.mahout.common.RandomUtils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class EvaluateRecommender {
    class MyRecommenderBuilder implements RecommenderBuilder{
        
        @Override
        public Recommender buildRecommender(DataModel dm) throws TasteException {
            UserSimilarity similarity = new CustomSimilarityJMSD(dm);
            UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dm);
            return new GenericUserBasedRecommender(dm, neighborhood, similarity);//
            //return null;
        }
        
        
        
    }
    public static void main(String arg[]) throws IOException, TasteException, ClassNotFoundException{
        DataModel model = new FileDataModel(new File("data/movies.csv"));
        RandomUtils.useTestSeed();
        BCPrecalculator.dataModel = model;
        User_pretable.dm = model;
        User_pretable.preCalculator();
        BCPrecalculator.preCalculator();
        File f = new File("data/map.ser");
        
        if(f.exists() && !f.isDirectory()) { 
            //System.out.println("here");
            User_pretable.table = readFromfile();
        }
        else{
            User_pretable.table = HashBasedTable.create();
        }
        
        RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
        EvaluateRecommender ev = new EvaluateRecommender();
        RecommenderBuilder builder = ev.new MyRecommenderBuilder();
        double result = evaluator.evaluate(builder, null, model, 0.3, 1.0); 
        
        System.out.println(result); 
    }
    
    public static void write_to_file() throws IOException{
        FileOutputStream fos = new FileOutputStream("data/map.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Table<Long, Long, Double> tab = User_pretable.table;
        //System.out.println(tab.toString());
        oos.writeObject(tab);
        oos.close();
    }
    
    public static Table<Long, Long, Double> readFromfile() throws IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream("data/map.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Table<Long, Long, Double> anotherMap = (Table<Long, Long, Double>) ois.readObject();
        //System.out.println(anotherMap.toString());
        ois.close();
        return anotherMap;
    }
    
}
