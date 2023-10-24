package kitchenpos.support.fixture.dto;

import kitchenpos.ui.dto.tablegroup.TableGroupUngroupRequest;

public class TableGroupUngroupRequestFixture {

    public static TableGroupUngroupRequest tableGroupUngroupRequest(final Long unGroupTableId) {
        return new TableGroupUngroupRequest(unGroupTableId);
    }
}
