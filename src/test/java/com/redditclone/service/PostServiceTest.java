package com.redditclone.service;

import java.time.Instant;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.redditclone.dto.PostRequest;
import com.redditclone.dto.PostResponse;
import com.redditclone.mapper.PostMapper;
import com.redditclone.model.Post;
import com.redditclone.model.Subreddit;
import com.redditclone.model.User;
import com.redditclone.repository.PostRepository;
import com.redditclone.repository.SubredditRepository;
import com.redditclone.repository.UserRepository;

import static java.util.Collections.emptyList;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@Mock
	private PostRepository postRepository;
	@Mock
	private SubredditRepository subredditRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private AuthService authService;
	@Mock
	private PostMapper postMapper;

	private PostService postService;

	@BeforeEach
	public void setup() {
		postService = new PostService(postRepository, subredditRepository, userRepository, authService, postMapper);
	}

	@Captor
	private ArgumentCaptor<Post> postArgumentCaptor;

	@Test
	@DisplayName("Should Retrieve Post by Id")
	void shouldFindPostById() {

		Post post = new Post(123L, "First Post", "http://url.site", "Test", 0, null, Instant.now(), null);

		PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test", "Test user",
				"Test subreddit", 0, 0, "1 Hour Ago", false, false);

		Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));
		Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);

		PostResponse actualPostResponse = postService.getPost(123L);

		Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
		Assertions.assertThat(actualPostResponse.getPostName()).isEqualTo(expectedPostResponse.getPostName());

	}

	@Test
	@DisplayName("Should Save Posts")
	void shouldSavePosts() {
		User currentUser = new User(123L, "test user", "secret password", "user@email.com", Instant.now(), true);
		Subreddit subreddit = new Subreddit(123L, "First Subreddit", "Subreddit Description", emptyList(),
				Instant.now(), currentUser);
		Post post = new Post(123L, "First Post", "http://url.site", "Test", 0, null, Instant.now(), null);
		PostRequest postRequest = new PostRequest(null, "First Subreddit", "First Post", "http://url.site", "Test");

		Mockito.when(subredditRepository.findByName("First Subreddit")).thenReturn(Optional.of(subreddit));
		Mockito.when(authService.getCurrentUser()).thenReturn(currentUser);
		Mockito.when(postMapper.map(postRequest, subreddit, currentUser)).thenReturn(post);

		postService.save(postRequest);
		Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());

		Assertions.assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(123L);
		Assertions.assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("First Post");
	}
}
