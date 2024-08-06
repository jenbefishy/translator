package com.lab.translator.repository;

import com.lab.translator.model.TranslationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TranslationResponseRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TranslationResponseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TranslationResponse> getAll() {
        return jdbcTemplate.query("SELECT * FROM translation_requests", new TranslationRequestRowMapper());
    }

    public void save(TranslationResponse translationResponse) {
        jdbcTemplate.update(
                "INSERT INTO translation_requests (user_ip, source_text, target_text) VALUES (?, ?, ?)",
                translationResponse.getUserIp(), translationResponse.getSourceText(), translationResponse.getTargetText()
        );
    }

    private static class TranslationRequestRowMapper implements RowMapper<TranslationResponse> {
        @Override
        public TranslationResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TranslationResponse(
                    rs.getLong("id"),
                    rs.getString("user_ip"),
                    rs.getString("source_text"),
                    rs.getString("target_text")
            );
        }
    }
}
