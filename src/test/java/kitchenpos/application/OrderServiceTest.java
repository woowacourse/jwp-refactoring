package kitchenpos.application;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 주문을_생성할_수_있다() {
        Product product1 = productRepository.save(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productRepository.save(new Product("상품2", new BigDecimal(20000)));

        MenuProduct menuProduct1 = new MenuProduct(product1, 1);
        MenuProduct menuProduct2 = new MenuProduct(product2, 2);
        MenuProduct menuProduct3 = new MenuProduct(product2, 2);
        MenuProduct menuProduct4 = new MenuProduct(product2, 2);

        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹1")).getId();

        Menu menu1 = menuRepository.save(new Menu("메뉴1", new BigDecimal(40000), menuGroupId,
                List.of(menuProduct1, menuProduct2)));
        Menu menu2 = menuRepository.save(new Menu("메뉴2", new BigDecimal(40000), menuGroupId,
                List.of(menuProduct3, menuProduct4)));

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));

        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(menu1.getId(), 1);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(menu2.getId(), 2);

        OrderRequest request = new OrderRequest(orderTable.getId(),
                List.of(orderLineItemRequest1, orderLineItemRequest2));

        OrderResponse actual = orderService.create(request);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getOrderStatus()).isEqualTo(COOKING);
            assertThat(actual.getOrderLineItems()).hasSize(2)
                    .extracting("menuName")
                    .containsOnly("메뉴1", "메뉴2");
        });
    }

    @Test
    void 주문_테이블이_빈_테이블인_경우_주문을_생성할_수_없다() {
        Product product1 = productRepository.save(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productRepository.save(new Product("상품2", new BigDecimal(20000)));

        MenuProduct menuProduct1 = new MenuProduct(product1, 1);
        MenuProduct menuProduct2 = new MenuProduct(product2, 2);
        MenuProduct menuProduct3 = new MenuProduct(product2, 2);
        MenuProduct menuProduct4 = new MenuProduct(product2, 2);

        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹1")).getId();

        Menu menu1 = menuRepository.save(new Menu("메뉴1", new BigDecimal(40000), menuGroupId,
                List.of(menuProduct1, menuProduct2)));
        Menu menu2 = menuRepository.save(new Menu("메뉴2", new BigDecimal(40000), menuGroupId,
                List.of(menuProduct3, menuProduct4)));

        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(menu1.getId(), 1);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(menu2.getId(), 2);

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, true));

        OrderRequest request = new OrderRequest(orderTable.getId(),
                List.of(orderLineItemRequest1, orderLineItemRequest2));

        assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목이_비어있는_경우_주문을_생성할_수_없다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));

        OrderRequest request = new OrderRequest(orderTable.getId(), new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_주문_목록을_조회할_수_있다() {
        Long orderTableId = orderTableRepository.save(new OrderTable(null, 5, false)).getId();

        OrderLineItem orderLineItem1 = new OrderLineItem("메뉴", new BigDecimal(10000), 1);
        OrderLineItem orderLineItem2 = new OrderLineItem("메뉴", new BigDecimal(10000), 1);

        Order order1 = new Order(orderTableId, COOKING, List.of(orderLineItem1));
        Order order2 = new Order(orderTableId, COOKING, List.of(orderLineItem2));

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<OrderResponse> actual = orderService.list();

        assertThat(actual).hasSize(2);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        Long orderTableId = orderTableRepository.save(new OrderTable(null, 5, false)).getId();

        OrderLineItem orderLineItem = new OrderLineItem("메뉴", new BigDecimal(10000), 1);

        Order order = orderRepository.save(new Order(orderTableId, COOKING, List.of(orderLineItem)));
        OrderStatusRequest request = new OrderStatusRequest(MEAL);

        OrderResponse actual = orderService.changeOrderStatus(order.getId(), request);

        assertThat(actual.getOrderStatus()).isEqualTo(MEAL);
    }

    @Test
    void 기존_주문_상태가_계산_완료_상태인_경우_상태를_변경할_수_없다() {
        Long orderTableId = orderTableRepository.save(new OrderTable(null, 5, false)).getId();

        OrderLineItem orderLineItem = new OrderLineItem("메뉴", new BigDecimal(10000), 1);

        Order order = orderRepository.save(new Order(orderTableId, COMPLETION, List.of(orderLineItem)));
        OrderStatusRequest request = new OrderStatusRequest(MEAL);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
