package com.demo.chromosome.controller;

import com.demo.chromosome.service.ChromosomeOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class ChromosomeController {
    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }

    @Autowired
    ChromosomeOperationService chromosomeOperationService;

    /**
     *
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        ArrayList<String> strArray = new ArrayList<String>();
        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                System.out.println(fileName);

                File tempFile = File.createTempFile("tmp", null);
                file.transferTo(tempFile);
                BufferedReader in = new BufferedReader(new FileReader(tempFile));

                strArray.clear();

                String line;
                String tempStr = "";
                boolean isFirst = true;

                while((line=in.readLine())!=null)
                {
                    if (line.startsWith(">")) {
                        if (isFirst) {
                            isFirst = false;
                            continue;
                        }
                        strArray.add(tempStr);
                        tempStr = "";
                    }
                    else {
                        tempStr += line;
                    }
                }

                if (tempStr.length() > 0) {
                    strArray.add(tempStr);
                }

                System.out.println(strArray.size());
                for(int i = 0;i < strArray.size(); i ++){
                    System.out.println(strArray.get(i));
                }

                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "Upload failed," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Upload failed," + e.getMessage();
            }

            return chromosomeOperationService.getChromosomeBySegmentList(strArray);

        } else {
            return "Upload failed, file empty error.";
        }
    }
}