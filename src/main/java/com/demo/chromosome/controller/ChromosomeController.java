package com.demo.chromosome.controller;

import com.demo.chromosome.service.ChromosomeOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class ChromosomeController {

    @Autowired
    ChromosomeOperationService chromosomeOperationService;

    /**
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
        String bufferLine = null;
        String bufferStr = "";
        ArrayList<String> segmentList = new ArrayList<String>();
        while ((bufferLine = bufferReader.readLine()) != null) {
            if (bufferLine.indexOf('>') < 0) {
                bufferStr = bufferStr + bufferLine.trim();
            } else {
                if (bufferStr != "") {
                    segmentList.add(bufferStr);
                    bufferStr = "";
                }
            }
        }
        if (bufferStr != "") {
            segmentList.add(bufferStr);
        }
        bufferReader.close();
        return chromosomeOperationService.getChromosomeBySegmentList(segmentList);
    }
}