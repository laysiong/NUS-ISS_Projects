package nus.iss.team07.laps.dto;

public class SupervisorDTO {
	private Integer id;
    private String name;
    // Exclude joinDate or any other problematic fields

    public SupervisorDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SupervisorDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
