package nus.iss.team07.laps.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Holidays {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
	@NotBlank(message = "Name is required")
	private String holiday;
	
    @NotNull(message = "Date cannot be null")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
    public Holidays () {}
    
	public Holidays (String Holiday, LocalDate startDate) {
		this.holiday = Holiday;
		this.startDate = startDate;
	}
    

	//getter and setter

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getHoliday() {
		return holiday;
	}
	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}
	
	public LocalDate getStartDate() {
	    return startDate;
	}
	public void setStartDate(LocalDate startDate) {
	    this.startDate = startDate;
	}

	
}
