package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer>{

}