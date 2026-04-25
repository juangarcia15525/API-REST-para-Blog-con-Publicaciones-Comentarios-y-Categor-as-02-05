package org.example.comment;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.post.PostService;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Comment> comments = new ConcurrentHashMap<>();
    private final PostService postService;

    public CommentService(PostService postService) {
        this.postService = postService;
    }

    public List<Comment> findAll() {
        return comments.values().stream()
                .sorted(Comparator.comparing(Comment::id))
                .toList();
    }

    public List<Comment> findByPostId(Long postId) {
        return comments.values().stream()
                .filter(comment -> comment.postId().equals(postId))
                .sorted(Comparator.comparing(Comment::id))
                .toList();
    }

    public Comment findById(Long id) {
        Comment comment = comments.get(id);
        if (comment == null) {
            throw new NotFoundException("Comentario no encontrado: " + id);
        }
        return comment;
    }

    public Comment create(CommentRequest request) {
        validatePost(request.postId());
        validateUniqueComment(request, null);
        Long id = sequence.incrementAndGet();
        Comment comment = new Comment(
                id,
                request.postId(),
                request.authorName().trim(),
                request.authorEmail().trim(),
                request.content().trim()
        );
        comments.put(id, comment);
        return comment;
    }

    public Comment update(Long id, CommentRequest request) {
        findById(id);
        validatePost(request.postId());
        validateUniqueComment(request, id);
        Comment comment = new Comment(
                id,
                request.postId(),
                request.authorName().trim(),
                request.authorEmail().trim(),
                request.content().trim()
        );
        comments.put(id, comment);
        return comment;
    }

    public void delete(Long id) {
        if (comments.remove(id) == null) {
            throw new NotFoundException("Comentario no encontrado: " + id);
        }
    }

    public void deleteByPostId(Long postId) {
        comments.entrySet().removeIf(entry -> entry.getValue().postId().equals(postId));
    }

    private void validatePost(Long postId) {
        if (!postService.existsById(postId)) {
            throw new IllegalArgumentException("La publicacion " + postId + " no existe.");
        }
    }

    private void validateUniqueComment(CommentRequest request, Long currentId) {
        String normalizedEmail = request.authorEmail().trim();
        String normalizedContent = request.content().trim();

        boolean duplicateComment = comments.values().stream()
                .anyMatch(comment -> !comment.id().equals(currentId)
                        && comment.postId().equals(request.postId())
                        && comment.authorEmail().equalsIgnoreCase(normalizedEmail)
                        && comment.content().equalsIgnoreCase(normalizedContent));
        if (duplicateComment) {
            throw new ConflictException("Ya existe un comentario igual para esta publicacion.");
        }
    }
}
