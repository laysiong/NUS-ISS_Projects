package com.example.demo.repository;

import com.example.demo.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LabelRepository extends JpaRepository<Label, Integer> {
    Label findByLabel(String label);

    @Query("SELECT COUNT(l) > 0 FROM Label l WHERE l.label = :label")
    Boolean checkDuplicateLabel(String label);
}
