package com.example.demo.interfacemethods;

import java.util.List;

import com.example.demo.model.Tag;

public interface TagInterface {
	List<Tag> findAllTag();
	Tag saveTag(Tag tag);
	Tag getTagById(Integer id);
	Tag updateReport(Integer id, Tag TagForm);
}