package nus.iss.team07.laps.interfacemethods;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import nus.iss.team07.laps.model.Holidays;

public interface HolidayInterface {
	
	List<Holidays> findAllHolidays();
	Optional<Holidays> findHolidayById(Integer id);
	void deleteHolidays(Holidays form);
	void deleteHolidaybyId(int id);

	Holidays saveHolidays(Holidays form);
	
}
