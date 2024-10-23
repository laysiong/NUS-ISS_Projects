package com.example.demo.dto;

import com.example.demo.model.Label;

public class LabelDTO {
    private Integer id;
    private String label;

    public LabelDTO(Label label) {
        this.id = label.getId();
        this.label = label.getLabel();
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
}
