package com.example.demo.service;

import com.example.demo.dto.LabelDTO;
import com.example.demo.exception.DuplicateAuthException;
import com.example.demo.exception.DuplicateLabelException;
import com.example.demo.interfacemethods.LabelInterface;
import com.example.demo.model.Auth;
import com.example.demo.model.Label;
import com.example.demo.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LabelService implements LabelInterface {

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public List<Label> findAllLabels() {
        return labelRepository.findAll();
    }

    @Override
    public List<LabelDTO> findAllLabelsDTO() {
        return labelRepository.findAll().stream().map(LabelDTO::new).collect(Collectors.toList());
    }

    @Override
    public Label findById(Integer id) {
        return labelRepository.findById(id).orElse(null);
    }

    @Override
    public Label findByLabel(String label) {
        return labelRepository.findByLabel(label);
    }

    @Override
    @Transactional(readOnly = false)
    public Label saveLabel(Label label) {
        handleDuplicateLabelException(label);
        return labelRepository.save(label);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteById(Integer id) {
        try {
            labelRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Label with ID: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void updatePenaltyScoreById(Integer id, Integer penaltyScore) {
        Label label = findById(id);
        label.setPenaltyScore(penaltyScore);
        labelRepository.save(label);
    }

    @Override
    @Transactional(readOnly = false)
    public Label updateLabel(Integer id, Label newLabel) {
        Label oldLabel = findById(id);
        oldLabel.setPenaltyScore(newLabel.getPenaltyScore());
        oldLabel.setLabel(newLabel.getLabel());
        oldLabel.setColorCode(newLabel.getColorCode());
        return labelRepository.save(oldLabel);
    }
    
    @Override
    public Map<String, String> getColorCodeByLabel() {
        List<Label> labels = labelRepository.findAll();
        Map<String, String> labelTable = new HashMap<>();
        for (Label lbl : labels) {
            labelTable.put(lbl.getLabel(), lbl.getColorCode());
        }
        return labelTable;
    }
    

    private void handleDuplicateLabelException(Label label) {
        if (labelRepository.checkDuplicateLabel(label.getLabel())) {
            throw new DuplicateLabelException("Label already exists: " + "Label:" +label.getLabel());
        }
    }
}
