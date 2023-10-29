package kitchenpos.support.fixture.dto;

import kitchenpos.application.tablegroup.dto.TableGroupUngroupRequest;

public abstract class TableGroupUngroupRequestFixture {

    public static TableGroupUngroupRequest tableGroupUngroupRequest(final Long unGroupTableId) {
        return new TableGroupUngroupRequest(unGroupTableId);
    }
}
