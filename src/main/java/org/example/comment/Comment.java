package org.example.comment;

public record Comment(
        Long id,
        Long postId,
        String authorName,
        String authorEmail,
        String content
) {
}
