package kitchenpos.support.fixture.dto;

import kitchenpos.application.dto.tablegroup.TableGroupUngroupRequest;

public abstract class TableGroupUngroupRequestFixture {

    public static TableGroupUngroupRequest tableGroupUngroupRequest(final Long unGroupTableId) {
        return new TableGroupUngroupRequest(unGroupTableId);
    }
}
