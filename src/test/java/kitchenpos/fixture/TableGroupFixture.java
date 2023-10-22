package kitchenpos.fixture;

import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.domain.TableGroup;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 단체_지정_생성() {
        return TableGroup.createDefault();
    }

    public static TableGroupCreateRequest 단체_지정_생성_요청(final List<Long> orderTableIds) {
        return new TableGroupCreateRequest(orderTableIds);
    }
}
