package kitchenpos.application;

import kitchenpos.EntityFactory;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EntityFactory entityFactory;

    @Nested
    @DisplayName("주문 생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("주문을 생성할 수 있다")
        void create() {
            //given
            final OrderTable orderTable = entityFactory.saveOrderTableWithNotEmpty();
            final Menu menu = entityFactory.saveMenu();
            final OrderLineItem orderLineItem = createOrderLineItem(menu, 2);

            final Order request = new Order();
            request.setOrderTableId(orderTable.getId());
            request.setOrderLineItems(List.of(orderLineItem));

            //when
            final Order order = orderService.create(request);

            //then
            assertSoftly(softAssertions -> {
                assertThat(order.getId()).isNotNull();
                assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                assertThat(order.getOrderedTime()).isNotNull();
            });
        }

        @Test
        @DisplayName("주문을 생성할 때 주문 항목이 없으면 예외가 발생한다")
        void create_fail1() {
            //given
            final OrderTable orderTable = entityFactory.saveOrderTableWithNotEmpty();

            final Order request = new Order();
            request.setOrderTableId(orderTable.getId());

            //when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문을 생성할 때 주문 항목 메뉴의 갯수와 실제 존재하는 메뉴의 갯수가 다르면 예외가 발생한다")
        void create_fail2() {
            //given
            final OrderTable orderTable = entityFactory.saveOrderTableWithNotEmpty();
            final OrderLineItem orderLineItem = new OrderLineItem();

            final Order request = new Order();
            request.setOrderTableId(orderTable.getId());
            request.setOrderLineItems(List.of(orderLineItem, orderLineItem));

            //when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문을 생성할 때 주문 테이블이 존재하지 않으면 예외가 발생한다")
        void create_fail3() {
            //given
            final Menu menu = entityFactory.saveMenu();
            final OrderLineItem orderLineItem = createOrderLineItem(menu, 2);

            final Order request = new Order();
            request.setOrderLineItems(List.of(orderLineItem, orderLineItem));

            //when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문을 생성할 때 주문 테이블이 빈 테이블이면 예외가 발생한다")
        void create_fail4() {
            //given
            final OrderTable orderTable = entityFactory.saveOrderTableWithNotEmpty();
            final Menu menu = entityFactory.saveMenu();
            final OrderLineItem orderLineItem = createOrderLineItem(menu, 2);

            final Order request = new Order();
            request.setOrderTableId(orderTable.getId());
            request.setOrderLineItems(List.of(orderLineItem, orderLineItem));

            //when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("주문 전체 조회를 할 수 있다")
    void list() {
        assertDoesNotThrow(() -> orderService.list());
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트")
    class ChangeOrderStatusTest {

        @ParameterizedTest
        @EnumSource(OrderStatus.class)
        @DisplayName("주문 상태를 바꿀 수 있다")
        void changeOrderStatus(final OrderStatus orderStatus) {
            //given
            final Order order = entityFactory.saveOrder();

            final Order request = new Order();
            request.setOrderStatus(orderStatus.name());

            //when
            final Order saved = orderService.changeOrderStatus(order.getId(), request);

            //then
            assertSoftly(softAssertions -> {
                assertThat(saved.getId()).isEqualTo(order.getId());
                assertThat(saved.getOrderStatus()).isEqualTo(orderStatus.name());
            });
        }

        @Test
        @DisplayName("주문 상태를 바꾸려고 할 때 존재하지 않는 주문 상태이면 예외가 발생한다")
        void changeOrderStatus_fail() {
            //given
            final Order order = entityFactory.saveOrder();

            final Order request = new Order();
            request.setOrderStatus("NOTHING");

            //when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 상태를 바꾸려고 할 때 주문이 존재하지 않으면 예외가 발생한다")
        void changeOrderStatus_fail2() {
            //given
            final Order request = new Order();
            request.setOrderStatus("MEAL");

            //when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(0L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 상태를 바꾸려고 할 때 COMPLETION 상태의 주문이라면 예외가 발생한다")
        void changeOrderStatus_fail3() {
            //given
            final Order order = entityFactory.saveOrder();

            final Order requestToCompletion = new Order();
            requestToCompletion.setOrderStatus(OrderStatus.COMPLETION.name());
            orderService.changeOrderStatus(order.getId(), requestToCompletion);

            final Order request = new Order();
            request.setOrderStatus(OrderStatus.MEAL.name());

            //when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private OrderLineItem createOrderLineItem(final Menu menu, final int quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }
}
