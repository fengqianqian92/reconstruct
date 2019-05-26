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
        int countCharacter = 0;
        ArrayList<String> segmentList = new ArrayList<String>();

        while ((bufferLine = bufferReader.readLine()) != null) {
            if (bufferLine.indexOf('>') < 0) {
                if (! bufferLine.trim().matches("[ACGT]+")) {
                    return "Unexpected character found.";
                }
                countCharacter = countCharacter + bufferLine.trim().length();
                bufferStr = bufferStr + bufferLine.trim();
            } else {
                if (! "".equals(bufferStr)) {
                    if (countCharacter > 1000) {
                        return "Sequence length exceeds 1000 characters!";
                    }
                    segmentList.add(bufferStr);
                    countCharacter = 0;
                    bufferStr = "";
                }
            }
        }
        bufferReader.close();

        if (! "".equals(bufferStr)) {
            segmentList.add(bufferStr);
        }
        if (countCharacter > 1000) {
            return "Sequence length exceeds 1000 characters!";
        }

        if (segmentList.size() > 50) {
            return "Number of sequences exceeds 50!";
        }

        String resString = chromosomeOperationService.getChromosomeBySegmentList(segmentList);
        if (resString == null) {
            return "Error! We cannot get chromosome from your sequence file";
        }
        else {
            return resString;
        }
    }
}