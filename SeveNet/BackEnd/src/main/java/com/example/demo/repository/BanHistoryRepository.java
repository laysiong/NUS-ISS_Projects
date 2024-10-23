package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.BanHistory;

public interface BanHistoryRepository extends JpaRepository<BanHistory,Integer>{
	List<BanHistory> findAllByBanUserId(Integer userId);
}
