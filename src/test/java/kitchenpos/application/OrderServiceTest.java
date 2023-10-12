package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.*;
import kitchenpos.fixtures.domain.OrderFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixtures.domain.MenuProductFixture.createMenuProduct;
import static kitchenpos.fixtures.domain.OrderFixture.createOrder;
import static kitchenpos.fixtures.domain.OrderLineItemFixture.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest extends ServiceTest{

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    private OrderTable savedOrderTable;
    private MenuGroup savedMenuGroup;
    private Product savedProduct;
    private Menu savedMenu;

    private OrderLineItem createdOrderLineItem;

    @BeforeEach
    void setUp() {
        savedOrderTable = saveOrderTable(10, false);
        savedMenuGroup = saveMenuGroup("메뉴 그룹");
        savedProduct = saveProduct("상품", 5_000);
        savedMenu = saveMenu("메뉴", 10_000, savedMenuGroup, List.of(
                createMenuProduct(savedProduct.getId(), 10)
        ));
        createdOrderLineItem = createOrderLineItem(savedMenu.getId(), 10);
    }

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("주문을 생성한다.")
        @Test
        void Should_CreateOrder() {
            // given
            final Order order = new OrderFixture.OrderRequestBuilder()
                    .orderTableId(savedOrderTable.getId())
                    .addOrderLineItem(createdOrderLineItem)
                    .build();

            // when
            final Order actual = orderService.create(order);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getOrderStatus()).isEqualTo(order.getOrderStatus());
                assertThat(actual.getOrderedTime()).isEqualTo(order.getOrderedTime());
            });
        }

        @DisplayName("주문의 주문 항목이 비어있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderLineItemsIsEmpty() {
            // given
            final Order order = new OrderFixture.OrderRequestBuilder()
                    .orderTableId(savedOrderTable.getId())
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 항목의 메뉴가 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuDoesNotExist() {
            // given
            final OrderLineItem orderLineItemHasNotSavedMenu = createOrderLineItem(savedMenu.getId() + 1, 1L);

            final Order order = new OrderFixture.OrderRequestBuilder()
                    .orderTableId(savedOrderTable.getId())
                    .addOrderLineItem(orderLineItemHasNotSavedMenu)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given
            final Order order = new OrderFixture.OrderRequestBuilder()
                    .orderTableId(savedOrderTable.getId() + 1)
                    .addOrderLineItem(createdOrderLineItem)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 빈 테이블이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableIsEmpty() {
            // given
            final OrderTable emptyOrderTable = saveOrderTable(10, true);

            final Order order = new OrderFixture.OrderRequestBuilder()
                    .orderTableId(emptyOrderTable.getId())
                    .addOrderLineItem(createdOrderLineItem)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {

        @DisplayName("저장된 모든 주문 목록을 조회한다.")
        @Test
        void Should_ReturnAllOrderList() {
            // given
            final int expected = 3;
            for (int i = 0; i < expected; i++) {
                final Order order = createOrder(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                        List.of(createdOrderLineItem));
                orderDao.save(order);
            }

            // when
            final List<Order> actual = orderService.list();

            // then
            assertThat(actual).hasSize(expected);
        }
    }

    @DisplayName("changeOrderStatus 메소드는")
    @Nested
    class ChangeOrderStatusMethod {

        @DisplayName("주문의 주문 상태를 변경한다.")
        @ValueSource(strings = {"MEAL", "COMPLETION"})
        @ParameterizedTest
        void Should_ChangeOrderStatus(final String after) {
            // given
            final Order oldOrder = orderDao.save(
                    createOrder(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                            List.of(createdOrderLineItem)));

            final Order request = new OrderFixture.OrderRequestBuilder()
                    .orderStatus(after)
                    .build();

            // when
            final Order actual = orderService.changeOrderStatus(oldOrder.getId(), request);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(after);
        }

        @DisplayName("주문이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderDoesNotExist() {
            // given
            final Order order = orderDao.save(createOrder(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                    List.of(createdOrderLineItem)));

            final Order orderRequest = new OrderFixture.OrderRequestBuilder()
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId() + 1, orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 상태가 COMPLETION 이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderStatusIsCompletion() {
            // given
            final Order completionOrder = orderDao.save(
                    createOrder(savedOrderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now(),
                            List.of(createdOrderLineItem)));

            final Order request = new OrderFixture.OrderRequestBuilder()
                    .orderStatus(OrderStatus.COOKING)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrder.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
