package org.example;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateCategoryAndPostWithHeaders() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Backend",
                                  "slug": "backend",
                                  "description": "Contenido tecnico"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-API-Version", "1"))
                .andExpect(header().string("Location", "/api/categories/1"))
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Primera publicacion",
                                  "slug": "primera-publicacion",
                                  "content": "Este contenido supera sin problema la longitud minima.",
                                  "categoryIds": [1]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-Resource-Type", "post"))
                .andExpect(header().string("Location", "/api/posts/1"))
                .andExpect(jsonPath("$.categoryIds[0]").value(1));
    }

    @Test
    void shouldRejectInvalidPostContent() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Corto",
                                  "slug": "corto",
                                  "content": "breve",
                                  "categoryIds": [99]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("X-Error-Code", "VALIDATION_ERROR"))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldExposeCollectionHeaders() throws Exception {
        mockMvc.perform(get("/api/comments"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-API-Version", "1"))
                .andExpect(header().string("X-Total-Count", "0"));
    }
}
