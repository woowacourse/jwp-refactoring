package kitchenpos.order.domain;

import static fixture.MenuFixtures.후라이드치킨_메뉴;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("빈 주문 목록으로 주문을 생성할 수 없다.")
    @Test
    void createOrderByEmptyOrderLineItems() {
        assertThatThrownBy(() -> new Order(1L, LocalDateTime.now(), List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산이 완료된 주문의 상태는 변경할 수 없다.")
    @Test
    void changeCompletionOrderStatus() {
        // arrange
        List<OrderLineItem> items = List.of(new OrderLineItem(후라이드치킨_메뉴.id(), 1L));
        Order order = new Order(1L, LocalDateTime.now(), items);
        order.changeStatus(MEAL);
        order.changeStatus(COMPLETION);

        // act
        assertThatThrownBy(() -> order.changeStatus(MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("제조 상태에서는 계산 완료 상태로 변경할 수 없다.")
    @Test
    void changeCookingToCompletion() {
        // arrange
        List<OrderLineItem> items = List.of(new OrderLineItem(후라이드치킨_메뉴.id(), 1L));
        Order order = new Order(1L, LocalDateTime.now(), items);

        // act
        assertThatThrownBy(() -> order.changeStatus(COMPLETION))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("제조 상태에서는 식사 상태로 변경할 수 있다.")
    @Test
    void changeCookingToMeal() {
        // arrange
        List<OrderLineItem> items = List.of(new OrderLineItem(후라이드치킨_메뉴.id(), 1L));
        Order order = new Order(1L, LocalDateTime.now(), items);

        // act
        order.changeStatus(MEAL);

        // assert
        assertThat(order.getOrderStatus()).isEqualTo(MEAL);
    }

    @DisplayName("식사 상태에서 제조 상태로 변경할 수 없다.")
    @Test
    void changeMealToCooking() {
        // arrange
        List<OrderLineItem> items = List.of(new OrderLineItem(후라이드치킨_메뉴.id(), 1L));
        Order order = new Order(1L, LocalDateTime.now(), items);
        order.changeStatus(MEAL);

        // act & assert
        assertThatThrownBy(() -> order.changeStatus(COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("식사 상태에서 계산완료 상태로 변경할 수 있다.")
    @Test
    void changeMealToCompletion() {
        // arrange
        List<OrderLineItem> items = List.of(new OrderLineItem(후라이드치킨_메뉴.id(), 1L));
        Order order = new Order(1L, LocalDateTime.now(), items);
        order.changeStatus(MEAL);

        // act
        order.changeStatus(COMPLETION);

        // assert
        assertThat(order.getOrderStatus()).isEqualTo(COMPLETION);
    }
}
