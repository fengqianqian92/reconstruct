package com.demo.chromosome.service.impl;

import java.util.*;

import com.demo.chromosome.service.ChromosomeOperationService;

import org.springframework.stereotype.Service;

@Service
public class ChromosomeOperationServiceImpl implements ChromosomeOperationService {

    private String checkPairInersection(String formerString, String latterString) {
        for (int strIdx=Math.max(0, formerString.length()-latterString.length()); strIdx<formerString.length(); strIdx++) {
            if  ((formerString.length()-strIdx)<formerString.length()/2 && (formerString.length()-strIdx)<latterString.length()/2) {
                break;
            }
            if (latterString.startsWith(formerString.substring(strIdx))) {
                return latterString.substring(formerString.length()-strIdx);
            }
        }
        return null;
    }

    private ArrayList<Integer> getSegmentOrder(ArrayList<String> segmentList, Map<Integer, Map<Integer, String>> neighborMap) {
        Stack<ArrayList<Integer>> dfsStack = new Stack<ArrayList<Integer>>();
        for (int segIdx=0; segIdx<segmentList.size(); segIdx++) {
            dfsStack.clear();
            ArrayList<Integer> initiateList = new ArrayList<>();
            initiateList.add(segIdx);
            dfsStack.add(initiateList);
            while (!dfsStack.isEmpty()) {
                ArrayList<Integer> candList = dfsStack.pop();
                Integer lastEle = candList.get(candList.size()-1);
                if (neighborMap.containsKey(lastEle)) {
                    for (Integer neighborKey: neighborMap.get(lastEle).keySet()) {
                        ArrayList<Integer> tmpCandList = new ArrayList<Integer>(candList);
                        if (!candList.contains(neighborKey)) {
                            tmpCandList.add(neighborKey);
                            if (tmpCandList.size()==segmentList.size()) {
                                return tmpCandList;
                            }
                            else {
                                dfsStack.push(tmpCandList);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getChromosomeBySegmentList(ArrayList<String> segmentList) {
        Map<Integer, Map<Integer, String>> bufferMap = new HashMap<Integer, Map<Integer, String>>();
        for (int segIdx=0; segIdx<segmentList.size(); segIdx++) {
            Map<Integer, String> candNeighbor = new HashMap<Integer, String>();
            for (int travelIdx=0; travelIdx<segmentList.size(); travelIdx++) {
                if (travelIdx!=segIdx) {
                    String candRes = checkPairInersection(segmentList.get(segIdx), segmentList.get(travelIdx));
                    if (candRes!=null) {
                        candNeighbor.put(travelIdx, candRes);
                    }
                }

            }
            bufferMap.put(segIdx, candNeighbor);
        }
        ArrayList<Integer> resOrderList = getSegmentOrder(segmentList, bufferMap);
        if (resOrderList!=null && resOrderList.size()>0) {
            String resString = segmentList.get(resOrderList.get(0));
            int formerIdx = resOrderList.get(0);
            for (int resIdx=1; resIdx<resOrderList.size(); resIdx++) {
                if (bufferMap.containsKey(formerIdx)) {
                    resString = resString+bufferMap.get(formerIdx).get(resOrderList.get(resIdx));
                    formerIdx = resOrderList.get(resIdx);
                }
            }
            return resString;
        }
        return null;
    }

}
