package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.interfacemethods.BanHistoryInterface;
import com.example.demo.model.BanHistory;

@RestController
@RequestMapping("/api/banHistory")
public class BanHistoryController {
	@Autowired
	private BanHistoryInterface banHistoryService;
	
	@GetMapping("/findAllByUserId/{userId}")
	public ResponseEntity<List<BanHistory>> getAllBanHistoryByUser(@PathVariable("userId") Integer userId) {
        List<BanHistory> banList = banHistoryService.findAllBanByUserId(userId);
        return ResponseEntity.ok(banList);
    }
	
	@GetMapping("/findById/{banId}")
	public ResponseEntity<BanHistory> getBanHistoryById(@PathVariable("banId") Integer banId) {
		BanHistory ban = banHistoryService.findBanHistoryById(banId);
        return ResponseEntity.ok(ban);
    }
	
	@GetMapping("/checkWithinBanPeriodByUserId/{userId}")
	public ResponseEntity<Boolean> checkWithinBanPeriodByUserId(@PathVariable("userId") Integer userId) {
		Boolean isWithinBanPeriod = banHistoryService.IsWithinBanPeriodByUserId(userId);
        return ResponseEntity.ok(isWithinBanPeriod);
    }
	
	@PostMapping("/create")
    public ResponseEntity<Void> saveBanHistory(@RequestBody BanHistory banHistory) {
        try {
        	banHistoryService.saveBanHistory(banHistory);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateBanHistory(@PathVariable("id") Integer id, @RequestBody Integer newBanDuration) {
    	banHistoryService.updateBanHistoryDurationById(id, newBanDuration);
    	return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAuth(@PathVariable("id") Integer id) {
    	banHistoryService.deleteBanHistoryById(id);
        return ResponseEntity.noContent().build();
    }
}
