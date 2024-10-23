package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Like;

public interface LikeRepository extends JpaRepository<Like,Integer> {
	@Query("SELECT l FROM Like l WHERE l.UserId = :userId AND l.PCMsgId = :pcmsgId")
	Like hasWithUserAndPCMsg(@Param("userId") int userId,@Param("pcmsgId") int pcmsgId);
	
	//remove > 0
	@Query("SELECT COUNT(l) FROM Like l WHERE l.PCMsgId = :pcmsgId")
	Integer countLikesByPCMsgId(@Param("pcmsgId") int pcmsgId);
	
	 @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.UserId = :userId AND l.PCMsgId = :pcmsgId")
	 boolean existsByUserIdAndPCMsgId(@Param("userId") int userId, @Param("pcmsgId") int pcmsgId);
}
