package kitchenpos.application;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU2_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.request.OrderCommand;
import kitchenpos.application.dto.request.OrderLineItemCommand;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.cleaner.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Nested
    @DisplayName("Order를 생성할 때 ")
    class CreateTest {

        @Test
        @DisplayName("orderLineItem이 비어있으면 예외가 발생한다.")
        void orderLineItemEmptyFailed() {
            OrderTable orderTable = createOrderTable(false);

            OrderCommand orderCommand = new OrderCommand(orderTable.getId(), Collections.emptyList());
            assertThatThrownBy(() -> orderService.create(orderCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("세부 주문 내역이 비어있습니다.");
        }

        @Test
        @DisplayName("메뉴가 존재하지 않는 경우 예외가 발생한다.")
        void menuNotExistFailed() {
            Menu menu1 = createMenu(MENU1_NAME, MENU1_PRICE);
            OrderTable orderTable = createOrderTable(false);

            List<OrderLineItemCommand> orderLineItemCommands = List.of(new OrderLineItemCommand(menu1.getId(), 1),
                    new OrderLineItemCommand(0L, 1));
            OrderCommand orderCommand = new OrderCommand(orderTable.getId(), orderLineItemCommands);

            assertThatThrownBy(() -> orderService.create(orderCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴입니다.");
        }

        @Test
        @DisplayName("주문 테이블이 없는 경우 예외가 발생한다.")
        void orderTableNotFoundFailed() {
            Menu menu1 = createMenu(MENU1_NAME, MENU1_PRICE);
            Menu menu2 = createMenu(MENU2_NAME, MENU2_PRICE);

            List<OrderLineItemCommand> orderLineItemCommands = List.of(new OrderLineItemCommand(menu1.getId(), 1),
                    new OrderLineItemCommand(menu2.getId(), 1));
            OrderCommand orderCommand = new OrderCommand(0L, orderLineItemCommands);

            assertThatThrownBy(() -> orderService.create(orderCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("주문 테이블이 비어있을 경우 예외가 발생한다.")
        void orderTableEmptyFailed() {
            Menu menu1 = createMenu(MENU1_NAME, MENU1_PRICE);
            Menu menu2 = createMenu(MENU2_NAME, MENU2_PRICE);
            OrderTable orderTable = createOrderTable(true);

            List<OrderLineItemCommand> orderLineItemCommands = List.of(new OrderLineItemCommand(menu1.getId(), 1),
                    new OrderLineItemCommand(menu2.getId(), 1));
            OrderCommand orderCommand = new OrderCommand(orderTable.getId(), orderLineItemCommands);

            assertThatThrownBy(() -> orderService.create(orderCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 비어있습니다.");
        }

        @Test
        @DisplayName("정상적인 경우 성공한다.")
        void create() {
            Menu menu1 = createMenu(MENU1_NAME, MENU1_PRICE);
            Menu menu2 = createMenu(MENU2_NAME, MENU2_PRICE);
            OrderTable orderTable = createOrderTable(false);

            List<OrderLineItemCommand> orderLineItemCommands = List.of(new OrderLineItemCommand(menu1.getId(), 1),
                    new OrderLineItemCommand(menu2.getId(), 1));
            OrderCommand orderCommand = new OrderCommand(orderTable.getId(), orderLineItemCommands);

            OrderResponse orderResponse = orderService.create(orderCommand);
            assertAll(
                    () -> assertThat(orderResponse.id()).isNotNull(),
                    () -> assertThat(orderResponse.orderLineItems()).hasSize(2)
            );
        }
    }

    @Test
    @DisplayName("모든 Order를 조회한다.")
    void list() {
        Menu menu1 = createMenu(MENU1_NAME, MENU1_PRICE);
        Menu menu2 = createMenu(MENU2_NAME, MENU2_PRICE);
        OrderTable orderTable = createOrderTable(false);

        List<OrderLineItemCommand> orderLineItemCommands = List.of(new OrderLineItemCommand(menu1.getId(), 1),
                new OrderLineItemCommand(menu2.getId(), 1));
        OrderCommand orderCommand = new OrderCommand(orderTable.getId(), orderLineItemCommands);
        orderService.create(orderCommand);
        List<Order> orders = orderService.list();

        assertThat(orders).hasSize(1);
    }

    @Nested
    @DisplayName("Order의 상태를 변경할 때 ")
    class ChangeStatusTest {

        @Test
        @DisplayName("주문이 없을 경우 예외가 발생한다.")
        void orderNotFoundFailed() {
            assertThatThrownBy(() -> orderService.changeOrderStatus(0L,
                    new Order(null, OrderStatus.COMPLETION, LocalDateTime.now())))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("이미 종료된 경우 예외가 발생한다.")
        void completionFailed() {
            Menu menu = createMenu(MENU1_NAME, MENU1_PRICE);
            OrderTable orderTable = createOrderTable(false);

            Order order = orderRepository.save(
                    new Order(orderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now()));
            orderLineItemRepository.save(new OrderLineItem(order.getId(), menu.getId(), 1));
            orderLineItemRepository.save(new OrderLineItem(order.getId(), menu.getId(), 2));

            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(),
                    new Order(null, OrderStatus.COMPLETION, LocalDateTime.now())))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 식사가 완료되었습니다.");
        }

        @Test
        @DisplayName("정상적인 경우 성공한다.")
        void changeOrderStatus() {
            Menu menu = createMenu(MENU1_NAME, MENU1_PRICE);
            OrderTable orderTable = createOrderTable(false);

            Order order = orderRepository.save(
                    new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now()));
            orderLineItemRepository.save(new OrderLineItem(order.getId(), menu.getId(), 1));
            orderLineItemRepository.save(new OrderLineItem(order.getId(), menu.getId(), 2));

            Order changedOrder = orderService.changeOrderStatus(order.getId(),
                    new Order(null, OrderStatus.MEAL, null));

            assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        }
    }

    private Menu createMenu(final String name, final BigDecimal price) {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
        return menuRepository.save(new Menu(name, price, menuGroup.getId()));
    }

    private OrderTable createOrderTable(final boolean empty) {
        return orderTableRepository.save(new OrderTable(10, empty));
    }
}
