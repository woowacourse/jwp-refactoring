package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableGroupCreateRequest;

public class RequestParser {

    public static TableGroupCreateRequest from(final List<OrderTable> entities) {
        final List<Long> ids = entities.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return new TableGroupCreateRequest(ids);
    }
}
