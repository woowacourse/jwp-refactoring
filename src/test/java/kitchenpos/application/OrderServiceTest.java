package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("주문 항목 없는 주문은 있을 수 없다.")
    void createNoOrderLineItems() {
        assertThatThrownBy(() -> 주문_요청한다(손님있는_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴는 주문할 수 없다.")
    void createNoExistMenu() {
        final Menu notExistMenu = new Menu(-1L, "없는메뉴", BigDecimal.valueOf(999_999L), -1L);

        assertThatThrownBy(() -> 주문_요청한다(손님있는_테이블, notExistMenu.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블에선 주문할 수 없다.")
    void createNoExistTable() {
        final OrderTable notExistTable = OrderTable.create();

        assertThatThrownBy(() -> 주문_요청한다(notExistTable, 파스타한상.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 저장한다.")
    void create() {
        final Order order = 주문_요청한다(손님있는_테이블, 파스타한상.getId());

        final List<OrderLineItem> savedOrderLineItems = order.getOrderLineItems();

        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrderLineItems).extracting("menuId")
                        .containsExactly(파스타한상.getId())
        );
    }

    @Test
    @DisplayName("주문을 식사 상태로 변경한다.")
    void changeOrderStatusMeal() {
        final Order order = 주문_요청한다(손님있는_테이블, 파스타한상.getId());

        final Order mealOrder = 주문을_식사_상태로_만든다(order);

        assertThat(mealOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문을 완료 상태로 변경한다.")
    void changeOrderStatusComplete() {
        final Order order = 주문_요청한다(손님있는_테이블, 파스타한상.getId());

        final Order completedOrder = 주문을_완료_상태로_만든다(order);

        assertThat(completedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문의 상태가 완료상태면 상태를 변경할 수 없다.")
    void changeOrderStatusFail() {
        final Order order = 주문_요청한다(손님있는_테이블, 파스타한상.getId());

        final Order completedOrder = 주문을_완료_상태로_만든다(order);

        assertThatThrownBy(() -> 주문을_완료_상태로_만든다(completedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
