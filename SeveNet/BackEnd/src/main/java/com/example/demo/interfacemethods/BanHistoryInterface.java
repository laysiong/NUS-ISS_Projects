package com.example.demo.interfacemethods;

import java.util.List;

import com.example.demo.model.BanHistory;

public interface BanHistoryInterface {
	List<BanHistory> findAllBanByUserId(Integer userId);
	BanHistory findBanHistoryById(Integer banId);
	// check whether this user is in the banned period
	Boolean IsWithinBanPeriodByUserId(Integer userId);
	
	// CRUD
	BanHistory saveBanHistory(BanHistory banHistory);
	// only allow to change/update the ban duration
	BanHistory updateBanHistoryDurationById(Integer banId, Integer newBanHistoryDuration);
	void deleteBanHistoryById(Integer banId);
}
