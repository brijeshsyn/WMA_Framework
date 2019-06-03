package com.wma.framework.report;

public class ReportIteration {
	private int iterationNo;
	private String timeTaken;
	private int numberOfTestExecuted;
	private int numberOfTestPassed;
	private int numberOfTestedFailed;
	private int numberOfTestFailed;
	private String systemName;
	
	public int getIterationNo() {
		return iterationNo;
	}
	public void setIterationNo(int iterationNo) {
		this.iterationNo = iterationNo;
	}
	public String getTimeTaken() {
		return timeTaken;
	}
	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}
	public int getNumberOfTestExecuted() {
		return numberOfTestExecuted;
	}
	public void setNumberOfTestsExecuted(int numberOfTestsExecuted) {
		this.numberOfTestExecuted = numberOfTestsExecuted;
	}
	public int getNumberOfTestsPassed() {
		return numberOfTestPassed;
	}
	public void setNumberOfTestsPassed(int numberOfTestsPassed) {
		this.numberOfTestPassed = numberOfTestsPassed;
	}
	public int getNumberOfTestsFailed() {
		return numberOfTestFailed;
	}
	public void setNumberOfTestsFailed(int numberOfTestsFailed) {
		this.numberOfTestsFailed = numberOfTestsFailed;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	 
	public String[] toStringArray() {
		String[] array = { Integer.toString(iterationNo),
				           timeTaken,
				           Integer.toString(numberOfTestExecuted),
				           Integer.toString(numberOfTestPassed),
				           Integer.toString(numberOfTestFailed),
				           systemName
		                 };
		return array;
	}
	public String toString() {
		return iterationNo + "," + numberOfTestExecuted + "," + numberOfTestPassed + "," + numberOfTestFailed + "," + systemName;
		}
	}
	