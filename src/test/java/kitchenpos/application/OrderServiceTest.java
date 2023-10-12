package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends IntegrationTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 주문_등록_성공_저장() {
        // given
        final OrderTable table = tableService.list().get(0);
        table.setEmpty(false);
        tableService.changeEmpty(table.getId(), table);

        // when
        final Order order = new Order();
        order.setOrderStatus(MEAL.name());
        order.setOrderTableId(table.getId());
        final OrderLineItem orderLineItem = new OrderLineItem();
        final Menu menuToOrder = menuService.list().get(0);
        orderLineItem.setMenuId(menuToOrder.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        final Order saved = orderService.create(order);

        // then
        assertThat(orderService.list())
                .map(Order::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
    }

    @Test
    @DisplayName("새로 등록된 주문의 상태는 '조리' 상태이다.")
    void 주문_등록_성공_주문_상태_조리() {
        // given
        final OrderTable table = tableService.list().get(0);
        table.setEmpty(false);
        tableService.changeEmpty(table.getId(), table);

        // when
        final Order order = new Order();
        order.setOrderStatus(MEAL.name());
        order.setOrderTableId(table.getId());
        final OrderLineItem orderLineItem = new OrderLineItem();
        final Menu menuToOrder = menuService.list().get(0);
        orderLineItem.setMenuId(menuToOrder.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        final Order saved = orderService.create(order);

        // then
        assertThat(orderService.list())
                .filteredOn(found -> Objects.equals(found.getId(), saved.getId()))
                .filteredOn(found -> COOKING.name().equals(found.getOrderStatus()))
                .hasSize(1);
    }

    @Test
    @DisplayName("주문 등록 시 주문 항목의 개수는 최소 1개 이상이다.")
    void 주문_등록_실패_주문_항목_개수_미달() {
        // given
        final OrderTable table = tableService.list().get(0);
        table.setEmpty(false);
        tableService.changeEmpty(table.getId(), table);

        // when
        final Order order = new Order();
        order.setOrderStatus(MEAL.name());
        order.setOrderTableId(table.getId());

        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
