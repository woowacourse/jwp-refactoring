package kitchenpos.fixture;

import java.time.LocalDateTime;

import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    public static TableGroup group1 = new TableGroup(1L, LocalDateTime.now());
    public static TableGroup group2 = new TableGroup(2L, LocalDateTime.now());
}
