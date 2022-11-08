package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.application.MenuProductDto;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.application.OrderLineItemDto;
import kitchenpos.order.dto.request.ChangeOrderStatusRequest;
import kitchenpos.order.dto.request.CreateOrderLineItemRequest;
import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuValidator menuValidator;

    @Nested
    @DisplayName("create()")
    class CreateMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 주문을 생성한다.")
        void create() {
            // given
            Menu savedMenu = createAndSaveMenu();
            OrderTable savedOrderTable = createAndSaveOrderTable();

            CreateOrderRequest request = createOrderCreateRequest(
                savedOrderTable.getId(),
                savedMenu.getId(),
                10
            );

            // when
            OrderResponse savedOrder = orderService.create(request);

            // then
            assertThat(savedOrder.getId()).isNotNull();
        }

        @Test
        @DisplayName("존재하지 않는 메뉴 id인 경우 예외가 발생한다.")
        void invalidOrderLineItemId() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable();

            CreateOrderRequest request = createOrderCreateRequest(
                savedOrderTable.getId(),
                0L,
                10
            );

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 메뉴입니다.");
        }

    }

    @Nested
    @DisplayName("list()")
    class ListMethod {

        @Test
        @DisplayName("전체 주문을 조회한다.")
        void list() {
            List<OrderResponse> orders = orderService.list();
            assertThat(orders).isNotNull();
        }

    }

    @Nested
    @DisplayName("changeOrderStatus()")
    class ChangeOrderStatusMethod {

        @Test
        @DisplayName("특정 주문의 주문 상태를 변경한다.")
        void changeOrderStatus() {
            // given
            Order savedOrder = createAndSaveOrder();
            ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("MEAL");

            // when
            OrderResponse changedOrder = orderService.changeOrderStatus(savedOrder.getId(), request);

            // then
            assertThat(changedOrder.getOrderStatus()).isEqualTo("MEAL");
        }

        @Test
        @DisplayName("존재하지 않는 order id인 경우 예외가 발생한다.")
        void wrongInvalidOrderId() {
            // given
            ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("MEAL");

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(0L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 주문입니다.");
        }

    }

    private Order createAndSaveOrder() {
        Menu menu = createAndSaveMenu();
        OrderTable orderTable = createAndSaveOrderTable();

        Order order = new Order(
            orderTable.getId(),
            new ArrayList<OrderLineItemDto>() {{
                add(new OrderLineItemDto(menu.getName(), menu.getPrice(), 1L));
            }},
            orderValidator
        );

        return orderRepository.save(order);
    }

    private Menu createAndSaveMenu() {
        Product product = productRepository.save(new Product("product", new BigDecimal(5000)));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));

        return menuRepository.save(new Menu(
            "menu",
            new BigDecimal(2000),
            menuGroup.getId(),
            new ArrayList<MenuProductDto>() {{
                add(new MenuProductDto(product.getId(), 1L));
            }},
            menuValidator
        ));
    }

    private OrderTable createAndSaveOrderTable() {
        OrderTable orderTable = new OrderTable(10, false);
        return orderTableRepository.save(orderTable);
    }

    private CreateOrderRequest createOrderCreateRequest(long orderTableId, long menuId, long quantity) {
        CreateOrderLineItemRequest orderLineItem = new CreateOrderLineItemRequest(menuId, quantity);
        return new CreateOrderRequest(
            orderTableId,
            new ArrayList<CreateOrderLineItemRequest>() {{
                add(orderLineItem);
            }}
        );
    }

}
