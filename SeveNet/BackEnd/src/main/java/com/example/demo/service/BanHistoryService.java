package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.interfacemethods.BanHistoryInterface;
import com.example.demo.interfacemethods.UserInterface;
import com.example.demo.model.BanHistory;
import com.example.demo.model.User;
import com.example.demo.statusEnum.UserStatus;
import com.example.demo.repository.BanHistoryRepository;

@Service      
@Transactional(readOnly = true)
public class BanHistoryService implements BanHistoryInterface{
	
	@Autowired
	private UserInterface userService;
	
	@Autowired
	private BanHistoryRepository banHistoryRepository;
	
	@Override
	public BanHistory findBanHistoryById(Integer banId) {
		return banHistoryRepository.findById(banId)
				.orElseThrow(() -> new RuntimeException("BanHistory not found with ID:" + banId));
	}

	@Override
	public List<BanHistory> findAllBanByUserId(Integer userId) {
		List<BanHistory> findBanList= banHistoryRepository.findAllByBanUserId(userId);
		if (findBanList == null || findBanList.size() <= 0) {
			throw new RuntimeException("BanHistorys not found with User/Employee ID:" + userId);
		}
		return findBanList;
	}

	@Override
	public Boolean IsWithinBanPeriodByUserId(Integer userId) {
	    User curUser = userService.findUserById(userId);
	    Boolean isWithinBanPeriod = false;
	    // only when user's status is 'ban' then we do the checking
	    if (curUser.getStatus() == UserStatus.ban) {
	        // retrieve now time
	        LocalDateTime now = LocalDateTime.now();
	        
	        // find all banHistory with this userId
	        List<BanHistory> findBanList = findAllBanByUserId(userId);
	        
	        // check isWithinBanPeriod
	        isWithinBanPeriod = findBanList.stream().anyMatch(banHistory -> {
	            LocalDateTime banEndTime = banHistory.getBanTime().plusYears(banHistory.getBanDuration());
	            return now.isAfter(banHistory.getBanTime()) && now.isBefore(banEndTime);
	        });
	    }
	    return isWithinBanPeriod;
	}

	@Override
	@Transactional(readOnly = false)
	public BanHistory saveBanHistory(BanHistory banHistory) {
		return banHistoryRepository.save(banHistory);
	}

	@Override
	@Transactional(readOnly = false)
	public BanHistory updateBanHistoryDurationById(Integer banId, Integer newBanHistoryDuration) {
		BanHistory oldBanHistory = findBanHistoryById(banId);
		oldBanHistory.setBanDuration(newBanHistoryDuration);
		return banHistoryRepository.save(oldBanHistory);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteBanHistoryById(Integer banId) {
		try {
			banHistoryRepository.deleteById(banId);
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete BanHistory with ID: " + banId, e);
		}
	}

}
