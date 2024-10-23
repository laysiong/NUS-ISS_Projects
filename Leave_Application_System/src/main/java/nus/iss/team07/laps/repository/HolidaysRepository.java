package nus.iss.team07.laps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nus.iss.team07.laps.model.Holidays;

public interface HolidaysRepository extends JpaRepository <Holidays, Integer> {

}
