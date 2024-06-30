package nus.iss.team07.laps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import nus.iss.team07.laps.interfacemethods.LeaveTypeInterface;
import nus.iss.team07.laps.model.LeaveType;
import nus.iss.team07.laps.repository.LeaveTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeaveTypeImplementation implements LeaveTypeInterface {

	    @Autowired
	    private LeaveTypeRepository leaveTypeRepository;
	 
	    @Override
	    public List<LeaveType> getAllLeaveTypes() {
	        return leaveTypeRepository.findAll();
	    }

	    @Override
	    public Optional<LeaveType> getLeaveTypeById(int id) {
	        return leaveTypeRepository.findById(id);
	    }

	    @Override
	    public LeaveType saveLeaveType(LeaveType leaveType) {
	        return leaveTypeRepository.save(leaveType);
	    }

	    @Override
	    public void deleteLeaveType(LeaveType leaveType) {
	        leaveTypeRepository.delete(leaveType);
	    }

	    
		@Override
		public LeaveType findIdByTypeLike(String leaveName) {
			return leaveTypeRepository.findByTypeLike(leaveName);
		}
		
		
		
		
}