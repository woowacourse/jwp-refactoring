package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.common.vo.OrderStatus;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.ui.dto.OrderLineItemDto;
import kitchenpos.order.ui.dto.OrderRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    private static final long DEFAULT_QUANTITY = 1L;

    public static Order 조리_상태의_주문_엔티티_생성(final OrderTable orderTable, final Menu menu) {
        return 주문_생성(orderTable, menu, OrderStatus.COOKING);
    }

    public static List<Order> 조리_상태의_주문_엔티티들_생성(final List<OrderTable> orderTables, final Menu menu) {
        final List<Order> 주문들 = new ArrayList<>();

        for (OrderTable orderTable : orderTables) {
            주문들.add(조리_상태의_주문_엔티티_생성(orderTable, menu));
        }

        return 주문들;
    }

    public static Order 식사_상태의_주문_엔티티_생성(final OrderTable orderTable, final Menu menu) {
        return 주문_생성(orderTable, menu, OrderStatus.MEAL);
    }

    public static Order 계산_완료_상태의_주문_생성(final OrderTable orderTable, final Menu menu) {
        return 주문_생성(orderTable, menu, OrderStatus.COMPLETION);
    }

    private static Order 주문_생성(final OrderTable orderTable, final Menu menu, final OrderStatus orderStatus) {
        final OrderLineItem 주문_항목 = new OrderLineItem(menu.getId(), DEFAULT_QUANTITY);
        final OrderLineItems orderLineItems = new OrderLineItems(List.of(주문_항목));
        final Order 주문 = Order.of(orderTable.getId(), orderStatus, LocalDateTime.now(), orderLineItems);

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
