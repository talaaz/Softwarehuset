package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import employee.Employee;
import employee.PermanentActivity;
import exceptions.ErrorMessageHolder;
import exceptions.OperationNotAllowedException;
import main.Softwarehuset;

public class EmployeeSteps {
	private Softwarehuset softwarehuset;
	private String projectName;
	private ErrorMessageHolder errorMessageHolder;
	private List<Employee> employeeList;
	private Employee employee;
	private Calendar startDate, endDate;
	private int startYear, startMonth, startDay, endYear, endMonth, endDay;
	private Object permanentActivity;
	
	public EmployeeSteps(Softwarehuset softwarehuset, ErrorMessageHolder errorMessageHolder) {
		this.softwarehuset = softwarehuset;
		this.errorMessageHolder = errorMessageHolder;
		this.employeeList = softwarehuset.generateEmployees();
	}

	// from create-a-new-project.feature

	@Given("that a name {string} for the project is provided")
	public void thatANameForTheProjectIsProvided(String projectName) {
		this.projectName = projectName;
	}
	
	@When("a new project is created")
	public void a_new_project_is_created() throws Exception {
		try {
			softwarehuset.addProjectToProjectList(projectName, employeeList.get(0));
		} catch (OperationNotAllowedException e) {
			errorMessageHolder.setErrorMessage(e.getMessage());
		}
	}

	@Then("the system creates a project with the provided name {string} and a consecutive number")
	public void theSystemCreatesAProjectWithTheProvidedNameAndAConsecutiveNumber(String projectName) {
		assertTrue(softwarehuset.getProjectsFromProjectList().contains(softwarehuset.searchForProjectByName(projectName)));
	}

	@Given("that a name for the project is not provided")
	public void thatANameForTheProjectIsNotProvided() {
		projectName = "";
	}

	@Then("the system provides an error message {string}")
	public void theSystemProvidesAnErrorMessage(String errorMessage) throws Exception {
		assertEquals(errorMessage, errorMessageHolder.getErrorMessage());
	}

	
//	//from create-a-permanent-activity.feature
	
	@Given("that an employee  provides his ID {string}")
	public void thatAnEmployeeProvidesHisID(String ID) {
	    employee = employeeList.get(0);
	    ID = employee.getEmployeeID();
	    assertTrue(employeeList.contains(softwarehuset.searchForEmployeeById(ID)));
	}

	@Given("provides a start date {int}:{int}:{int} for the permanent activity")
	public void providesAStartDateForThePermanentActivity(Integer year, Integer month, Integer day) {
		startYear = year;
		startMonth = month;
		startDay = day;
//		startDate = new GregorianCalendar(year, (month-1), day);
	}

	@Given("provides an end date {int}:{int}:{int} for the permanent activity")
	public void providesAnEndDateForThePermanentActivity(Integer year, Integer month, Integer day) {
		endYear = year;
		endMonth = month;
		endDay = day;
//		endDate = new GregorianCalendar(year, (month-1), day);
	}

	@When("the employee creates a permanent activity")
	public void theEmployeeCreatesAPermanentActivity() throws OperationNotAllowedException {
		try {
			employee.createPermanentActivity(startYear, startMonth, startDay, endYear, endMonth, endDay);
		} catch(OperationNotAllowedException e) {
			errorMessageHolder.setErrorMessage(e.getMessage());
		}
	    
	}

	@Then("the system creates the permanent activity")
	public void theSystemCreatesThePermanentActivity() {
	    assertTrue(employee.getPermanentActivityList().size() > 0);
	}
	
	@Given("does not provide a start date for the permanent activity")
	public void doesNotProvideAStartDateForThePermanentActivity() {
		
	}

	@Given("does not provide an end date for the permanent activity")
	public void doesNotProvideAnEndDateForThePermanentActivity() {
		
	}


}
