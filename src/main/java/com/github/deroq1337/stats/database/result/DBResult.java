package com.github.deroq1337.stats.database.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class DBResult {

    private final List<DBRow> rows;
}
