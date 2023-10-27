package kitchenpos.ui;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.request.TableGroupCreateRequest;

public class RequestFixture {

    // 메뉴 그룹
    public static MenuGroupCreateRequest menuGroupCreateRequest() {
        return new MenuGroupCreateRequest("추천메뉴");
    }

    // 메뉴
    public static MenuCreateRequest menuCreateRequest() {
        return new MenuCreateRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                1,
                List.of(new MenuProductRequest(1, 2))
        );
    }

    // 주문
    public static OrderCreateRequest orderCreateRequest() {
        return new OrderCreateRequest(
                1,
                List.of(new OrderLineItemRequest(1, 1))
        );
    }

    public static OrderStatusChangeRequest orderStatusChangeRequest_MEAL() {
        return new OrderStatusChangeRequest("MEAL");
    }

    public static OrderStatusChangeRequest orderStatusChangeRequest_COMPLETION() {
        return new OrderStatusChangeRequest("COMPLETION");
    }

    // 상품
    public static ProductCreateRequest productCreateRequest() {
        return new ProductCreateRequest("강정치킨", BigDecimal.valueOf(17000));
    }

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
