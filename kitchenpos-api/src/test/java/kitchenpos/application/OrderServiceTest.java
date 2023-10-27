package kitchenpos.application;

import kitchenpos.fixture.MenuFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.request.OrderCreateRequest;
import kitchenpos.order.application.request.OrderLineItemCreateRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderedMenu;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 주문을_생성한다() {
        // given
        Product 치킨 = productRepository.save(Product.of("후라이드", new BigDecimal("16000.00")));

        Menu menu = menuRepository.save(MenuFixture.MENU_1(치킨));

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 3, false));

        OrderedMenu orderedMenu = OrderedMenu.from(menu);
        OrderLineItem orderLineItem = new OrderLineItem(null, orderedMenu, 3);
        Order order = new Order(orderTable, List.of(orderLineItem));

        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(menu.getId(), 3);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(orderLineItemCreateRequest));

        // when
        OrderResponse savedOrder = orderService.create(orderCreateRequest);

        // then
        assertThat(savedOrder).usingRecursiveComparison().ignoringFields("id", "orderedTime", "orderLineItems.order", "orderLineItems.seq")
                .isEqualTo(order);
    }

    @Test
    void empty_상태의_테이블에_대한_주문_생성은_예외_발생() {
        // given
        Product 치킨 = productRepository.save(Product.of("후라이드", new BigDecimal("16000.00")));

        Menu menu = menuRepository.save(MenuFixture.MENU_1(치킨));
        OrderedMenu orderedMenu = OrderedMenu.from(menu);

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 3, true));
        OrderLineItem orderLineItem = new OrderLineItem(null, orderedMenu, 3);
        Order order = new Order(orderTable, List.of(orderLineItem));

        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(menu.getId(), 3);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(orderLineItemCreateRequest));


        // when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 같은_메뉴에_대한_주문_항목이_중복되어_존재한다면_주문_생성시_예외_발생() {
        // given
        Product 치킨 = productRepository.save(Product.of("후라이드", new BigDecimal("16000.00")));

        Menu menu = menuRepository.save(MenuFixture.MENU_1(치킨));
        OrderedMenu orderedMenu = OrderedMenu.from(menu);

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 3, true));
        OrderLineItem orderLineItem1 = new OrderLineItem(null, orderedMenu, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, orderedMenu, 4);
        Order order = new Order(orderTable, List.of(orderLineItem1, orderLineItem2));

        OrderLineItemCreateRequest orderLineItemCreateRequest1 = new OrderLineItemCreateRequest(menu.getId(), 3);
        OrderLineItemCreateRequest orderLineItemCreateRequest2 = new OrderLineItemCreateRequest(menu.getId(), 4);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(orderLineItemCreateRequest1, orderLineItemCreateRequest2));


        // when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_주문을_조회한다() {
        // given
        Product 치킨 = productRepository.save(Product.of("후라이드", new BigDecimal("16000.00")));

        Menu menu = menuRepository.save(MenuFixture.MENU_1(치킨));
        OrderedMenu orderedMenu = OrderedMenu.from(menu);

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 3, false));
        OrderLineItem orderLineItem = new OrderLineItem(null, orderedMenu, 3);
        Order order = new Order(orderTable, List.of(orderLineItem));

        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(menu.getId(), 3);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(orderLineItemCreateRequest));

        OrderResponse savedOrder = orderService.create(orderCreateRequest);

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).usingRecursiveComparison()
                .isEqualTo(List.of(savedOrder));
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        Product 치킨 = productRepository.save(Product.of("후라이드", new BigDecimal("16000.00")));

        Menu menu = menuRepository.save(MenuFixture.MENU_1(치킨));
        OrderedMenu orderedMenu = OrderedMenu.from(menu);

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 3, false));
        OrderLineItem orderLineItem = new OrderLineItem(null, orderedMenu, 3);
        Order order = new Order(orderTable, List.of(orderLineItem));

        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(menu.getId(), 3);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(orderLineItemCreateRequest));

        OrderResponse savedOrder = orderService.create(orderCreateRequest);

        // when
        OrderResponse changedOrder = orderService.changeOrderStatus(savedOrder.getId(),
                new Order(orderTable, OrderStatus.MEAL));

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void 주문_상태가_완료인_주문은_주문_상태를_변경할_수_없다() {
        // given
        Product 치킨 = productRepository.save(Product.of("후라이드", new BigDecimal("16000.00")));

        Menu menu = menuRepository.save(MenuFixture.MENU_1(치킨));
        OrderedMenu orderedMenu = OrderedMenu.from(menu);

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 3, false));
        OrderLineItem orderLineItem = new OrderLineItem(null, orderedMenu, 3);
        Order order = new Order(orderTable, List.of(orderLineItem));

        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(menu.getId(), 3);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(orderLineItemCreateRequest));

        OrderResponse orderResponse = orderService.create(orderCreateRequest);
        Order savedOrder = new Order(orderResponse.getId(), orderResponse.getOrderTable(), orderResponse.getOrderStatus(),
                orderResponse.getOrderedTime(), orderResponse.getOrderLineItems());
        orderService.changeOrderStatus(savedOrder.getId(), new Order(null, null, OrderStatus.COMPLETION, null, null));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(),
                new Order(null, null, OrderStatus.MEAL, null, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목이_생성되고_메뉴_정보가_변경되더라도_주문_항목_정보는_바뀌지_않는다() {
        // given
        Product 치킨 = productRepository.save(Product.of("후라이드", new BigDecimal("16000.00")));

        Menu menu = menuRepository.save(MenuFixture.MENU_1(치킨));
        OrderedMenu orderedMenu = OrderedMenu.from(menu);

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 3, false));
        OrderLineItem orderLineItem = new OrderLineItem(null, orderedMenu, 3);
        Order order = new Order(orderTable, List.of(orderLineItem));

        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(menu.getId(), 3);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(orderLineItemCreateRequest));

        OrderResponse orderResponse = orderService.create(orderCreateRequest);

        // when
        menu.updateName("맛있는후라이드치킨");
        menu.updatePrice(new BigDecimal("17000.00"));
        List<OrderResponse> orderResponses = orderService.list();

        // then
        assertAll(
                () -> assertThat(orderResponses.get(0).getOrderLineItems().get(0).getOrderedMenu().getMenuName()).isEqualTo(orderedMenu.getMenuName()),
                () -> assertThat(orderResponses.get(0).getOrderLineItems().get(0).getOrderedMenu().getMenuPrice()).isEqualTo(orderedMenu.getMenuPrice())
        );

    }
}
