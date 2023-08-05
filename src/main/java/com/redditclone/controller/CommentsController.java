package com.redditclone.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redditclone.dto.CommentsDto;
import com.redditclone.service.CommentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
		commentService.save(commentsDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(params = "postId")
	public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@RequestParam Long postId) {
		return new ResponseEntity<List<CommentsDto>>(commentService.getAllCommentsForPost(postId), HttpStatus.OK);
	}

	@GetMapping(params = "userName")
	public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@RequestParam String userName) {
		return new ResponseEntity<List<CommentsDto>>(commentService.getAllCommentsForUser(userName), HttpStatus.OK);
	}
}
