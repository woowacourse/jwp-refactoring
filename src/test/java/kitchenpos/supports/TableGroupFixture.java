package kitchenpos.supports;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.TableResponse;

public class TableGroupFixture {

    public static TableGroupRequest from(final TableResponse... orderTables) {
        final List<Long> tableIds = Arrays.stream(orderTables).map(TableResponse::getId).collect(Collectors.toList());
        return new TableGroupRequest(tableIds);
    }
}
