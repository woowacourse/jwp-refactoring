package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 단체_지정(Long tableGroupId) {
        return new TableGroup(tableGroupId, LocalDateTime.MAX);
    }

    public static TableGroup 단체_지정() {
        return new TableGroup(LocalDateTime.MAX);
    }
}
