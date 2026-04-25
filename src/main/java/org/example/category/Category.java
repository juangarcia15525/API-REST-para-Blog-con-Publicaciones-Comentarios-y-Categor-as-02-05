package org.example.category;

public record Category(
        Long id,
        String name,
        String slug,
        String description
) {
}
