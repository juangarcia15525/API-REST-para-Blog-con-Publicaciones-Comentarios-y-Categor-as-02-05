package org.example.post;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.example.category.CategoryService;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final CategoryService categoryService;

    public PostService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public List<Post> findAll() {
        return posts.values().stream()
                .sorted(Comparator.comparing(Post::id))
                .toList();
    }

    public Post findById(Long id) {
        Post post = posts.get(id);
        if (post == null) {
            throw new NotFoundException("Publicacion no encontrada: " + id);
        }
        return post;
    }

    public Post create(PostRequest request) {
        validateCategories(request.categoryIds());
        validateUniqueSlug(request.slug(), null);
        Long id = sequence.incrementAndGet();
        Post post = new Post(
                id,
                request.title().trim(),
                request.slug().trim(),
                request.content().trim(),
                List.copyOf(request.categoryIds())
        );
        posts.put(id, post);
        return post;
    }

    public Post update(Long id, PostRequest request) {
        findById(id);
        validateCategories(request.categoryIds());
        validateUniqueSlug(request.slug(), id);
        Post post = new Post(
                id,
                request.title().trim(),
                request.slug().trim(),
                request.content().trim(),
                List.copyOf(request.categoryIds())
        );
        posts.put(id, post);
        return post;
    }

    public void delete(Long id) {
        if (posts.remove(id) == null) {
            throw new NotFoundException("Publicacion no encontrada: " + id);
        }
    }

    public boolean existsById(Long id) {
        return posts.containsKey(id);
    }

    private void validateCategories(List<Long> categoryIds) {
        for (Long categoryId : categoryIds) {
            if (!categoryService.existsById(categoryId)) {
                throw new IllegalArgumentException("La categoria " + categoryId + " no existe.");
            }
        }
    }

    private void validateUniqueSlug(String slug, Long currentId) {
        String normalizedSlug = slug.trim();
        boolean duplicateSlug = posts.values().stream()
                .anyMatch(post -> !post.id().equals(currentId)
                        && post.slug().equalsIgnoreCase(normalizedSlug));
        if (duplicateSlug) {
            throw new ConflictException("Ya existe una publicacion con ese slug.");
        }
    }
}
