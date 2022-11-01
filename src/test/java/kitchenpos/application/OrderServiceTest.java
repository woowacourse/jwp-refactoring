package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.request.OrderCommand;
import kitchenpos.application.dto.request.OrderLineItemCommand;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.common.DataClearExtension;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderValidator;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
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

    @Nested
    @DisplayName("주문을 생성할 때")
    class CreateOrder {

        @Test
        @DisplayName("조리상태로 주문이 생성된다.")
        void create() {
            Menu menu = createMenu();

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
                createMenu();
                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));

                assertThatThrownBy(
                        () -> orderService.create(new OrderCommand(orderTable.getId(), Collections.emptyList())))
                        .hasMessage("주문 항목이 비어있습니다.");
            }

            @Test
            @DisplayName("주문항목의 수와 메뉴의 수가 일치하지 않으면 에외가 발생한다.")
            void createInvalidOrderLine() {
                Menu menu = createMenu();
                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));
                OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(menu.getId(), 2);

                assertThatThrownBy(() -> orderService.create(
                        new OrderCommand(orderTable.getId(), List.of(orderLineItemCommand, orderLineItemCommand))))
                        .hasMessage("주문항목의 수와 메뉴의 수가 일치하지 않습니다.");
            }

            @Test
            @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
            void createNotFoundOrderTable() {
                Menu menu = createMenu();
                OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(menu.getId(), 2);

                assertThatThrownBy(
                        () -> orderService.create(new OrderCommand(1L, List.of(orderLineItemCommand))))
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("빈 테이블이면 예외가 발새한다.")
            void createOrderTableIsEmpty() {
                Menu menu = createMenu();

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
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
        Menu menu = menuRepository.save(
                new Menu("강정치킨", new Price(BigDecimal.valueOf(37000)), menuGroup.getId(), new ArrayList<>()));

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
            Menu menu = createMenu();

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
                Menu menu = createMenu();
                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));
                OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(menu.getId(), 2)));
                Order order = orderRepository.save(Order.startCooking(orderTable.getId(), orderLineItems, orderValidator));

                assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), "COMPLETION"))
                        .hasMessage("계산 완료된 주문입니다.");
            }
        }
    }

    private Menu createMenu() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
        Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));
        Menu menu = menuRepository.save(new Menu("강정치킨", new Price(BigDecimal.valueOf(37000)), menuGroup.getId(), new ArrayList<>()));
        menu.addMenuProduct(new MenuProduct(menu, product.getId(), 2));
        return menu;
    }
}
