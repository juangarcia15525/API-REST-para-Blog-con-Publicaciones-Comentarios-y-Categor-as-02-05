package org.example.post;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.example.comment.Comment;
import org.example.comment.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> findAll() {
        List<Post> posts = postService.findAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .header("X-Resource-Type", "post")
                .body(postService.findById(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> findCommentsByPost(@PathVariable Long id) {
        postService.findById(id);
        List<Comment> comments = commentService.findByPostId(id);
        return ResponseEntity.ok()
                .header("X-Resource-Type", "comment")
                .header("X-Total-Count", String.valueOf(comments.size()))
                .body(comments);
    }

    @PostMapping
    public ResponseEntity<Post> create(@Valid @RequestBody PostRequest request) {
        Post post = postService.create(request);
        return ResponseEntity.created(URI.create("/api/posts/" + post.id()))
                .header("X-Resource-Type", "post")
                .body(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok()
                .header("X-Resource-Type", "post")
                .body(postService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        commentService.deleteByPostId(id);
        return ResponseEntity.noContent()
                .header("X-Resource-Type", "post")
                .build();
    }
}
