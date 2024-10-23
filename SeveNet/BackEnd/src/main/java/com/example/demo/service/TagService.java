package com.example.demo.service;

import com.example.demo.model.Label;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.interfacemethods.TagInterface;
import com.example.demo.model.Tag;
import com.example.demo.repository.TagRepository;

@Service
@Transactional
public class TagService implements TagInterface{

    @Autowired
    private TagRepository tagRepository;
	
	@Override
    public List<Tag> findAllTag(){
    	return tagRepository.findAll();
    }
    
	@Override
	public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
	}

    @Override
    @Transactional(readOnly = false)
    public Tag updateReport(Integer id, Tag TagForm) {
        Tag existingTag = getTagById(id);
        existingTag.setRemark(TagForm.getRemark());
        existingTag.setTag(TagForm.getTag());
        return tagRepository.save(existingTag);
    }
	
	@Override
	public Tag getTagById(Integer id) {
		return tagRepository.findById(id).orElse(null);
	}

}