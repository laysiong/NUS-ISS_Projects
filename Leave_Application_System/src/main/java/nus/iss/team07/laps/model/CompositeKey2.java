package nus.iss.team07.laps.model;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
public class CompositeKey2 implements Serializable {
    
    private Integer employee;
    private LocalDate startDate;

    public CompositeKey2() {}

    public CompositeKey2(Integer idPart1, LocalDate idPart2) {
        this.employee = idPart1;
        this.startDate = idPart2;
    }

    // Getters and setters

    public Integer getIdPart1() {
        return employee;
    }

    public void setIdPart1(Integer idPart1) {
        this.employee = idPart1;
    }

    public LocalDate getIdPart2() {
        return startDate;
    }

    public void setIdPart2(LocalDate idPart2) {
        this.startDate = idPart2;
    }

    // hashCode and equals methods

    @Override
    public int hashCode() {
        return Objects.hash(employee, startDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CompositeKey2 that = (CompositeKey2) obj;
        return Objects.equals(employee, that.employee) && Objects.equals(startDate, that.startDate);
    }
}
