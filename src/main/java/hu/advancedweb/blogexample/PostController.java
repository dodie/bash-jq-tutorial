package hu.advancedweb.blogexample;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {
    
    private static final int PAGE_SIZE = 10;
    
    @Autowired
    PostRepository repository;

    @GetMapping
    public ResponseEntity<PostList> getAllPosts(@RequestParam(defaultValue = "0") Integer pageNumber) {
	Pageable paging = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("id"));
	Page<Post> pagedResult = repository.findAll(paging);

	final List<Post> list;
	if (pagedResult.hasContent()) {
	    list = pagedResult.getContent();
	} else {
	    list = new ArrayList<Post>();
	}

	List<PostShortDto> posts = list.stream().map(PostController::toShortDto).collect(Collectors.toList());
	
	PostList postList = new PostList();
	postList.setPosts(posts);
	postList.setCount(posts.size());
	postList.setCurrentPageNumber(pageNumber);
	postList.setLastPageNumber((repository.count() - 1) / PAGE_SIZE);
	
	return new ResponseEntity<PostList>(postList, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id) {
	return new ResponseEntity<Post>(repository.findById(id).get(), new HttpHeaders(), HttpStatus.OK);
    }
    
    static PostShortDto toShortDto(Post post) {
	PostShortDto dto = new PostShortDto();
	dto.setId(post.getId());
	dto.setTitle(post.getTitle());
	dto.setTags(post.getTags());
	dto.setStatus(post.getStatus());
	return dto;
    }
    
}