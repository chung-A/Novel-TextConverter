package com.pier.tc.excel;

import java.util.List;

public interface ExcelService {
	
	public List<String> readTextFile();
	
	public List<ScenarioVO> readCommand(List<String> scenarioList);
	
	public void writeDataInExcel(List<ScenarioVO> scenarioVOList);
}
