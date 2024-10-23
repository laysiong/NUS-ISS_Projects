package nus.iss.team07.laps.interfacemethods;

import nus.iss.team07.laps.model.Compensation;
import nus.iss.team07.laps.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

public interface CompensationInterface {
	
	
	List<Compensation> listCompensation();
	Optional<Compensation> findCompensationById(int id);
	
//	List<Compensation> findPendingCompensationForm();


	Compensation saveCompensation (Compensation form);
	void deleteCompensationForm(Compensation form);
	
	List<Compensation> getPersonalCompensationHistory(Employee emp);
	List<Compensation> findPendingAndApprovedCompensations();
}
