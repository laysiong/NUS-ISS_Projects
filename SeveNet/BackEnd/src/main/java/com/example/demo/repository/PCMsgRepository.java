package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.PCMsg;

public interface PCMsgRepository extends JpaRepository<PCMsg,Integer> {

	@Query("SELECT COUNT(p) FROM PCMsg p WHERE p.sourceId IS NULL")
	Integer countPosts();
	
	@Query("SELECT COUNT(p) FROM PCMsg p WHERE p.sourceId IS NOT NULL")
	Integer countComments();
	
    @Query("SELECT p FROM PCMsg p ORDER BY p.timeStamp DESC")
    List<PCMsg> findAllPCMsgsOrderByDateDesc();
    

    @Query("SELECT p FROM PCMsg p WHERE p.sourceId IS NULL AND p.user.id = :userId ORDER BY p.timeStamp DESC ")
    List<PCMsg> findAllPostsByUserIdByDateDesc(@Param("userId") int userId);
    
    @Query("SELECT p FROM PCMsg p WHERE p.sourceId = :pcmsgId")
    List<PCMsg> findAllFirstLayerChildren(@Param("pcmsgId") int pcmsgId);
    
    @Query("SELECT p FROM PCMsg p WHERE p.sourceId IS NULL AND p.user.id IN :followingUserIds AND p.user.id NOT IN :blockedUserIds AND p.status != 'delete'  ORDER BY p.timeStamp DESC")
    List<PCMsg> findAllFollowingPostsByUserId(@Param("followingUserIds") List<Integer> followingUserIds, 
                                              @Param("blockedUserIds") List<Integer> blockedUserIds);
    
  
    @Query("Select p From PCMsg p WHERE p.sourceId IS NULL AND (p.content like CONCAT('%',:k,'%') OR p.user.username like CONCAT('%',:k,'%')) ORDER BY p.timeStamp DESC") 
	List<PCMsg> SearchPostsByContentAndUserNameOrderByDateDesc(@Param("k") String keyword);
    
    @Query("SELECT p FROM PCMsg p WHERE p.sourceId IS NULL ORDER BY p.timeStamp DESC ")
    List<PCMsg> findAllPostsOnly();
    
    // For top 5 posts
    @Query("SELECT p FROM PCMsg p WHERE p.sourceId IS NULL ORDER BY p.timeStamp DESC")
    List<PCMsg> findTop5BySourceIdIsNullOrderByTimeStampDesc();

    //Remove Deleted Post
//	@Query("SELECT p FROM PCMsg p WHERE p.sourceId IS NULL and p.status != 'delete' ORDER BY p.timeStamp DESC ")
//	List<PCMsg> findAllPostsOnlyNotDeleted();
    
    @Query("SELECT p FROM PCMsg p WHERE p.sourceId IS NULL AND p.status != 'delete' "
            + "AND (p.user.id IN :followingUserIds OR p.user.id = :userId) "
            + "AND p.user.id NOT IN :blockedUserIds "
            + "ORDER BY p.timeStamp DESC")
    List<PCMsg> findAllFollowingPostsAndNotDeletedByUserId(
            @Param("followingUserIds") List<Integer> followingUserIds, 
            @Param("blockedUserIds") List<Integer> blockedUserIds,
            @Param("userId") int userId);

    // for pagination
    @Query("SELECT p FROM PCMsg p WHERE p.sourceId IS NULL ORDER BY p.timeStamp DESC ")
    Page<PCMsg> findAllPostsOnly(Pageable pageable);

    @Query("SELECT p FROM PCMsg p WHERE p.sourceId IS NULL AND p.user.id = :userId ORDER BY p.timeStamp DESC ")
    Page<PCMsg> findAllPostsByUserIdByDateDesc(@Param("userId") int userId,Pageable pageable);

    @Query("Select p From PCMsg p WHERE p.sourceId IS NULL AND (p.content like CONCAT('%',:k,'%') OR p.user.username like CONCAT('%',:k,'%')) ORDER BY p.timeStamp DESC")
    Page<PCMsg> SearchPostsByContentAndUserNameOrderByDateDesc(@Param("k") String keyword,Pageable pageable);

    @Query("SELECT p FROM PCMsg p WHERE p.sourceId IS NULL AND p.status != 'delete' "
            + "AND (p.user.id IN :followingUserIds OR p.user.id = :userId) "
            + "AND p.user.id NOT IN :blockedUserIds "
            + "ORDER BY p.timeStamp DESC")
    Page<PCMsg> findAllFollowingPostsAndNotDeletedByUserId(
            @Param("followingUserIds") List<Integer> followingUserIds,
            @Param("blockedUserIds") List<Integer> blockedUserIds,
            @Param("userId") int userId,Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PCMsg p WHERE p.user.id = :userId AND p.id = :pcmsgId")
    boolean isPCMsgBelongToUser(@Param("userId") int userId, @Param("pcmsgId") int pcmsgId);

    
}