package kitchenpos.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.OrderFixture.COMPLETION_ORDER;
import static kitchenpos.fixture.OrderFixture.COOKING_ORDER;
import static kitchenpos.fixture.OrderLineItemFixture.양념반_후라이드반_하나;
import static kitchenpos.fixture.OrderLineItemFixture.후라이드_단품_둘;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order 단위 테스트")
class OrderTest {

    @Test
    @DisplayName("주문을 생성할 수 있다")
    void create() {
        // given
        OrderTable orderTable = new OrderTable(5, false);

        // when & then
        assertDoesNotThrow(() -> {
            Order order = new Order(orderTable, OrderStatus.COOKING, Arrays.asList(후라이드_단품_둘, 양념반_후라이드반_하나));
            assertNotNull(order.getOrderTable());
            assertEquals(OrderStatus.COOKING, order.getOrderStatus());
            assertNotNull(order.getOrderedTime());
        });
    }

    @Test
    @DisplayName("메뉴 목록이 없는 경우 주문을 생성할 수 없다.")
    void emptyOrderLineItem() {
        // given
        List<OrderLineItem> orderLineItems = Collections.emptyList();

        // when & then
        assertThatThrownBy(() -> new Order(new OrderTable(), orderLineItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문하려면 하나 이상의 메뉴가 필요합니다.");
    }

    @Test
    @DisplayName("주문하려는 테이블이 비어있으면 주문을 생성할 수 없다.")
    void emptyOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(0, true);

        // when & then
        assertThatThrownBy(() -> new Order(orderTable, Collections.singletonList(후라이드_단품_둘)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블은 주문할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        Order order = COOKING_ORDER;

        // when
        order.changeOrderStatus(OrderStatus.MEAL.name());

        // then
        assertEquals(OrderStatus.MEAL, order.getOrderStatus());

    }

    @Test
    @DisplayName("주문 상태가 COMPLETION이면 주문 상태를 변경할 수 없다.")
    void notExistOrder() {
        // given
        Order order = COMPLETION_ORDER;

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("계산 완료된 주문의 상태는 변경할 수 없습니다.");
    }
}
