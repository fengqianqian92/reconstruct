package com.demo.chromosome.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.demo.chromosome.service.ChromosomeOperationService;

import org.springframework.stereotype.Service;

@Service
public class ChromosomeOperationServiceImpl implements ChromosomeOperationService {

    private String getPairIntersection(String firstString, String secondString) {
        for (int strIdx=Math.max(0, firstString.length()-secondString.length()); strIdx<firstString.length(); strIdx++) {
            if  ((firstString.length()-strIdx)<firstString.length()/2 && (firstString.length()-strIdx)<secondString.length()/2) {
                break;
            }
            if (secondString.startsWith(firstString.substring(strIdx))) {
                return firstString.substring(0, strIdx)+secondString;
            }
        }
        for (int strIdx=Math.max(0, secondString.length()-firstString.length()); strIdx<secondString.length(); strIdx++) {
            if ((secondString.length()-strIdx)<firstString.length()/2 && (secondString.length()-strIdx)<secondString.length()/2) {
                break;
            }
            if (firstString.startsWith(secondString.substring(strIdx))) {
                return secondString.substring(0, strIdx)+firstString;
            }
        }
        return null;
    }

    @Override
    public String getChromosomeBySegmentList(ArrayList<String> segmentList) {
        Queue<String> bufferQueue = new LinkedList<String>(segmentList);
        String bufferRes = "";
        int attemptCount = 0;
        while (!bufferQueue.isEmpty()) {
            String candString = bufferQueue.poll();
            if (bufferRes.equals("")) {
                bufferRes = candString;
                attemptCount = 0;
            }
            else {
                String candRes = getPairIntersection(bufferRes, candString);
                if (candRes!=null) {
                    bufferRes = candRes;
                    attemptCount = 0;
                }
                else {
                    bufferQueue.offer(candString);
                    attemptCount++;
                    if (attemptCount>=bufferQueue.size()) {
                        break;
                    }
                }
            }
        }
        return bufferRes;
    }

}
