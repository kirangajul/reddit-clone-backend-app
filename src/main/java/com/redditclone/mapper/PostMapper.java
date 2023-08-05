package com.redditclone.mapper;

import java.util.Date;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.redditclone.dto.PostRequest;
import com.redditclone.dto.PostResponse;
import com.redditclone.model.Post;
import com.redditclone.model.Subreddit;
import com.redditclone.model.User;
import com.redditclone.model.Vote;
import com.redditclone.model.VoteType;
import com.redditclone.repository.CommentRepository;
import com.redditclone.repository.VoteRepository;
import com.redditclone.service.AuthService;

import static com.redditclone.model.VoteType.DOWNVOTE;
import static com.redditclone.model.VoteType.UPVOTE;

import org.ocpsoft.prettytime.PrettyTime;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private VoteRepository voteRepository;
	@Autowired
	private AuthService authService;

	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "description", source = "postRequest.description")
	@Mapping(target = "subreddit", source = "subreddit")
	@Mapping(target = "voteCount", constant = "0")
	@Mapping(target = "user", source = "user")
	public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);
	
	    @Mapping(target = "id", source = "postId")
	    @Mapping(target = "subredditName", source = "subreddit.name")
	    @Mapping(target = "userName", source = "user.username")
	    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
	    @Mapping(target = "duration", expression = "java(getDuration(post))")
	    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
	    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
	    public abstract PostResponse mapToDto(Post post);

	    Integer commentCount(Post post) {
	        return commentRepository.findByPost(post).size();
	    }

	    String getDuration(Post post) {
	    	PrettyTime prettyTime = new PrettyTime();
	        return prettyTime.formatDuration(new Date(post.getCreatedDate().toEpochMilli()));
	    }

	    boolean isPostUpVoted(Post post) {
	        return checkVoteType(post, UPVOTE);
	    }

	    boolean isPostDownVoted(Post post) {
	        return checkVoteType(post, DOWNVOTE);
	    }

	    private boolean checkVoteType(Post post, VoteType voteType) {
	        if (authService.isLoggedIn()) {
	            Optional<Vote> voteForPostByUser =
	                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
	                            authService.getCurrentUser());
	            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
	                    .isPresent();
	        }
	        return false;
	    }
}
