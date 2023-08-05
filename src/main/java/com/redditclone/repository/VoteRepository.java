package com.redditclone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.redditclone.model.Post;
import com.redditclone.model.User;
import com.redditclone.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
	 Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
