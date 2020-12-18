package com.pier.tc.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/excel")
public class ExcelController {
	
	private static final Logger logger=LoggerFactory.getLogger(ExcelController.class);
	
	@Resource(name="excelService")
	private ExcelService excelService;
	
    @RequestMapping(value = "/adminPage.do", method = RequestMethod.GET)
    public String serviceMngForm(Model model) {
        logger.info(this.getClass().getName()+".adminPage.do");

        return "admin";
    }
    
    //버튼 클릭 페이지로 이동.
    @RequestMapping(value = "/textUploadPage.do", method = RequestMethod.GET)
    public String serviceMngForm2(Model model) {
        logger.info(this.getClass().getName()+".textUploadPage.do");

        return "uploadText";
    }
    
    
  //text데이터 넣고 등록버튼 클릭.
    @RequestMapping(value = "/textUpload.do", method = RequestMethod.GET)
    public String textUpload(){
        logger.info(this.getClass().getName()+".textUpload.do");
        
        //시나리오 내용 담을 리스트.
        List<String> scenarioList=excelService.readTextFile();

        //텍스트 파일을 엑셀 형식에 맞게 분류
        List<ScenarioVO> svList=excelService.readCommand(scenarioList);
            
        //엑셀 생성.
        excelService.writeDataInExcel(svList);
        return "admin";
    }
    
}
