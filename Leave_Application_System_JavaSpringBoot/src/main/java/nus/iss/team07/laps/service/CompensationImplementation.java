package nus.iss.team07.laps.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import nus.iss.team07.laps.interfacemethods.CompensationInterface;
import nus.iss.team07.laps.model.ApplicationStatus;
import nus.iss.team07.laps.model.Compensation;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.repository.CompensationRepository;

@Service

public class CompensationImplementation implements CompensationInterface{

	
	@Autowired
	CompensationRepository CompensationRepos;
	
	@Transactional
	public List<Compensation> listCompensation() {
		return CompensationRepos.findAll();
	}

	@Transactional
	public Optional<Compensation> findCompensationById(int id) {
	    //System.out.println(CompensationRepos.findById(id));
	    return CompensationRepos.findById(id);
	}
	 
	@Transactional
	public Compensation saveCompensation(Compensation form) {
		return CompensationRepos.save(form);
	}
	
	@Transactional
	public void deleteCompensationForm(Compensation form) {
		 CompensationRepos.delete(form);
    }


	
	@Transactional
    public List<Compensation> findPendingAndApprovedCompensations() {
        List<ApplicationStatus> statuses = Arrays.asList(ApplicationStatus.Applied, ApplicationStatus.Updated);
        return CompensationRepos.findByStatusIn(statuses);
    }

	@Override
	public List<Compensation> getPersonalCompensationHistory(Employee emp) {
        return CompensationRepos.findByEmployee(emp);
	}
	

}
