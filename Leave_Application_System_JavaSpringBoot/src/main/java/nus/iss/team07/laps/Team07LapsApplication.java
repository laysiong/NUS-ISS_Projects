package nus.iss.team07.laps;


import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jakarta.annotation.PostConstruct;
import nus.iss.team07.laps.model.ApplicationStatus;
import nus.iss.team07.laps.model.Compensation;
import nus.iss.team07.laps.model.Department;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.Holidays;
import nus.iss.team07.laps.model.LeaveCount;
import nus.iss.team07.laps.model.LeaveRecord;
import nus.iss.team07.laps.model.LeaveType;
import nus.iss.team07.laps.model.Role;
import nus.iss.team07.laps.repository.CompensationRepository;
import nus.iss.team07.laps.repository.DepartmentRepository;
import nus.iss.team07.laps.repository.EmployeeRepository;
import nus.iss.team07.laps.repository.HolidaysRepository;
import nus.iss.team07.laps.repository.LeaveCountRepository;
import nus.iss.team07.laps.repository.LeaveRecordRepository;
import nus.iss.team07.laps.repository.LeaveTypeRepository;
import nus.iss.team07.laps.repository.RoleRepository;
import nus.iss.team07.laps.service.CaculateOffDays;
import nus.iss.team07.laps.service.HolidaysImplementation;




@SpringBootApplication

public class Team07LapsApplication {

	public static void main(String[] args) {
		System.out.println("TEAM-07 NUS ISS Test Data");
		SpringApplication.run(Team07LapsApplication.class, args);
	}
	
//delete the /* scroll all way down and look for */ to uncomment
	 @Bean
	    CommandLineRunner initDatabase(
	    		HolidaysRepository holidayRepo,
	            DepartmentRepository departmentRepo,
	            RoleRepository roleRepo,
	            EmployeeRepository employeeRepo,
	            LeaveTypeRepository leaveTypeRepo,
	            CompensationRepository compensationRepo,
	            LeaveCountRepository leaveCountRepo,
	            LeaveRecordRepository leaveRecordRepo,
	            CaculateOffDays offDayService,
	            HolidaysImplementation holidayService) {
	        
	        return args -> {
	            System.out.println("---- Initializing Database with Test Data ----");

	            // Create Holidays
	            Holidays pb1 = holidayRepo.saveAndFlush(new Holidays("New Year's Day", LocalDate.parse("2024-01-01")));
	            Holidays pb2 = holidayRepo.saveAndFlush(new Holidays("Chinese New Year 1", LocalDate.parse("2024-02-10")));
	            Holidays pb3 = holidayRepo.saveAndFlush(new Holidays("Chinese New Year 2", LocalDate.parse("2024-02-11")));
	            Holidays pb4 = holidayRepo.saveAndFlush(new Holidays("Good Friday", LocalDate.parse("2024-03-29")));
	            Holidays pb5 = holidayRepo.saveAndFlush(new Holidays("Hari Raya Pusasa", LocalDate.parse("2024-04-10")));
	            Holidays pb6 = holidayRepo.saveAndFlush(new Holidays("Labour Day", LocalDate.parse("2024-05-01")));
	            Holidays pb7 = holidayRepo.saveAndFlush(new Holidays("Vesak Day", LocalDate.parse("2024-05-22")));
	            Holidays pb8 = holidayRepo.saveAndFlush(new Holidays("Hari Raya Haji", LocalDate.parse("2024-06-17")));
	            Holidays pb9 = holidayRepo.saveAndFlush(new Holidays("National Day", LocalDate.parse("2024-09-09")));
	            Holidays pb10 = holidayRepo.saveAndFlush(new Holidays("Deepavali", LocalDate.parse("2024-10-31")));
	            Holidays pb11 = holidayRepo.saveAndFlush(new Holidays("Christmas Day", LocalDate.parse("2024-12-25")));

	            List<Holidays> publicHolidays = holidayService.findAllHolidays();
	            List<LocalDate> phList = publicHolidays.stream()
	                    .map(Holidays::getStartDate)
	                    .collect(Collectors.toList());

	            // Create Departments
	            Department dep1 = departmentRepo.saveAndFlush(new Department("Sale", "85695351", "1"));
	            Department dep2 = departmentRepo.saveAndFlush(new Department("HR", "95463561", "2"));
	            Department dep3 = departmentRepo.saveAndFlush(new Department("R&D", "98756231", "1"));

	            // Create Roles
	            Role salesman = roleRepo.saveAndFlush(new Role("Saleman"));
	            Role admin = roleRepo.saveAndFlush(new Role("Admin"));
	            Role developer = roleRepo.saveAndFlush(new Role("Dev"));
	            Role manager = roleRepo.saveAndFlush(new Role("Manager"));


	            //Clean Account
	            Employee UserManager = employeeRepo.saveAndFlush(new Employee("UserManager", "uManagerB@company.com", manager, "UserManager", "123", null, "12341234", LocalDate.parse("2024-06-18"), dep3));
	            Employee UserStaff = employeeRepo.saveAndFlush(new Employee("UserStaff", "uStaffA@company.com", developer, "UserStaff", "123", UserManager, "12341234", LocalDate.parse("2024-06-18"), dep3));
	            Employee UserAdmin = employeeRepo.saveAndFlush(new Employee("UserAdmin", "uAdmin@company.com", admin, "UserAdmin", "123", UserManager, "12341234", LocalDate.parse("2024-06-18"), dep3));
	            
	            
	            // Create Employees
	            Employee emp5 = employeeRepo.saveAndFlush(new Employee("Thomas", "thomas@company.com", manager, "thomas", "123", null, "78454121", LocalDate.parse("2023-06-11"), dep1));
	            Employee emp6 = employeeRepo.saveAndFlush(new Employee("Charlie", "charlie@company.com", manager, "charlie", "123", emp5, "78454121", LocalDate.parse("2023-06-11"), dep2));
	            Employee emp1 = employeeRepo.saveAndFlush(new Employee("David", "david@company.com", admin, "david", "123", emp5, "3512145", LocalDate.parse("2022-06-12"), dep3));
	            Employee emp2 = employeeRepo.saveAndFlush(new Employee("Xia Tian", "xiatian@company.com", admin, "xiatian", "123", emp6, "12341234", LocalDate.parse("2022-06-12"), dep2));
	            Employee emp3 = employeeRepo.saveAndFlush(new Employee("Laysiong", "laysiong@hotmail.com", developer, "lays", "123", emp6, "17811781", LocalDate.parse("2023-06-12"), dep2));
	            Employee emp4 = employeeRepo.saveAndFlush(new Employee("Thiri", "thiri@company.com", developer, "thiri", "123", emp5, "78454121", LocalDate.parse("2022-06-12"), dep1));
	            Employee empt1 = employeeRepo.saveAndFlush(new Employee("Staff_A", "testorA@company.com", developer, "Staff_A", "123", emp5, "12341234", LocalDate.parse("2024-06-18"), dep3));
	            Employee empt2 = employeeRepo.saveAndFlush(new Employee("Staff_B", "testorB@company.com", developer, "Staff_B", "123", emp5, "12341234", LocalDate.parse("2024-06-18"), dep3));
	            Employee empt3 = employeeRepo.saveAndFlush(new Employee("Staff_C", "testorC@company.com", developer, "Staff_C", "123", emp5, "12341234", LocalDate.parse("2024-06-18"), dep3));
	            Employee empt4 = employeeRepo.saveAndFlush(new Employee("Staff_D", "testorE@company.com", developer, "Staff_D", "123", emp6, "12341234", LocalDate.parse("2024-06-18"), dep3));
	            Employee empt5 = employeeRepo.saveAndFlush(new Employee("Staff_E", "testorF@company.com", developer, "Staff_E", "123", emp6, "12341234", LocalDate.parse("2024-06-18"), dep3));
	            Employee empt6 = employeeRepo.saveAndFlush(new Employee("Staff_F", "testorG@company.com", developer, "Staff_F", "123", emp6, "12341234", LocalDate.parse("2024-06-18"), dep3));

	            // Create Leave Types
	            LeaveType leavetype1 = leaveTypeRepo.saveAndFlush(new LeaveType("MC", 60.0));
	            LeaveType leavetype2 = leaveTypeRepo.saveAndFlush(new LeaveType("Annual Leaves", 14.0));
	            LeaveType leavetype3 = leaveTypeRepo.saveAndFlush(new LeaveType("Compensation Leaves", 0.0));

	            // Create Compensations
	            compensationRepo.saveAndFlush(new Compensation(emp1, LocalDate.of(2024, 6, 7), 4, "OT working on PPT", "Got it", ApplicationStatus.Approved, leavetype3));
	            compensationRepo.saveAndFlush(new Compensation(emp1, LocalDate.of(2024, 6, 8), 8, "OT for project XYZ", "", ApplicationStatus.Cancel, leavetype3));
	            compensationRepo.saveAndFlush(new Compensation(emp1, LocalDate.of(2024, 6, 9), 4, "OT for project ATOZ", " ", ApplicationStatus.Deleted, leavetype3));
	            compensationRepo.saveAndFlush(new Compensation(emp1, LocalDate.of(2024, 6, 10), 6, "OT on Project beta", "Nope", ApplicationStatus.Reject, leavetype3));
	            compensationRepo.saveAndFlush(new Compensation(emp1, LocalDate.of(2024, 6, 11), 4, "OT on project zzzz", " ", ApplicationStatus.Updated, leavetype3));
	            compensationRepo.saveAndFlush(new Compensation(emp1, LocalDate.of(2024, 6, 12), 5, "OT on project 5", " ", ApplicationStatus.Applied, leavetype3));
	            compensationRepo.saveAndFlush(new Compensation(emp2, LocalDate.of(2024, 6, 13), 4, "OT working on PPT", "Got it", ApplicationStatus.Applied, leavetype3));


           
	            createLeaveCountForEmployee(leaveCountRepo, UserManager, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, UserStaff, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, UserAdmin, leavetype1, leavetype2, leavetype3);
	            // Create Leave Counts
	            createLeaveCountForEmployee(leaveCountRepo, empt1, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, empt2, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, empt3, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, empt4, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, empt5, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, empt6, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, emp1, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, emp2, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, emp3, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, emp4, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, emp5, leavetype1, leavetype2, leavetype3);
	            createLeaveCountForEmployee(leaveCountRepo, emp6, leavetype1, leavetype2, leavetype3);

	            // Create Leave Records
	            long count1 = offDayService.calculateLeaveDays(LocalDate.parse("2024-06-09"), LocalDate.parse("2024-06-19"), phList);
	            long count2 = offDayService.calculateLeaveDays(LocalDate.parse("2024-06-12"), LocalDate.parse("2024-06-15"), phList);
	            long count3 = offDayService.calculateLeaveDays(LocalDate.parse("2024-06-07"), LocalDate.parse("2024-06-19"), phList);
	            long count4 = offDayService.calculateLeaveDays(LocalDate.parse("2024-06-12"), LocalDate.parse("2024-06-15"), phList);
	            long count5 = offDayService.calculateLeaveDays(LocalDate.parse("2024-06-16"), LocalDate.parse("2024-06-18"), phList);
	            long count6 = offDayService.calculateLeaveDays(LocalDate.parse("2024-06-20"), LocalDate.parse("2024-06-21"), phList);
	            long count7 = offDayService.calculateLeaveDays(LocalDate.parse("2024-07-01"), LocalDate.parse("2024-07-02"), phList);
	            long count8 = offDayService.calculateLeaveDays(LocalDate.parse("2024-07-10"), LocalDate.parse("2024-07-12"), phList);

	            leaveRecordRepo.saveAndFlush(new LeaveRecord(emp1, LocalDate.parse("2024-06-09"), LocalDate.parse("2024-06-19"), "Sick", "see doctor", "rest well", ApplicationStatus.Applied, "234124", leavetype2, count1));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(emp1, LocalDate.parse("2024-06-12"), LocalDate.parse("2024-06-15"), "mental wellness", " ", " ", ApplicationStatus.Applied, "234124", leavetype2, count2));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(emp2, LocalDate.parse("2024-06-07"), LocalDate.parse("2024-06-19"), "Sick", " ", " ", ApplicationStatus.Applied, "234124", leavetype2, count3));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(emp5, LocalDate.parse("2024-06-12"), LocalDate.parse("2024-06-15"), "leave record 4", "No remark", "nicee", ApplicationStatus.Applied, "234124", leavetype1, count4));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(emp5, LocalDate.parse("2024-06-16"), LocalDate.parse("2024-06-18"), "leave record 5", "No remark", "nicee", ApplicationStatus.Applied, "234124", leavetype1, count5));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(emp5, LocalDate.parse("2024-06-20"), LocalDate.parse("2024-06-21"), "leave record 6", "No remark", "nicee", ApplicationStatus.Applied, "234124", leavetype1, count6));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(emp5, LocalDate.parse("2024-07-01"), LocalDate.parse("2024-07-02"), "leave record 7", "No remark", "nicee", ApplicationStatus.Approved, "234124", leavetype1, count7));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(emp5, LocalDate.parse("2024-07-10"), LocalDate.parse("2024-07-12"), "leave record 8", "No remark", "nicee", ApplicationStatus.Reject, "234124", leavetype1, count8));

	            leaveRecordRepo.saveAndFlush(new LeaveRecord(empt1, LocalDate.parse("2024-07-01"), LocalDate.parse("2024-07-02"), "Testor A Annual Leave", " ", " ", ApplicationStatus.Applied, "234124", leavetype2, count7));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(empt1, LocalDate.parse("2024-07-10"), LocalDate.parse("2024-07-12"), "Testor A MC", " ", " ", ApplicationStatus.Applied, "234124", leavetype1, count8));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(empt2, LocalDate.parse("2024-06-16"), LocalDate.parse("2024-06-18"), "Testor B Annual Leave", " ", " ", ApplicationStatus.Approved, "234124", leavetype2, count5));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(empt2, LocalDate.parse("2024-06-20"), LocalDate.parse("2024-06-21"), "Testor B MC", " ", " ", ApplicationStatus.Approved, "234124", leavetype1, count6));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(empt3, LocalDate.parse("2024-06-12"), LocalDate.parse("2024-06-15"), "Testor C Annual Leave", " ", " ", ApplicationStatus.Applied, "234124", leavetype2, count4));
	            leaveRecordRepo.saveAndFlush(new LeaveRecord(empt3, LocalDate.parse("2024-06-16"), LocalDate.parse("2024-06-18"), "Testor C MC", " ", " ", ApplicationStatus.Applied, "234124", leavetype1, count5));
	        };
	    }

	    private void createLeaveCountForEmployee(LeaveCountRepository leaveCountRepo, Employee employee, LeaveType leaveType1, LeaveType leaveType2, LeaveType leaveType3) {
	        leaveCountRepo.saveAndFlush(new LeaveCount(employee, leaveType1, 60.0));
	        leaveCountRepo.saveAndFlush(new LeaveCount(employee, leaveType2, 14.0));
	        leaveCountRepo.saveAndFlush(new LeaveCount(employee, leaveType3, 0.0));
	    }
         
		
}
