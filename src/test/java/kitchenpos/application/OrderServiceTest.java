package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.test.fixtures.OrderFixtures;
import kitchenpos.test.fixtures.OrderLineItemFixtures;
import kitchenpos.test.fixtures.OrderTableFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @Autowired
    TableService tableService;

    @Autowired
    OrderDao orderDao;

    @Nested
    @DisplayName("주문을 등록할 때,")
    class CreateOrder {
        @Test
        @DisplayName("정상 등록된다.")
        void create() {
            // given
            final Order order = OrderFixtures.BASIC.get();
            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.NOT_EMPTY.get());
            order.setOrderTableId(savedOrderTable.getId());

            // when
            final Order saved = orderService.create(order);

            // then
            assertSoftly(softly -> {
                softly.assertThat(saved.getId()).isEqualTo(saved.getId());
                softly.assertThat(saved.getOrderTableId()).isEqualTo(order.getOrderTableId());
                softly.assertThat(saved.getOrderStatus()).isEqualTo(order.getOrderStatus());
                softly.assertThat(saved.getOrderedTime().truncatedTo(ChronoUnit.MINUTES))
                        .isEqualTo(order.getOrderedTime().truncatedTo(ChronoUnit.MINUTES));
            });
        }

        @Test
        @DisplayName("주문 메뉴 목록이 비어있을 시 예외 발생")
        void orderLineItemsEmptyException() {
            // given
            final Order order = OrderFixtures.BASIC.get();
            order.setOrderLineItems(Collections.emptyList());

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.create(order));
        }

        @Test
        @DisplayName("주문 메뉴 개수와 실제 주문 메뉴 개수가 불일치 할 시 예외 발생")
        void orderLineItemsCountWrongException() {
            // given
            final Order order = OrderFixtures.BASIC.get();
            order.setOrderLineItems(List.of(OrderLineItemFixtures.BASIC.get(), OrderLineItemFixtures.BASIC.get()));

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.create(order));
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않을 시 예외 발생")
        void orderTableNotExistException() {
            // given
            final Order order = OrderFixtures.BASIC.get();
            order.setOrderTableId(-1L);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.create(order));
        }

        @Test
        @DisplayName("주문 테이블이 비어있을 시 예외 발생")
        void orderTableEmptyException() {
            // given
            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.EMPTY.get());

            final Order order = OrderFixtures.BASIC.get();
            order.setOrderTableId(savedOrderTable.getId());

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.create(order));
        }
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다")
    void getOrders() {
        // given
        final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.NOT_EMPTY.get());

        final Order order = OrderFixtures.BASIC.get();
        order.setOrderTableId(savedOrderTable.getId());

        orderService.create(order);

        // when
        final List<Order> list = orderService.list();

        // then
        assertThat(list).isNotEmpty();
    }

    @Nested
    @DisplayName("주문 상태를 변경할 때,")
    class ChangeOrderStatus {
        @Test
        @DisplayName("정상 변경된다")
        void changeOrderStatus() {
            // given
            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.NOT_EMPTY.get());
            final Order orderToSave = OrderFixtures.BASIC.get();
            orderToSave.setOrderTableId(savedOrderTable.getId());
            orderToSave.setOrderStatus(OrderStatus.MEAL.name());
            final Order savedOrder = orderService.create(orderToSave);

            final Order newOrder = OrderFixtures.EMPTY.get();


            // when
            final Order changed = orderService.changeOrderStatus(savedOrder.getId(), newOrder);

            // then
            assertThat(changed.getOrderStatus()).isEqualTo(newOrder.getOrderStatus());
        }

        @Test
        @DisplayName("현재 주문이 존재하지 않을 시 예외 처리")
        void orderNotExistException() {
            // given
            final Order order = OrderFixtures.BASIC.get();

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), order));
        }

        @Test
        @DisplayName("현재 주문 상태가 완료 상태일 시 예외 처리")
        void orderStatusCompletionException() {
            // given
            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.NOT_EMPTY.get());
            final Order order = OrderFixtures.BASIC.get();
            order.setOrderTableId(savedOrderTable.getId());

            final Order savedOrder = orderService.create(order);
            savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
            orderDao.save(savedOrder);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), savedOrder));
        }
    }
}
