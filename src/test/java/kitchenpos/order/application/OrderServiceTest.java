package kitchenpos.order.application;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.domain.vo.Name;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.menu.domain.vo.Quantity;
import kitchenpos.dto.OrderCreateOrderLineItemRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderServiceTest extends ApplicationTestConfig {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderTableRepository);
    }

    @DisplayName("새로운 주문 등록")
    @Nested
    class CreateNestedTest {

        @DisplayName("[SUCCESS] 새로운 주문을 등록한다.")
        @Test
        void success_create() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = createOrderTable(5, false);
            final OrderCreateRequest expected = new OrderCreateRequest(savedOrderTable.getId(), List.of(
                    new OrderCreateOrderLineItemRequest(savedMenu.getId(), 10)
            ));

            // when
            final OrderResponse actual = orderService.create(expected);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getOrderTable().getId()).isEqualTo(expected.getOrderTableId());
                softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
                softly.assertThat(actual.getOrderedTime()).isBefore(LocalDateTime.now());
                softly.assertThat(actual.getOrderLineItems()).hasSize(expected.getOrderLineItems().size());
            });
        }

        @DisplayName("[EXCEPTION] 주문 항목 목록이 비어있는 경우 예외가 발생한다.")
        @Test
        void throwException_create_order_when_orderLineItemsIsEmpty() {
            // given
            final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(5, false));

            // when
            final OrderCreateRequest request = new OrderCreateRequest(savedOrderTable.getId(), List.of(
                    new OrderCreateOrderLineItemRequest(0, 10)
            ));

            // then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 항목 목록 수량이 실제 메뉴의 수량과 다를 경우 예외가 발생한다.")
        @Test
        void throwException_create_order_when_orderLineItemsSize_IsNotEqualTo_menuCountsSize() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(5, false));

            // when
            final OrderCreateRequest request = new OrderCreateRequest(savedOrderTable.getId(), List.of(
                    new OrderCreateOrderLineItemRequest(savedMenu.getId(), 10),
                    new OrderCreateOrderLineItemRequest(-1L, 10)
            ));

            // then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 테이블이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void throwException_create_order_when_orderTableIsNotExists() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = createOrderTable(10, true);

            // when
            final OrderCreateRequest request = new OrderCreateRequest(0, List.of(
                    new OrderCreateOrderLineItemRequest(savedMenu.getId(), 10)
            ));

            // then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(EmptyResultDataAccessException.class);
        }
    }

    private Menu createMenu() {
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
        return menuRepository.save(
                Menu.withEmptyMenuProducts(
                        new Name("테스트용 메뉴명"),
                        Price.from("0"),
                        savedMenuGroup
                )
        );
    }

    private OrderTable createOrderTable(final int numberOfGuests, final boolean empty) {
        return orderTableRepository.save(OrderTable.withoutTableGroup(numberOfGuests, empty));
    }

    @DisplayName("주문 상태 변경")
    @Nested
    class ChangeOrderStatus {

        @DisplayName("[SUCCESS] 주문 상태를 변경한다.")
        @Test
        void success_changeOrderStatus() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = createOrderTable(5, false);

            final Order order = Order.ofEmptyOrderLineItems(savedOrderTable);
            final List<OrderLineItem> orderLineItems = new ArrayList<>(List.of(OrderLineItem.withoutOrder(savedMenu, new Quantity(10))));
            order.addOrderLineItems(orderLineItems);
            final Order savedOrder = orderRepository.save(order);
            final OrderResponse expected = OrderResponse.from(savedOrder);

            // when
            final OrderResponse actual = orderService.changeOrderStatus(expected.getId(), OrderStatus.MEAL.name());

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getOrderTable())
                        .usingRecursiveComparison()
                        .isEqualTo(expected.getOrderTable());
                softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
                softly.assertThat(actual.getOrderedTime()).isEqualTo(expected.getOrderedTime());
                softly.assertThat(actual.getOrderLineItems())
                        .usingRecursiveComparison()
                        .isEqualTo(expected.getOrderLineItems());
            });
        }

        @DisplayName("[EXCEPTION] 주문이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void throwException_when_order_isNotExists() {
            final Long wrongOrderId = -1L;
            assertThatThrownBy(() -> orderService.changeOrderStatus(wrongOrderId, OrderStatus.COOKING.name()))
                    .isInstanceOf(EmptyResultDataAccessException.class);
        }

        @DisplayName("[EXCEPTION] 주문이 완료 상태에서 완료 상태로 변경할 경우 예외가 발생한다.")
        @Test
        void throwException_when_orderStatus_is_COMPLETION() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = createOrderTable(5, false);

            final Order order = Order.ofEmptyOrderLineItems(savedOrderTable);
            final List<OrderLineItem> orderLineItems = new ArrayList<>(List.of(OrderLineItem.withoutOrder(savedMenu, new Quantity(10))));
            order.addOrderLineItems(orderLineItems);
            final Order savedOrder = orderRepository.save(order);

            // when
            orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.COMPLETION.name());

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.COMPLETION.name()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
