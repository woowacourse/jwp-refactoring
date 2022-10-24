package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;

    private Long nonEmptyOrderTableId;

    @BeforeEach
    void setup() {
        nonEmptyOrderTableId = orderTableDao.save(new OrderTable(null, 1, false)).getId();
    }

    @DisplayName("주문 생성 기능")
    @Nested
    class CreateTest {

        @DisplayName("정상 작동")
        @Test
        void create() {
            final OrderLineItem orderLineItem = new OrderLineItem(null, 1L, 1L);
            final Order order = new Order(nonEmptyOrderTableId, null, null, List.of(orderLineItem));

            final Order savedOrder = orderService.create(order);

            assertThat(savedOrder.getId()).isNotNull();
        }

        @DisplayName("주문메뉴가 존재하지 않는 요청일 경우 예외가 발생한다.")
        @Test
        void create_Exception_NonExistOrderLineItem() {
            final Order order = new Order(nonEmptyOrderTableId, null, null, Collections.emptyList());

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문메뉴가 존재하지 않습니다.");
        }

        @DisplayName("주문받은 메뉴가 실제 저장되어 있는 메뉴에 속하지 않는다면 예외가 발생한다.")
        @Test
        void create_Exception_NotExistMenu() {
            final OrderLineItem orderLineItem = new OrderLineItem(null, 1000L, 1L);
            final Order order = new Order(nonEmptyOrderTableId, null, null, List.of(orderLineItem));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문받은 메뉴가 실제 저장되어 있는 메뉴에 속하지 않습니다.");
        }

        @DisplayName("주문테이블이 존재하지 않으면 예외가 발생한다.")
        @Test
        void create_Exception_NonExistsOrderTable() {
            final OrderLineItem orderLineItem = new OrderLineItem(null, 1L, 1L);
            final Order order = new Order(1000L, null, null, List.of(orderLineItem));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문테이블이 존재하지 않습니다.");
        }

        @DisplayName("주문테이블이 empty 상태면 예외가 발생한다.")
        @Test
        void create_Exception_EmptyOrderTable() {
            final Long emptyOrderTableId = orderTableDao.save(new OrderTable(null, 1, true)).getId();
            final OrderLineItem orderLineItem = new OrderLineItem(null, 1L, 1L);
            final Order order = new Order(emptyOrderTableId, null, null, List.of(orderLineItem));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문테이블이 주문을 받을수 없는 상태입니다.");
        }
    }

    @DisplayName("주문상태를 변경하는 기능")
    @Nested
    class ChangeOrderStatusTest {

        @DisplayName("저장된 주문상태가 계산완료이면 예외가 발생한다.")
        @Test
        void changeOrderStatus_Exception_CompletionStatus() {
            final OrderLineItem orderLineItem = new OrderLineItem(null, 1L, 1L);
            final Long savedOrderId = orderDao.save(
                    new Order(nonEmptyOrderTableId, OrderStatus.COMPLETION.name(), LocalDateTime.now(), List.of(orderLineItem))).getId();
            final Order order = new Order(null, OrderStatus.MEAL.name(), null, null);

            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문상태가 이미 식사완료면 상태를 바꾸지 못합니다.");
        }

        @DisplayName("상태를 변경하려는 주문이 저장되어 있지 않으면 예외가 발생한다.")
        @Test
        void changeOrderStatus_Exception_NonExistOrder() {
            final Order order = new Order(null, OrderStatus.MEAL.name(), null, null);

            assertThatThrownBy(() -> orderService.changeOrderStatus(1000L, order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("저장되어 있지 않은 주문입니다.");
        }
    }
}
