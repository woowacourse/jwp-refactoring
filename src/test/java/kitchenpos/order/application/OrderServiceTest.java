package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.DataClearExtension;
import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.order.application.request.OrderCommand;
import kitchenpos.order.application.request.OrderLineItemCommand;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("주문 관련 기능에서")
@SpringBootTest
@ExtendWith(DataClearExtension.class)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private MenuValidator menuValidator;

    @Nested
    @DisplayName("주문을 생성할 때")
    class CreateOrder {

        @Test
        @DisplayName("조리상태로 주문이 생성된다.")
        void create() {
            Menu menu = createMenu("강정치킨", 18000);
            OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));

            OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(menu.getId(), 2);
            OrderResponse response = orderService.create(
                    new OrderCommand(orderTable.getId(), List.of(orderLineItemCommand)));

            assertThat(response.getId()).isNotNull();
            assertThat(response.getOrderStatus()).isEqualTo("COOKING");
            assertThat(response.getOrderLineItems()).isNotNull();
        }

        @Nested
        @DisplayName("예외가 발생하는 경우는")
        class Exception {

            @Test
            @DisplayName("주문항목이 비어있으면 예외가 발생한다.")
            void createOrderLineEmpty() {
                createMenu("강정치킨", 18000);
                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));

                assertThatThrownBy(
                        () -> orderService.create(new OrderCommand(orderTable.getId(), Collections.emptyList())))
                        .hasMessage("주문 항목이 비어있습니다.");
            }

            @Test
            @DisplayName("주문항목의 수와 메뉴의 수가 일치하지 않으면 에외가 발생한다.")
            void createInvalidOrderLine() {
                Menu menu = createMenu("강정치킨", 18000);
                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));
                OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(menu.getId(), 2);

                assertThatThrownBy(() -> orderService.create(
                        new OrderCommand(orderTable.getId(), List.of(orderLineItemCommand, orderLineItemCommand))))
                        .hasMessage("주문항목의 수와 메뉴의 수가 일치하지 않습니다.");
            }

            @Test
            @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
            void createNotFoundOrderTable() {
                Menu menu = createMenu("강정치킨", 18000);
                OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(menu.getId(), 2);

                assertThatThrownBy(
                        () -> orderService.create(new OrderCommand(1L, List.of(orderLineItemCommand))))
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("빈 테이블이면 예외가 발새한다.")
            void createOrderTableIsEmpty() {
                Menu menu = createMenu("강정치킨", 18000);
                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, true));
                OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(menu.getId(), 2);

                assertThatThrownBy(
                        () -> orderService.create(new OrderCommand(orderTable.getId(), List.of(orderLineItemCommand))))
                        .hasMessage("빈 테이블입니다.");
            }
        }
    }

    @Test
    @DisplayName("존재하는 주문을 모두 조회한다.")
    void list() {
        Menu menu = createMenu("강정치킨", 18000);

        OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));
        OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(menu.getId(), 2)));

        orderRepository.save(Order.startCooking(orderTable.getId(), orderLineItems, orderValidator));

        List<OrderResponse> orders = orderService.list();

        assertThat(orders).hasSize(1);
    }

    @Nested
    @DisplayName("주문의 상태를 변경할 때")
    class ChangeStatus {

        @Test
        @DisplayName("식사 상태로 변경한다.")
        void changeOrderStatus() {
            Menu menu = createMenu("강정치킨", 18000);

            OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));

            OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(menu.getId(), 2)));
            Order order = orderRepository.save(Order.startCooking(orderTable.getId(), orderLineItems, orderValidator));

            OrderResponse orderResponse = orderService.changeOrderStatus(order.getId(), "MEAL");

            assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Nested
        @DisplayName("예외가 발생하는 경우는")
        class Exception {

            @Test
            @DisplayName("주문이 존재하지 않으면 예외가 발생한다.")
            void changeOrderStatusNotFoundOrder() {
                orderTableRepository.save(new OrderTable(2, true));

                assertThatThrownBy(() -> orderService.changeOrderStatus(1L, "MEAL"))
                        .hasMessage("주문이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("계산 완료 상태로 변경하면 예외가 발생한다.")
            void changeOrderStatusCompletion() {
                Menu menu = createMenu("강정치킨", 18000);
                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));
                OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(menu.getId(), 2)));
                Order order = orderRepository.save(
                        Order.startCooking(orderTable.getId(), orderLineItems, orderValidator));

                assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), "COMPLETION"))
                        .hasMessage("계산 완료된 주문입니다.");
            }
        }
    }

    private Menu createMenu(String name, int price) {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
        Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));
        List<MenuProduct> menuProducts = List.of(new MenuProduct(product.getId(), 2L));
        return menuRepository.save(
                Menu.create(name, new Price(BigDecimal.valueOf(price)), menuGroup.getId(), menuProducts,
                        menuValidator));
    }
}
