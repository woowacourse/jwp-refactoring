package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.order.OrderLineItemDto;
import kitchenpos.ui.dto.order.OrderRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    private static final long DEFAULT_QUANTITY = 1L;

    public static Order 조리_상태의_주문_엔티티_생성(final OrderTable orderTable, final Menu menu) {
        final Order 주문 = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        final OrderLineItem 주문_항목 = new OrderLineItem(주문, menu, DEFAULT_QUANTITY);
        주문.updateOrderLineItems(List.of(주문_항목));

        return 주문;
    }

    public static List<Order> 조리_상태의_주문_엔티티들_생성(final List<OrderTable> orderTables, final Menu menu) {
        final List<Order> 주문들 = new ArrayList<>();

        for (OrderTable orderTable : orderTables) {
            주문들.add(조리_상태의_주문_엔티티_생성(orderTable, menu));
        }

        return 주문들;
    }

    public static Order 식사_상태의_주문_엔티티_생성(final OrderTable orderTable, final Menu menu) {
        final Order 주문 = new Order(orderTable, OrderStatus.MEAL, LocalDateTime.now());
        final OrderLineItem 주문_항목 = new OrderLineItem(주문, menu, DEFAULT_QUANTITY);
        주문.updateOrderLineItems(List.of(주문_항목));

        return 주문;
    }

    public static Order 계산_완료_상태의_주문_생성(final OrderTable orderTable, final Menu menu) {
        final Order 주문 = new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.now());
        final OrderLineItem 주문_항목 = new OrderLineItem(주문, menu, DEFAULT_QUANTITY);
        주문.updateOrderLineItems(List.of(주문_항목));

        return 주문;
    }

    public static OrderRequest 조리_상태의_주문_요청_dto_생성(final OrderTable orderTable, final List<Menu> menus) {
        return new OrderRequest(orderTable.getId(), 주문_항목_dto_생성(menus));
    }

    private static List<OrderLineItemDto> 주문_항목_dto_생성(final List<Menu> menus) {
        return menus.stream()
                    .map(menu -> new OrderLineItemDto(menu.getId(), DEFAULT_QUANTITY))
                    .collect(Collectors.toList());
    }
}
