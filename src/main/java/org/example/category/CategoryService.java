package org.example.category;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Category> categories = new ConcurrentHashMap<>();

    public List<Category> findAll() {
        return categories.values().stream()
                .sorted(Comparator.comparing(Category::id))
                .toList();
    }

    public Category findById(Long id) {
        Category category = categories.get(id);
        if (category == null) {
            throw new NotFoundException("Categoria no encontrada: " + id);
        }
        return category;
    }

    public Category create(CategoryRequest request) {
        validateUniqueness(request, null);
        Long id = sequence.incrementAndGet();
        Category category = new Category(id, request.name().trim(), request.slug().trim(), normalize(request.description()));
        categories.put(id, category);
        return category;
    }

    public Category update(Long id, CategoryRequest request) {
        findById(id);
        validateUniqueness(request, id);
        Category category = new Category(id, request.name().trim(), request.slug().trim(), normalize(request.description()));
        categories.put(id, category);
        return category;
    }

    public void delete(Long id) {
        if (categories.remove(id) == null) {
            throw new NotFoundException("Categoria no encontrada: " + id);
        }
    }

    public boolean existsById(Long id) {
        return categories.containsKey(id);
    }

    private void validateUniqueness(CategoryRequest request, Long currentId) {
        String normalizedName = request.name().trim();
        String normalizedSlug = request.slug().trim();

        boolean duplicateName = categories.values().stream()
                .anyMatch(category -> !category.id().equals(currentId)
                        && category.name().equalsIgnoreCase(normalizedName));
        if (duplicateName) {
            throw new ConflictException("Ya existe una categoria con ese nombre.");
        }

        boolean duplicateSlug = categories.values().stream()
                .anyMatch(category -> !category.id().equals(currentId)
                        && category.slug().equalsIgnoreCase(normalizedSlug));
        if (duplicateSlug) {
            throw new ConflictException("Ya existe una categoria con ese slug.");
        }
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
