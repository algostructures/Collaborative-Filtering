package com.recommander.code;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;


public class Tester {
    public static void main(String arg[]) throws IOException, TasteException{
        DataModel model = new FileDataModel(new File("data/test1.csv"));
        
    }
}
