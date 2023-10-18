package kitchenpos.application.order;

import kitchenpos.application.OrderService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderServiceTest extends ApplicationTestConfig {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository);
    }

    @DisplayName("새로운 주문 등록")
    @Nested
    class CreateNestedTest {

        @DisplayName("[SUCCESS] 새로운 주문을 등록한다.")
        @Test
        void success_create() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = createTableOrder(5, false);

            final Order expected = new Order(
                    savedOrderTable,
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    new ArrayList<>()
            );
            final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, savedMenu, 10));
            expected.addOrderLineItems(orderLineItems);

            // when
            final Order actual = orderService.create(expected);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getOrderTable()).isEqualTo(expected.getOrderTable());
                softly.assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
                softly.assertThat(actual.getOrderedTime()).isEqualTo(expected.getOrderedTime());
                softly.assertThat(actual.getOrderLineItems())
                        .usingRecursiveComparison()
                        .isEqualTo(expected.getOrderLineItems());
            });
        }

        @DisplayName("[EXCEPTION] 주문 항목 목록이 비어있는 경우 예외가 발생한다.")
        @Test
        void throwException_create_order_when_orderLineItemsIsEmpty() {
            // given
            final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, 5, false));

            // when
            final List<OrderLineItem> wrongOrderLineItems = Collections.emptyList();
            final Order expected = new Order(
                    savedOrderTable,
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    wrongOrderLineItems
            );

            // then
            assertThatThrownBy(() -> orderService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 항목 목록 수량이 실제 메뉴의 수량과 다를 경우 예외가 발생한다.")
        @Test
        void throwException_create_order_when_orderLineItemsSize_IsNotEqualTo_menuCountsSize() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, 5, false));

            // when
            final List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(null, savedMenu, 10),
                    new OrderLineItem(null, savedMenu, 10),
                    new OrderLineItem(null, savedMenu, 10)
            );

            final Order expected = new Order(
                    savedOrderTable,
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    orderLineItems
            );

            // then
            assertThatThrownBy(() -> orderService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 테이블이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void throwException_create_order_when_orderTableIsNotExists() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = createTableOrder(10, true);

            // when
            final Order expected = new Order(
                    savedOrderTable,
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    Collections.emptyList()
            );
            final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, savedMenu, 10));
            expected.addOrderLineItems(orderLineItems);

            // then
            assertThatThrownBy(() -> orderService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private OrderTable createTableOrder(final int numberOfGuests, final boolean empty) {
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), Collections.emptyList()));
        return orderTableRepository.save(new OrderTable(savedTableGroup, numberOfGuests, empty));
    }

    private Menu createMenu() {
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("테스트용 메뉴 그룹명"));
        return menuRepository.save(new Menu(
                "테스트용 메뉴명",
                new Price("0"),
                savedMenuGroup,
                Collections.emptyList()
        ));
    }

    @DisplayName("주문 상태 변경")
    @Nested
    class ChangeOrderStatus {

        @DisplayName("[SUCCESS] 주문 상태를 변경한다.")
        @Test
        void success_changeOrderStatus() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = createTableOrder(5, false);

            final Order order = new Order(
                    savedOrderTable,
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    new ArrayList<>()
            );
            final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, savedMenu, 10));
            order.addOrderLineItems(orderLineItems);
            final Order expected = orderRepository.save(order);

            // when
            final Order status = new Order(
                    null,
                    OrderStatus.COMPLETION.name(),
                    null,
                    null
            );

            final Order actual = orderService.changeOrderStatus(expected.getId(), status);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getOrderTable()).isEqualTo(expected.getOrderTable());
                softly.assertThat(actual.getOrderStatus()).isEqualTo(status.getOrderStatus());
                softly.assertThat(actual.getOrderedTime()).isEqualTo(expected.getOrderedTime());
                softly.assertThat(actual.getOrderLineItems()).isEqualTo(expected.getOrderLineItems());
            });
        }

        @DisplayName("[EXCEPTION] 주문이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void throwException_when_order_isNotExists() {
            // given
            final Order status = new Order(
                    null,
                    OrderStatus.COMPLETION.name(),
                    null,
                    null
            );

            // then
            final Long wrongOrderId = -1L;
            assertThatThrownBy(() -> orderService.changeOrderStatus(wrongOrderId, status))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문이 완료 상태에서 완료 상태로 변경할 경우 예외가 발생한다.")
        @Test
        void throwException_when_orderStatus_is_COMPLETION() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = createTableOrder(5, false);

            final Order order = new Order(
                    savedOrderTable,
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    new ArrayList<>()
            );
            final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, savedMenu, 10));
            order.addOrderLineItems(orderLineItems);
            final Order expected = orderRepository.save(order);

            final Order completionStatus = new Order(
                    null,
                    OrderStatus.COMPLETION.name(),
                    null,
                    null
            );

            // when
            orderService.changeOrderStatus(expected.getId(), completionStatus);

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(expected.getId(), completionStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
