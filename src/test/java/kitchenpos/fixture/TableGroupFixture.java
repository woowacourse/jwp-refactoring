package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.assertj.core.util.Lists;

import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableId;
import kitchenpos.dto.request.TableGroupCreateRequest;

public class TableGroupFixture {
    public static final Long ID1 = 1L;

    public static TableGroupCreateRequest createOneTableRequest() {
        return new TableGroupCreateRequest(
            Collections.singletonList(new OrderTableId(OrderTableFixture.ID1)));
    }

    public static TableGroupCreateRequest createRequest() {
        return new TableGroupCreateRequest(Arrays.asList(new OrderTableId(OrderTableFixture.ID1),
            new OrderTableId(OrderTableFixture.ID2)));
    }

    public static TableGroupCreateRequest createEmptyRequest() {
        return new TableGroupCreateRequest(Lists.emptyList());
    }

    public static TableGroup createWithoutId() {
        return new TableGroup(null, LocalDateTime.now());
    }

    public static TableGroup createWithId(Long id) {
        return new TableGroup(id, LocalDateTime.now());
    }
}
