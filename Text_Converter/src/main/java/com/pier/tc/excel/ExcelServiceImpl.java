package com.pier.tc.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.*;

import org.springframework.stereotype.Service;

import com.pier.tc.cmmn.CommonConstants;

@Service("excelService")
public class ExcelServiceImpl implements ExcelService{

	private enum ReadState{
		READING,WRITE_CGTEXT,FINISH
	}
	private ReadState readState;
	
	//텍스트 파일 읽어서 한줄씩 String으로 넣어 리스트로 변환.
	@Override
	public List<String> readTextFile() {
		
        List<String> scenarioList=new ArrayList<>();

		// textFile 위치.
		String dir = CommonConstants.FILE_DIRECTORY;
		File[] files = new File(dir).listFiles();
		List<String> filedir = new ArrayList<>();
		
		//경로상에 있는 파일들 읽어오기.
		for(int i=0; i<files.length; i++) {
			filedir.add(files[i].toString());
		} 

		//읽어온 파일 내부를 인코딩후 리스트에 삽입.
		for(int i=0; i<filedir.size(); i++) {	
            try {
				FileInputStream input=new FileInputStream(filedir.get(i));
				InputStreamReader reader=new InputStreamReader(input,"UTF-8");
	            BufferedReader bufReader = new BufferedReader(reader);
	            
	            //text파일 읽어오기.
	            String line = "";
	            while((line = bufReader.readLine()) != null){
	            	scenarioList.add(line);
	            }
	           
            
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
    	return scenarioList;
	}
	
	//text List를 엑셀형식에 맞게 변환.
	@Override
	public List<ScenarioVO> readCommand(List<String> scenarioList) {
		//임시.
		String[] tempArr=new String[10];
		
		//반환할 리스트.
        List<ScenarioVO> svList=new ArrayList<>();
		readState=ReadState.READING;

        ScenarioVO tempVO=new ScenarioVO();
        //읽어온 데이터 라벨에 맞게 분류하기.
        for(String text:scenarioList)
        {
        	//끝났다면 초기화.
        	if(readState==ReadState.FINISH)
        	{
        		readState=ReadState.READING;
        	}
        	
    		System.out.println("nowText: "+text);
        	//페이지 바꿈.
        	if(text.contains("@enter"))
        	{
        		if(readState==ReadState.WRITE_CGTEXT)
        		{
        			svList.add(tempVO);
        			tempVO=new ScenarioVO();
        			tempVO.setPageCtrl("Input");
        		}
            	readState=ReadState.FINISH;
        	}
        	//cg 표시.
        	else if(text.contains("@cg"))
        	{
        		tempArr=text.split("/");
        		tempVO.setCommand("SendMessageByName");
        		tempVO.setArg1("SendMessageSystem");
        		tempVO.setArg2("SetEventCG");
        		tempVO.setArg3(tempArr[1]);
        		
        		//다음줄에 PageCtrl-Input넣기.
            	svList.add(tempVO);
        		tempVO=new ScenarioVO();
        		tempVO.setPageCtrl("Input");
        		
            	readState=ReadState.FINISH;
        	}
        	//선택지.
        	else if(text.contains("@selectionContent"))
        	{
        		tempArr=text.split("/");
        		tempVO.setCommand("Selection");
        		tempVO.setArg1(tempArr[2]);		//이동할 라벨정보.
        		tempVO.addText(tempArr[1]);		//선택지에 띄울 텍스트.
            	readState=ReadState.FINISH;
        	}
        	//선택지 제목.
        	else if(text.contains("@selectionTitle"))
        	{
        		//선택지 들어가기 전에 param설정(일단 애매해서 패스).
        		
        		//선택지 제목.
        		tempArr=text.split("/");
        		tempVO.setCommand("SendMessageByName");
        		tempVO.setArg1("SendMessageSystem");
        		tempVO.setArg2("SetSelectionTitle");
        		tempVO.setArg3(tempArr[1]);		//선택지 제목.
            	readState=ReadState.FINISH;
        	}
        	//라벨.
        	else if(text.contains("@*"))
        	{
        		text=text.replace("@", "");
        		tempVO.setCommand(text);
            	readState=ReadState.FINISH;
        	}
        	//점프.
        	else if(text.contains("@jump"))
        	{
        		tempArr=text.split("/");
        		tempVO.setCommand("Jump");
        		tempVO.setArg1(tempArr[1]);		//점프할 목적지 라벨.
            	readState=ReadState.FINISH;
        	}
        	//cg와 Text 같이있는 페이지.
        	else if(text.contains("@textCg")) 
        	{
        		tempArr=text.split("/");
        		tempVO.setCommand("SendMessageByName");
        		tempVO.setArg1("SendMessageSystem");
        		tempVO.setArg2("SetCGText");
        		tempVO.setArg3(tempArr[1]);		//이미지 명.
        		readState=ReadState.WRITE_CGTEXT;
        		continue;
        	}
        	//bgm on
        	else if(text.contains("@bgm")) {
        		tempArr=text.split("/");
        		tempVO.setCommand("Bgm");
        		tempVO.setArg1(tempArr[1]);		//노래 제목.
        		tempVO.setArg6("1");		//페이드 인 시간.
            	readState=ReadState.FINISH;
        	}
        	//bgm off
        	else if(text.contains("@stopBgm"))
        	{
        		tempArr=text.split("/");
        		tempVO.setCommand("StopBgm");
        		tempVO.setArg6("1");
            	readState=ReadState.FINISH;
        	}
        	//공백이 아닌 텍스트.
        	else if(text!="")
        	{
        		tempVO.addText(text);
        		tempVO.addText("\n");
        		continue;
        	}
        	//공백일때.
        	else
        	{
        		tempVO.addText("\n");
        		continue;
        	}
        	
        	//finish이면 vo초기화.
        	if(readState==ReadState.FINISH)
        	{
            	svList.add(tempVO);
        		tempVO=new ScenarioVO();
        	}
        	
        }
        return svList;
	}

	//엑셀을 만들고 데이터 삽입.
	@Override
	public void writeDataInExcel(List<ScenarioVO> scenarioVOList) {
		
		ScenarioVO tempVO;
		XSSFSheet workSheet=null;
		XSSFRow row=null;
		XSSFWorkbook workbook=new XSSFWorkbook();
		workSheet=workbook.createSheet("ScenarioFile");
		row=workSheet.createRow(0);
		
		//첫 행 항목설정.
		row.createCell(0).setCellValue("Command");
		row.createCell(1).setCellValue("Arg1");
		row.createCell(2).setCellValue("Arg2");
		row.createCell(3).setCellValue("Arg3");
		row.createCell(4).setCellValue("Arg4");
		row.createCell(5).setCellValue("Arg5");
		row.createCell(6).setCellValue("Arg6");
		row.createCell(7).setCellValue("WaitType");
		row.createCell(8).setCellValue("Text");
		row.createCell(9).setCellValue("PageCtrl");
		row.createCell(10).setCellValue("Voice");
		row.createCell(11).setCellValue("WindowType");
		
		//각 행 셀 데이터 삽입.
		for(int i=0;i<scenarioVOList.size();i++)
		{
			row=workSheet.createRow(i+1);
			tempVO=scenarioVOList.get(i);
			//command
			if(tempVO.getCommand()!=null)
			{
				row.createCell(0).setCellValue(tempVO.getCommand());
				
				//set arg1
				if(tempVO.getArg1()!=null)
				{
					row.createCell(1).setCellValue(tempVO.getArg1());
				}
				//set arg2
				if(tempVO.getArg2()!=null)
				{
					row.createCell(2).setCellValue(tempVO.getArg2());
				}
				//set arg3
				if(tempVO.getArg3()!=null)
				{
					row.createCell(3).setCellValue(tempVO.getArg3());
				}
				//set arg6(bgm)
				if(tempVO.getArg6()!=null)
				{
					row.createCell(6).setCellValue(tempVO.getArg6());
				}
			}
			
			if(tempVO.getText()!=null)
			{
				row.createCell(8).setCellValue(tempVO.getText());
			}
			
			if(tempVO.getPageCtrl()!=null)
			{
				row.createCell(9).setCellValue(tempVO.getPageCtrl());
			}
			
			/*
			//셀 초기화.
			for(int k=0;k<10;k++)
			{
				if(row.getCell(k)==null)
				{
					row.createCell(k).setCellValue("");
				}
			}
			*/
		}
		
		//파일 생성.
		try {
			FileOutputStream fileOut=new FileOutputStream(CommonConstants.EXCEL_CREATE_DIRECTORY);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
