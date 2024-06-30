package nus.iss.team07.laps.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;



@Service
public class CaculateOffDays {
	
    public long calculateLeaveDays(LocalDate startDate, LocalDate endDate, List<LocalDate> publicHolidays) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Invalid dates");
        }

        long totalDays = startDate.datesUntil(endDate.plusDays(1)).count();

        if (totalDays > 14) {
            return totalDays; // Include all days
        }

        long businessDays = startDate.datesUntil(endDate.plusDays(1))
                                      .filter(date -> !isWeekendOrHoliday(date, publicHolidays))
                                      .count();
        return businessDays;
    }

    private boolean isWeekendOrHoliday(LocalDate date, List<LocalDate> publicHolidays) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY ||
               date.getDayOfWeek() == DayOfWeek.SUNDAY ||
               publicHolidays.contains(date);
    }
	
}
