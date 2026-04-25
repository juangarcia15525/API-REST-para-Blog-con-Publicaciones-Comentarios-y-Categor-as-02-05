package org.example.comment;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> findAll() {
        List<Comment> comments = commentService.findAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(comments.size()))
                .body(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> findById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .header("X-Resource-Type", "comment")
                .body(commentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Comment> create(@Valid @RequestBody CommentRequest request) {
        Comment comment = commentService.create(request);
        return ResponseEntity.created(URI.create("/api/comments/" + comment.id()))
                .header("X-Resource-Type", "comment")
                .body(comment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> update(@PathVariable Long id, @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok()
                .header("X-Resource-Type", "comment")
                .body(commentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent()
                .header("X-Resource-Type", "comment")
                .build();
    }
}
