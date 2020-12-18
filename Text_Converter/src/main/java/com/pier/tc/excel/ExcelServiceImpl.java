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
	
	//�ؽ�Ʈ ���� �о ���پ� String���� �־� ����Ʈ�� ��ȯ.
	@Override
	public List<String> readTextFile() {
		
        List<String> scenarioList=new ArrayList<>();

		// textFile ��ġ.
		String dir = CommonConstants.FILE_DIRECTORY;
		File[] files = new File(dir).listFiles();
		List<String> filedir = new ArrayList<>();
		
		//��λ� �ִ� ���ϵ� �о����.
		for(int i=0; i<files.length; i++) {
			filedir.add(files[i].toString());
		} 

		//�о�� ���� ���θ� ���ڵ��� ����Ʈ�� ����.
		for(int i=0; i<filedir.size(); i++) {	
            try {
				FileInputStream input=new FileInputStream(filedir.get(i));
				InputStreamReader reader=new InputStreamReader(input,"UTF-8");
	            BufferedReader bufReader = new BufferedReader(reader);
	            
	            //text���� �о����.
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
	
	//text List�� �������Ŀ� �°� ��ȯ.
	@Override
	public List<ScenarioVO> readCommand(List<String> scenarioList) {
		//�ӽ�.
		String[] tempArr=new String[10];
		
		//��ȯ�� ����Ʈ.
        List<ScenarioVO> svList=new ArrayList<>();
		readState=ReadState.READING;

        ScenarioVO tempVO=new ScenarioVO();
        //�о�� ������ �󺧿� �°� �з��ϱ�.
        for(String text:scenarioList)
        {
        	//�����ٸ� �ʱ�ȭ.
        	if(readState==ReadState.FINISH)
        	{
        		readState=ReadState.READING;
        	}
        	
    		System.out.println("nowText: "+text);
        	//������ �ٲ�.
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
        	//cg ǥ��.
        	else if(text.contains("@cg"))
        	{
        		tempArr=text.split("/");
        		tempVO.setCommand("SendMessageByName");
        		tempVO.setArg1("SendMessageSystem");
        		tempVO.setArg2("SetEventCG");
        		tempVO.setArg3(tempArr[1]);
        		
        		//�����ٿ� PageCtrl-Input�ֱ�.
            	svList.add(tempVO);
        		tempVO=new ScenarioVO();
        		tempVO.setPageCtrl("Input");
        		
            	readState=ReadState.FINISH;
        	}
        	//������.
        	else if(text.contains("@selectionContent"))
        	{
        		tempArr=text.split("/");
        		tempVO.setCommand("Selection");
        		tempVO.setArg1(tempArr[2]);		//�̵��� ������.
        		tempVO.addText(tempArr[1]);		//�������� ��� �ؽ�Ʈ.
            	readState=ReadState.FINISH;
        	}
        	//������ ����.
        	else if(text.contains("@selectionTitle"))
        	{
        		//������ ���� ���� param����(�ϴ� �ָ��ؼ� �н�).
        		
        		//������ ����.
        		tempArr=text.split("/");
        		tempVO.setCommand("SendMessageByName");
        		tempVO.setArg1("SendMessageSystem");
        		tempVO.setArg2("SetSelectionTitle");
        		tempVO.setArg3(tempArr[1]);		//������ ����.
            	readState=ReadState.FINISH;
        	}
        	//��.
        	else if(text.contains("@*"))
        	{
        		text=text.replace("@", "");
        		tempVO.setCommand(text);
            	readState=ReadState.FINISH;
        	}
        	//����.
        	else if(text.contains("@jump"))
        	{
        		tempArr=text.split("/");
        		tempVO.setCommand("Jump");
        		tempVO.setArg1(tempArr[1]);		//������ ������ ��.
            	readState=ReadState.FINISH;
        	}
        	//cg�� Text �����ִ� ������.
        	else if(text.contains("@textCg")) 
        	{
        		tempArr=text.split("/");
        		tempVO.setCommand("SendMessageByName");
        		tempVO.setArg1("SendMessageSystem");
        		tempVO.setArg2("SetCGText");
        		tempVO.setArg3(tempArr[1]);		//�̹��� ��.
        		readState=ReadState.WRITE_CGTEXT;
        		continue;
        	}
        	//bgm on
        	else if(text.contains("@bgm")) {
        		tempArr=text.split("/");
        		tempVO.setCommand("Bgm");
        		tempVO.setArg1(tempArr[1]);		//�뷡 ����.
        		tempVO.setArg6("1");		//���̵� �� �ð�.
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
        	//������ �ƴ� �ؽ�Ʈ.
        	else if(text!="")
        	{
        		tempVO.addText(text);
        		tempVO.addText("\n");
        		continue;
        	}
        	//�����϶�.
        	else
        	{
        		tempVO.addText("\n");
        		continue;
        	}
        	
        	//finish�̸� vo�ʱ�ȭ.
        	if(readState==ReadState.FINISH)
        	{
            	svList.add(tempVO);
        		tempVO=new ScenarioVO();
        	}
        	
        }
        return svList;
	}

	//������ ����� ������ ����.
	@Override
	public void writeDataInExcel(List<ScenarioVO> scenarioVOList) {
		
		ScenarioVO tempVO;
		XSSFSheet workSheet=null;
		XSSFRow row=null;
		XSSFWorkbook workbook=new XSSFWorkbook();
		workSheet=workbook.createSheet("ScenarioFile");
		row=workSheet.createRow(0);
		
		//ù �� �׸���.
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
		
		//�� �� �� ������ ����.
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
			//�� �ʱ�ȭ.
			for(int k=0;k<10;k++)
			{
				if(row.getCell(k)==null)
				{
					row.createCell(k).setCellValue("");
				}
			}
			*/
		}
		
		//���� ����.
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
