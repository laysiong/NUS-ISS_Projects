package nus.iss.team07.laps.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nus.iss.team07.laps.interfacemethods.HolidayInterface;
import nus.iss.team07.laps.model.Holidays;
import nus.iss.team07.laps.repository.HolidaysRepository;

@Service
@Transactional(readOnly = true)
public class HolidaysImplementation implements HolidayInterface {

	@Autowired
	private HolidaysRepository holidayRepository;
	
	
	@Override
	public List<Holidays> findAllHolidays() {
		// TODO Auto-generated method stub
		return  holidayRepository.findAll();
	}

	@Override
	public Optional<Holidays> findHolidayById(Integer id) {
		// TODO Auto-generated method stub
		return holidayRepository.findById(id);
	}

	
	
	@Transactional(readOnly = false)
	@Override
	public void deleteHolidays(Holidays form) {
		holidayRepository.delete(form);
	}

	
	@Transactional(readOnly = false)
	@Override
	public Holidays saveHolidays(Holidays form) {
		return holidayRepository.save(form);
	}

	
	@Transactional(readOnly = false)
	@Override
	public void deleteHolidaybyId(int id) {
		// TODO Auto-generated method stub
		holidayRepository.deleteById(id);
		
	}




	
}
