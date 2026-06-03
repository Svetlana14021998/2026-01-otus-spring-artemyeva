package ru.otus.hw.migration.table;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "step", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MigrationContext {

    private final List<Long> currentSourceIds = new ArrayList<>();

    @Getter
    @Setter
    private MigrationTable currentTable;

    public void addSourceId(Long id) {
        currentSourceIds.add(id);
    }

    public List<Long> getAndClearSourceIds() {
        List<Long> copy = new ArrayList<>(currentSourceIds);
        currentSourceIds.clear();
        return copy;
    }
}
