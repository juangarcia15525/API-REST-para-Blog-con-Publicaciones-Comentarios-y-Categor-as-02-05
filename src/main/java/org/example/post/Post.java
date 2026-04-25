package org.example.post;

import java.util.List;

public record Post(
        Long id,
        String title,
        String slug,
        String content,
        List<Long> categoryIds
) {
}
