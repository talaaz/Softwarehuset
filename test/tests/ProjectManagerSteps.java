package tests;

// Hadi

import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import employee.Employee;
import exceptions.ErrorMessageHolder;
import exceptions.OperationNotAllowedException;
import main.Softwarehuset;
import time.DateServer;
import time.MockWeekHolder;

public class ProjectManagerSteps {
	private Softwarehuset softwarehuset;
	private ErrorMessageHolder errorMessageHolder;
	private String activityName, projectId, pmId;
	private int startYear, startWeek, endYear, endWeek, projectStartWeekBefore, projectStartYearBefore;
	private Employee chosenEmployee, employee;
	private int expectedWorkload;
	private MockWeekHolder mockWeekHolder;

	public ProjectManagerSteps(Softwarehuset softwarehuset, ErrorMessageHolder errorMessageHolder,
			MockWeekHolder mockWeekHolder) throws Exception {
		this.softwarehuset = softwarehuset;
		this.errorMessageHolder = errorMessageHolder;
		softwarehuset.generateEmployees();
		chosenEmployee = softwarehuset.getEmployeeList().get(0);
		softwarehuset.addProjectToProjectList("TestProject5", chosenEmployee, 50, 2019);
		softwarehuset.choosePM(chosenEmployee.getEmployeeID(), "ceo",
				softwarehuset.getProjectsFromProjectList().get(0).getId());
		this.mockWeekHolder = mockWeekHolder;
	}

	// from create-a-new-activity.feature

	@Given("that the project manager provides the ID {string} for a project")
	public void thatTheProjectManagerProvidesTheIDForAProject(String string) {
		this.pmId = chosenEmployee.getEmployeeID();
		projectId = softwarehuset.getProjectsFromProjectList().get(0).getId();
		projectStartWeekBefore = softwarehuset.searchForProjectById(projectId).getStartWeek();
		projectStartYearBefore = softwarehuset.searchForProjectById(projectId).getStartYear();

	}

	@Given("provides a name {string} for the activity")
	public void provides_a_name_for_the_activity(String activityName) {
		this.activityName = activityName;
	}

	@Given("provides start week {int} of {int}")
	public void providesStartWeekOf(Integer startWeek, Integer startYear) {
		this.startWeek = startWeek;
		this.startYear = startYear;
	}

	@Given("provides end week {int} of {int}")
	public void providesEndWeekOf(Integer endWeek, Integer endYear) {
		this.endWeek = endWeek;
		this.endYear = endYear;
	}

	@Given("provides {int} hours as the expected workload for the activity")
	public void providesHoursAsTheExpectedWorkloadForTheActivity(Integer expectedWorkload) {
		this.expectedWorkload = expectedWorkload;
	}

	@When("the project manager creates an activity")
	public void the_project_manager_creates_an_activity() throws Exception {

		try {
			softwarehuset.createAct(activityName, projectId, expectedWorkload, pmId, startWeek, endWeek, startYear,
					endYear);
		} catch (OperationNotAllowedException e) {
			errorMessageHolder.setErrorMessage(e.getMessage());
		}
	}

	@Then("the system creates an activity with a consecutive number")
	public void theSystemCreatesAnActivityWithAConsecutiveNumber() {
		assertTrue(softwarehuset.getActivitiesFromActivityList(projectId).size() > 0);

	}

	@Then("sets the start time for the project")
	public void setsTheStartTimeForTheProject() {
		assertTrue(softwarehuset.searchForProjectById(projectId).getStartYear() != projectStartYearBefore
				|| softwarehuset.searchForProjectById(projectId).getStartWeek() != projectStartWeekBefore);

	}

	@Then("updates the start time for the project if the new activity's start time is earlier than the existing")
	public void updatesTheStartTimeForTheProjectIfTheNewActivitySStartTimeIsEarlierThanTheExisting() throws Exception {

		projectStartWeekBefore = softwarehuset.searchForProjectById(projectId).getStartWeek();
		projectStartYearBefore = softwarehuset.searchForProjectById(projectId).getStartYear();

		softwarehuset.createAct("test2", projectId, expectedWorkload, pmId, 37, endWeek, startYear, endYear);

		assertTrue(softwarehuset.searchForProjectById(projectId).getStartWeek() != projectStartWeekBefore
				|| softwarehuset.searchForProjectById(projectId).getStartWeek() == projectStartWeekBefore
						&& softwarehuset.searchForProjectById(projectId).getStartYear() != projectStartYearBefore);
	}

	@Given("that the project manager provides the ID {string} for a project, which has not been created")
	public void thatTheProjectManagerProvidesTheIDForAProjectWhichHasNotBeenCreated(String projectId) {
		this.pmId = chosenEmployee.getEmployeeID();
		this.projectId = projectId;
	}

	@Given("provides a name {string} for the activity, which already exists")
	public void providesANameForTheActivityWhichAlreadyExists(String activityName) throws Exception {
		softwarehuset.createAct(activityName, projectId, 1, pmId, 42, 43, 2019, 2019);
		this.activityName = activityName;
	}

	@Given("provides no name for the activity")
	public void providesNoNameForTheActivity() {
		this.activityName = null;
	}

	@Given("provides no the expected workload in hours for the activity")
	public void providesNoTheExpectedWorkloadInHoursForTheActivity() {
		expectedWorkload = 0;
	}

	@Given("that an employee without a valid ID {string} provides the ID {string} for a project")
	public void thatAnEmployeeWithoutAValidIDProvidesTheIDForAProject(String employeeId, String string) {
		projectId = softwarehuset.getProjectsFromProjectList().get(0).getId();
		this.pmId = employeeId;
	}

	// from assign_an_employee_to_activity.feature

	@Given("that a project manager provides the employee with ID {string}, who is vacant")
	public void thatAProjectManagerProvidesTheEmployeeWithIDWhoIsVacant(String string) {
		employee = softwarehuset.getEmployeeList().get(1);
		employee.getPermanentActivityList().clear();
	}

	@Given("that provides the name of the activity")
	public void thatProvidesTheNameOfTheActivity() throws Exception {
		String projectId = softwarehuset.getProjectsFromProjectList().get(0).getId();
		softwarehuset.createAct("test1", projectId, 1, chosenEmployee.getEmployeeID(), 35, 43, 2019, 2019);
		this.activityName = "test1";
	}

	@When("the project manager wants to add an employee to an activity")
	public void the_project_manager_wants_to_add_an_employee_to_an_activity() throws OperationNotAllowedException {
		this.pmId = chosenEmployee.getEmployeeID();
		try {
			softwarehuset.assignEmployeeToActivity(employee, softwarehuset.searchForPMById(pmId), activityName);
		} catch (OperationNotAllowedException e) {
			errorMessageHolder.setErrorMessage(e.getMessage());
		}
	}

	@Then("the system assigns the employee to the activity")
	public void the_system_assigns_the_employee_to_the_activity() {
		assertTrue(employee.getActivityList().size() > 0);
	}

	@When("the project manager wants to add an employee to an activity, which he is already assigned to")
	public void theProjectManagerWantsToAddAnEmployeeToAnActivityWhichHeIsAlreadyAssignedTo() {
		this.pmId = chosenEmployee.getEmployeeID();
		try {
			softwarehuset.assignEmployeeToActivity(employee, softwarehuset.searchForPMById(pmId), activityName);
			softwarehuset.assignEmployeeToActivity(employee, softwarehuset.searchForPMById(pmId), activityName);
		} catch (OperationNotAllowedException e) {
			errorMessageHolder.setErrorMessage(e.getMessage());
		}
	}

	@Given("that a project manager provides the employee with ID {string}, who is not vacant")
	public void thatAProjectManagerProvidesTheEmployeeWithIDWhoIsNotVacant(String string)
			throws OperationNotAllowedException {
		employee = softwarehuset.getEmployeeList().get(1);
		employee.getActivityList().clear();
		employee.createPermanentActivity(40, 45, 2019, 2019);
	}

	@Given("that the employee with ID {string} is already assigned to {int} activities")
	public void thatTheEmployeeWithIDIsAlreadyAssignedToActivities(String string, Integer activitiesAmount)
			throws Exception {
		this.projectId = softwarehuset.getProjectsFromProjectList().get(0).getId();
		this.pmId = chosenEmployee.getEmployeeID();
		employee = softwarehuset.getEmployeeList().get(1);

		employee.getActivityList().clear();

		softwarehuset.createAct("tester32", projectId, 30, pmId, 35, 39, 2019, 2020);
		softwarehuset.assignEmployeeToActivity(employee, softwarehuset.searchForPMById(pmId), "tester32");

		for (int i = 0; i < 18; i++) {
			softwarehuset.createAct("tester" + i, projectId, 30, pmId, 35, 39, 2019, 2019);
			softwarehuset.assignEmployeeToActivity(employee, softwarehuset.searchForPMById(pmId), "tester" + i);
		}

		softwarehuset.createAct("tester40", projectId, 30, pmId, 35, 39, 2019, 2020);
		softwarehuset.assignEmployeeToActivity(employee, softwarehuset.searchForPMById(pmId), "tester40");

		assertTrue(employee.getActivityList().size() == activitiesAmount);
	}

	@When("the project manager wants to assign the employee to another activity")
	public void theProjectManagerWantsToAssignTheEmployeeToAnotherActivity() throws Exception {
		softwarehuset.createAct("testerss", projectId, 30, pmId, 35, 39, 2019, 2019);
		try {
			softwarehuset.assignEmployeeToActivity(employee, softwarehuset.searchForPMById(pmId), "testerss");
		} catch (OperationNotAllowedException e) {
			errorMessageHolder.setErrorMessage(e.getMessage());
		}
	}

	@When("an employee without a project manager ID wants to assign the employee to another activity")
	public void anEmployeeWithoutAProjectManagerIDWantsToAssignTheEmployeeToAnotherActivity() throws Exception {
		try {
			softwarehuset.assignEmployeeToActivity(employee, softwarehuset.searchForPMById(employee.getEmployeeID()),
					activityName);
		} catch (OperationNotAllowedException e) {
			errorMessageHolder.setErrorMessage(e.getMessage());
		}
	}

	@When("the project manager wants to add an employee to an activity with deadline exceeded")
	public void theProjectManagerWantsToAddAnEmployeeToAnActivityWithDeadlineExceeded() throws Exception {
		employee.getActivityList().clear();

		int currentWeek = new DateServer().getDate().get(Calendar.WEEK_OF_YEAR);

		mockWeekHolder.advancedDateByWeeks(30);

		assertTrue(currentWeek != softwarehuset.getDate().get(Calendar.WEEK_OF_YEAR));

		try {
			softwarehuset.assignEmployeeToActivity(employee,
					softwarehuset.searchForPMById(chosenEmployee.getEmployeeID()), activityName);
		} catch (OperationNotAllowedException e) {
			errorMessageHolder.setErrorMessage(e.getMessage());
		}
	}
}
