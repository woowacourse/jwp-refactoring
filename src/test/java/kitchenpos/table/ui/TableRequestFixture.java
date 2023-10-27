package kitchenpos.table.ui;

import java.util.List;
import kitchenpos.table.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.table.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.request.OrderTableRequest;
import kitchenpos.table.application.dto.request.TableGroupCreateRequest;

public class TableRequestFixture {

    // 테이블 그룹
    public static TableGroupCreateRequest tableGroupCreateRequest() {
        return new TableGroupCreateRequest(List.of(new OrderTableRequest(1), new OrderTableRequest(2)));
    }

    // 테이블
    public static OrderTableCreateRequest orderTableCreateRequest() {
        return new OrderTableCreateRequest(0, true);
    }

    public static OrderTableChangeEmptyRequest orderTableChangeEmptyRequest_false() {
        return new OrderTableChangeEmptyRequest(false);
    }

    public static OrderTableChangeEmptyRequest orderTableChangeEmptyRequest_true() {
        return new OrderTableChangeEmptyRequest(true);
    }

    public static OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest() {
        return new OrderTableChangeNumberOfGuestsRequest(4);
    }
}
