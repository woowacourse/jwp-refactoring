package kitchenpos.order.apllication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.vo.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
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
            order.setOrderTable(savedOrderTable.id());

            // when
            final Order saved = orderService.create(order);

            // then
            assertSoftly(softly -> {
                softly.assertThat(saved.id()).isEqualTo(saved.id());
                softly.assertThat(saved.orderTable()).isEqualTo(order.orderTable());
                softly.assertThat(saved.orderStatus()).isEqualTo(order.orderStatus());
                softly.assertThat(saved.orderedTime().truncatedTo(ChronoUnit.MINUTES))
                        .isEqualTo(order.orderedTime().truncatedTo(ChronoUnit.MINUTES));
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
            order.setOrderTable(-1L);

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
            order.setOrderTable(savedOrderTable.id());

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
        order.setOrderTable(savedOrderTable.id());

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
            final Order order = OrderFixtures.BASIC.get();
            order.setOrderTable(savedOrderTable.id());
            order.setOrderStatus(OrderStatus.MEAL.name());
            final Order savedOrder = orderService.create(order);

            final Order newOrder = OrderFixtures.EMPTY.get();


            // when
            final Order changed = orderService.changeOrderStatus(savedOrder.id(), newOrder);

            // then
            assertThat(changed.orderStatus()).isEqualTo(newOrder.orderStatus());
        }

        @Test
        @DisplayName("현재 주문이 존재하지 않을 시 예외 처리")
        void orderNotExistException() {
            // given
            final Order order = OrderFixtures.BASIC.get();

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.changeOrderStatus(null, order));
        }

        @Test
        @DisplayName("현재 주문 상태가 완료 상태일 시 예외 처리")
        void orderStatusCompletionException() {
            // given
            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.NOT_EMPTY.get());
            final Order order = OrderFixtures.BASIC.get();
            order.setOrderTable(savedOrderTable.id());

            final Order savedOrder = orderService.create(order);
            savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
            orderDao.save(savedOrder);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.changeOrderStatus(savedOrder.id(), savedOrder));
        }
    }
}
