package com.demo.chromosome.service.impl;

import com.demo.chromosome.service.ChromosomeOperationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ChromosomeOperationServiceImplTest {

    @Autowired
    ChromosomeOperationService chromosomeOperationService;


    @Test
    public void testGetChromosomeBySegmentList() {
        ArrayList<String> testSegmentList = new ArrayList<>();
        testSegmentList.add("ATTAGACCTG");
        testSegmentList.add("CCTGCCGGAA");
        testSegmentList.add("AGACCTGCCG");
        testSegmentList.add("GCCGGAATAC");

        String result = chromosomeOperationService.getChromosomeBySegmentList(testSegmentList);
        Assert.assertEquals(result, "ATTAGACCTGCCGGAATAC");

    }
}
