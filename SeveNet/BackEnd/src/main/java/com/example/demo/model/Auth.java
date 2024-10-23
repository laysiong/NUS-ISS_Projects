package com.example.demo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Auth {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 255, name = "`rank`")
	// Rank is a reserved keyword in MySQL, 
	// so using it when creating tables can result in syntax errors.
	// using `` can avoid such error
	private String rank;

	@Column(columnDefinition = "TEXT")
	private String menuViewJason;

	@OneToMany(mappedBy = "auth", cascade = CascadeType.ALL)
	private List<User> users;

	public Auth() {
	}

	public Auth(String rank, String menuViewJason) {
		this.rank = rank;
		this.menuViewJason = menuViewJason;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getMenuViewJason() {
		return menuViewJason;
	}

	public void setMenuViewJason(String menuViewJason) {
		this.menuViewJason = menuViewJason;
	}
}
