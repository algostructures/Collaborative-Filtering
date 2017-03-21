package com.recommander.code;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;

public class UserNeighbours {
    DataModel dm;
    UserNeighbours(DataModel dm){
        this.dm = dm;
    }
    public long[] getClosestUsers(long uid, int k) throws TasteException{
        LongPrimitiveIterator userIds = dm.getUserIDs();
        ArrayList<User_Node> ar = new ArrayList<User_Node>();
        User_Similarity us = new User_Similarity(dm);
        while(userIds.hasNext()){
            long current = userIds.next();
            if(uid != current){
                User_Node temp = new User_Node(current);
                temp.similarity = us.similarity(uid, current);
                ar.add(temp);
            }
        }
        Collections.sort(ar);
        long[] list = new long[k];
        for(int i = 0; i < k; i++){
            list[i] = ar.get(i).userId;
        }
        return list;
    }
    
}

class User_Node implements Comparable<User_Node>{
    long userId;
    double similarity;
    User_Node(long userID){
        this.userId = userID; 
    }
    @Override
    public int compareTo(User_Node o) {
        // TODO Auto-generated method stub
        return Double.compare(o.similarity, this.similarity);
    }
    
}
