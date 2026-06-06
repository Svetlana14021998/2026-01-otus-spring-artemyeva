package ru.otus.hw.migration.table;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class MigrationTableService {

    private static final String MERGE_SQL = """
        merge into %s (source_id, target_id)
        values (:sourceId, :targetId)""";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void saveMapping(MigrationTable table, List<Long> sourceIds, List<String> targetIds) {
        if (sourceIds.size() != targetIds.size()) {
            throw new IllegalArgumentException(
                "Size mismatch for table %s: sourceIds=%d, targetIds=%d"
                    .formatted(table.getMigrationTableName(), sourceIds.size(), targetIds.size())
            );
        }

        String sql = MERGE_SQL.formatted(table.getMigrationTableName());
        Map[] batchValues = IntStream.range(0, sourceIds.size())
            .mapToObj(i -> Map.of("sourceId", sourceIds.get(i), "targetId", targetIds.get(i)))
            .toArray(Map[]::new);

        jdbcTemplate.batchUpdate(sql, batchValues);
    }

    public Map<Long, String> getAllMappingsAsMap(MigrationTable table) {
        String sql = "SELECT source_id, target_id FROM %s".formatted(table.getMigrationTableName());

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, Map.of());

        return results.stream().collect(Collectors.toMap(
            row -> (Long) row.get("source_id"),
            row -> (String) row.get("target_id")
        ));
    }
}
