package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.request.OrderCommand;
import kitchenpos.application.dto.request.OrderLineItemCommand;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.common.DataClearExtension;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
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

    @Nested
    @DisplayName("주문을 생성할 때")
    class CreateOrder {

        @Test
        @DisplayName("조리상태로 주문이 생성된다.")
        void create() {
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
            Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));
            Menu menu = menuRepository.save(new Menu("강정치킨", BigDecimal.valueOf(37000), menuGroup.getId()));
            menu.addMenuProduct(new MenuProduct(menu.getId(), product.getId(), 2));

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
                MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
                Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));
                Menu menu = menuRepository.save(new Menu("강정치킨", BigDecimal.valueOf(37000), menuGroup.getId()));
                menu.addMenuProduct(new MenuProduct(menu.getId(), product.getId(), 2));
                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, true));

                assertThatThrownBy(
                        () -> orderService.create(new OrderCommand(orderTable.getId(), Collections.emptyList())))
                        .hasMessage("주문 항목이 비어있습니다.");
            }

            @Test
            @DisplayName("주문항목의 수와 메뉴의 수가 일치하지 않으면 에외가 발생한다.")
            void createInvalidOrderLine() {
                MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
                Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));
                Menu menu = menuRepository.save(new Menu("강정치킨", BigDecimal.valueOf(37000), menuGroup.getId()));
                menu.addMenuProduct(new MenuProduct(menu.getId(), product.getId(), 2));
                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));
                OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(menu.getId(), 2);

                assertThatThrownBy(() -> orderService.create(
                        new OrderCommand(orderTable.getId(), List.of(orderLineItemCommand, orderLineItemCommand))))
                        .hasMessage("주문항목의 수와 메뉴의 수가 일치하지 않습니다.");
            }

            @Test
            @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
            void createNotFoundOrderTable() {
                MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
                Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));
                Menu menu = menuRepository.save(new Menu("강정치킨", BigDecimal.valueOf(37000), menuGroup.getId()));
                menu.addMenuProduct(new MenuProduct(menu.getId(), product.getId(), 2));
                OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(menu.getId(), 2);

                assertThatThrownBy(
                        () -> orderService.create(new OrderCommand(1L, List.of(orderLineItemCommand))))
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("빈 테이블이면 예외가 발새한다.")
            void createOrderTableIsEmpty() {
                MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
                Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));
                Menu menu = menuRepository.save(new Menu("강정치킨", BigDecimal.valueOf(37000), menuGroup.getId()));
                menu.addMenuProduct(new MenuProduct(menu.getId(), product.getId(), 2));

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
        Menu menu = menuRepository.save(new Menu("강정치킨", BigDecimal.valueOf(37000), menuGroup.getId()));
        OrderTable orderTable = orderTableRepository.save(new OrderTable(2, true));

        orderRepository.save(new Order(orderTable.getId(), List.of(new OrderLineItem(menu.getId(), 2))));
        orderRepository.save(new Order(orderTable.getId(), List.of(new OrderLineItem(menu.getId(), 2))));
        orderRepository.save(new Order(orderTable.getId(), List.of(new OrderLineItem(menu.getId(), 2))));

        List<OrderResponse> orders = orderService.list();

        assertThat(orders).hasSize(3);
    }

    @Nested
    @DisplayName("주문의 상태를 변경할 때")
    class ChangeStatus {

        @Test
        @DisplayName("식사 상태로 변경한다.")
        void changeOrderStatus() {
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
            Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));
            Menu menu = menuRepository.save(new Menu("강정치킨", BigDecimal.valueOf(37000), menuGroup.getId()));
            menu.addMenuProduct(new MenuProduct(menu.getId(), product.getId(), 2));

            OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));

            OrderResponse savedOrder = orderService.create(
                    new OrderCommand(orderTable.getId(), List.of(new OrderLineItemCommand(menu.getId(), 2))));

            OrderResponse orderResponse = orderService.changeOrderStatus(savedOrder.getId(), "MEAL");

            assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
            assertThat(orderResponse.getOrderLineItems()).isNotEmpty();
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
                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, true));
                Order savedOrder = orderRepository.save(new Order(orderTable.getId(), OrderStatus.COMPLETION));

                assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), "COMPLETION"))
                        .hasMessage("계산 완료된 주문입니다.");
            }
        }
    }
}
