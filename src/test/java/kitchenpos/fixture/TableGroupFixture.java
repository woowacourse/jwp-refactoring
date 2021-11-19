package kitchenpos.fixture;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupFixture {

    private static final Long ID = 1L;
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now();

    public static TableGroup create() {
        TableGroup tableGroup = new TableGroup(ID, CREATED_DATE);

        return tableGroup;
    }
}
