package com.lab.translator.repository;

import com.lab.translator.model.TranslationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TranslationResponseRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TranslationResponseRepository translationResponseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        TranslationResponse response = new TranslationResponse(1L, "127.0.0.1", "hello", "hola");
        when(jdbcTemplate.query(any(String.class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(response));

        List<TranslationResponse> responses = translationResponseRepository.getAll();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(response, responses.get(0));
    }

    @Test
    void testSave() {
        TranslationResponse response = new TranslationResponse(1L, "127.0.0.1", "hello", "hola");

        translationResponseRepository.save(response);

        verify(jdbcTemplate).update(
                "INSERT INTO translation_requests (user_ip, source_text, target_text) VALUES (?, ?, ?)",
                response.getUserIp(), response.getSourceText(), response.getTargetText()
        );
    }

}
