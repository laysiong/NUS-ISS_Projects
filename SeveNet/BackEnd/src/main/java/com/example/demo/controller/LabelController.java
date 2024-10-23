package com.example.demo.controller;

import com.example.demo.dto.LabelDTO;
import com.example.demo.exception.DuplicateAuthException;
import com.example.demo.exception.DuplicateTypeException;
import com.example.demo.model.Label;
import com.example.demo.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Label>> getAllLabels() {
        List<Label> labels = labelService.findAllLabels();
        return ResponseEntity.ok(labels);
    }

    @GetMapping("/findAllForReport")
    public ResponseEntity<List<LabelDTO>> getAllLabelDTOs() {
        List<LabelDTO> labels = labelService.findAllLabelsDTO();
        return ResponseEntity.ok(labels);
    }
    
    @GetMapping("/findColorCodeByLabel")
    public ResponseEntity <Map<String, String>> getColorCodeByLabel() {
    	Map<String, String> labels = labelService.getColorCodeByLabel();
        return ResponseEntity.ok(labels);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Label> getLabelById(@PathVariable("id") Integer id) {
        Label label = labelService.findById(id);
        if (label != null) {
            return ResponseEntity.ok(label);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findByLabel/{label}")
    public ResponseEntity<Label> getLabelByRank(@PathVariable("label") String label) {
        Label fLabel = labelService.findByLabel(label);
        if (fLabel != null) {
            return ResponseEntity.ok(fLabel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Label> saveLabel(@RequestBody Label label) {
        try {
            return ResponseEntity.ok(labelService.saveLabel(label));
        } catch (DuplicateTypeException e) {
            return ResponseEntity.status(409).build();  // 409 Conflict
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Label> updateLabel(@PathVariable("id") Integer id, @RequestBody Label updateLabel) {
        return ResponseEntity.ok(labelService.updateLabel(id, updateLabel));
    }

    @PutMapping("/updatePenaltyScore/{id}/{penaltyScore}")
    public ResponseEntity<Void> updatePenaltyScoreById(@PathVariable("id") Integer id, @PathVariable("penaltyScore") Integer penaltyScore) {
        labelService.updatePenaltyScoreById(id, penaltyScore);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable("id") Integer id) {
        labelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    

    @ExceptionHandler(DuplicateAuthException.class)
    public ResponseEntity<String> handleDuplicateAuthException(DuplicateAuthException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
