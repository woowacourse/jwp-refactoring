package kitchenpos.fixture;

import java.time.LocalDateTime;

import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    public static TableGroup GROUP1 = new TableGroup(1L, LocalDateTime.now());
    public static TableGroup GROUP2 = new TableGroup(2L, LocalDateTime.now());
}
