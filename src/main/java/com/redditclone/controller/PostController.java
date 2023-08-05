package com.redditclone.controller;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redditclone.dto.PostRequest;
import com.redditclone.dto.PostResponse;
import com.redditclone.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
		postService.save(postRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<PostResponse>> getAllPosts() {
		return new ResponseEntity<List<PostResponse>>(postService.getAllPosts(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
		return new ResponseEntity<PostResponse>(postService.getPost(id), HttpStatus.OK);
	}

	@GetMapping(params = "subredditId")
	public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@RequestParam Long subredditId) {
		return new ResponseEntity<List<PostResponse>>(postService.getPostsBySubreddit(subredditId), HttpStatus.OK);
	}

	@GetMapping(params = "username")
	public ResponseEntity<List<PostResponse>> getPostsByUsername(@RequestParam String username) {
		return new ResponseEntity<List<PostResponse>>(postService.getPostsByUsername(username), HttpStatus.OK);
	}
}
