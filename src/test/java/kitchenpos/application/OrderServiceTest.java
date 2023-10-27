package kitchenpos.application;

import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemsCreateRequest;
import kitchenpos.order.dto.request.OrderStatusUpdateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import(OrderService.class)
class OrderServiceTest extends ServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private Menu 후1양1_메뉴;
    private Menu 간1양1_메뉴;
    private OrderTable 주문테이블;
    private OrderLineItemsCreateRequest 후1양1_수량1;
    private OrderLineItemsCreateRequest 간1양1_수량1;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = MenuGroup.create("두마리메뉴");
        MenuGroup 두마리메뉴 = menuGroupRepository.save(menuGroup);

        Product product1 = Product.create("후라이드", BigDecimal.valueOf(16000));
        Product 후라이드 = productRepository.save(product1);

        Product product2 = Product.create("양념치킨", BigDecimal.valueOf(16000));
        Product 양념치킨 = productRepository.save(product2);

        Product product3 = Product.create("간장치킨", BigDecimal.valueOf(16000));
        Product 간장치킨 = productRepository.save(product3);

        Menu menu1 = Menu.create("두마리메뉴 - 후1양1", BigDecimal.valueOf(32000L), 두마리메뉴);
        Menu menu2 = Menu.create("두마리메뉴 - 간1양1", BigDecimal.valueOf(32000L), 두마리메뉴);

        후1양1_메뉴 = menuRepository.save(menu1);
        간1양1_메뉴 = menuRepository.save(menu2);

        MenuProduct 후1양1_후라이드_한마리 = MenuProduct.create(후1양1_메뉴, 후라이드, 1);
        MenuProduct 후1양1_양념치킨_한마리 = MenuProduct.create(후1양1_메뉴, 양념치킨, 1);

        MenuProduct 간1양1_간장치킨_한마리 = MenuProduct.create(간1양1_메뉴, 간장치킨, 1);
        MenuProduct 간1양1_양념치킨_한마리 = MenuProduct.create(간1양1_메뉴, 양념치킨, 1);

        menu1.updateMenuProducts(List.of(후1양1_후라이드_한마리, 후1양1_양념치킨_한마리));
        menu2.updateMenuProducts(List.of(간1양1_간장치킨_한마리, 간1양1_양념치킨_한마리));

        OrderTable orderTable = OrderTable.create(0, false);
        주문테이블 = orderTableRepository.save(orderTable);

        후1양1_수량1 = new OrderLineItemsCreateRequest(후1양1_메뉴.getId(), 1L);
        간1양1_수량1 = new OrderLineItemsCreateRequest(간1양1_메뉴.getId(), 1L);
    }

    @DisplayName("주문을 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        OrderCreateRequest request = new OrderCreateRequest(주문테이블.getId(), List.of(후1양1_수량1, 간1양1_수량1));

        // when
        Order actual = orderService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderRepository.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getOrderLineItems()).hasSize(2);
            softly.assertThat(actual.getOrderLineItems()).extracting("menuId")
                    .contains(후1양1_메뉴.getId(), 간1양1_메뉴.getId());
            softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        });
    }

    @DisplayName("주문 등록 시, 주문 항목이 없을 경우 예외가 발생한다.")
    @Test
    void create_FailWithEmptyOrderLineItems() {
        // given
        OrderCreateRequest request = new OrderCreateRequest(주문테이블.getId(), Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시, 주문에 속한 수량이 있는 메뉴가 중복될 경우 예외가 발생한다.")
    @Test
    void create_FailWithDuplicatedOrderLineItems() {
        // given
        OrderCreateRequest request = new OrderCreateRequest(주문테이블.getId(), List.of(후1양1_수량1, 후1양1_수량1));

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시, 주문 테이블이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void create_FailWithInvalidOrderTableId() {
        // given
        Long invalidOrderTableId = 1000L;
        OrderCreateRequest request = new OrderCreateRequest(invalidOrderTableId, List.of(후1양1_수량1, 간1양1_수량1));

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시, 주문 테이블이 비어있을 경우 예외가 발생한다.")
    @Test
    void create_FailWithEmptyOrderTable() {
        // given
        주문테이블.changeEmpty(true);
        OrderTable 비어있는_주문테이블 = orderTableRepository.save(주문테이블);

        OrderCreateRequest request = new OrderCreateRequest(비어있는_주문테이블.getId(), List.of(후1양1_수량1, 간1양1_수량1));

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        OrderCreateRequest request1 = new OrderCreateRequest(주문테이블.getId(), List.of(후1양1_수량1, 간1양1_수량1));
        Order 두마리치킨_2개_주문 = orderService.create(request1);

        OrderCreateRequest request2 = new OrderCreateRequest(주문테이블.getId(), List.of(후1양1_수량1));
        Order 두마리치킨_1개_주문 = orderService.create(request2);

        orderRepository.save(두마리치킨_2개_주문);
        orderRepository.save(두마리치킨_1개_주문);

        // when
        List<Order> list = orderService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(list).hasSize(2);
            softly.assertThat(list).usingRecursiveComparison()
                    .ignoringFields("orderLineItems")
                    .isEqualTo(List.of(두마리치킨_2개_주문, 두마리치킨_1개_주문));
        });
    }

    @DisplayName("주문 상태를 정상적으로 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(주문테이블.getId(), List.of(후1양1_수량1, 간1양1_수량1));
        Order 주문 = orderService.create(orderCreateRequest);

        OrderStatus 변경할_주문_상태 = OrderStatus.MEAL;
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(변경할_주문_상태.name());

        // when
        Order 주문상태가_변경된_주문 = orderService.changeOrderStatus(주문.getId(), request);

        // then
        assertThat(주문상태가_변경된_주문.getOrderStatus()).isEqualTo(변경할_주문_상태);
    }

    @DisplayName("주문 상태 변경 시, 존재하지 않는 주문일 경우 예외가 발생한다.")
    @Test
    void changeOrderStatus_FailWithInvalidOrderId() {
        // given
        OrderStatus 변경할_주문_상태 = OrderStatus.MEAL;
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(변경할_주문_상태.name());

        Long invalidOrderId = 100L;

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경 시, 주문 상태가 COMPLETION인 경우 예외가 발생한다.")
    @Test
    void changeOrderStatus_FailWithInvalidOrderStatusRequest() {
        // given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(주문테이블.getId(), List.of(후1양1_수량1, 간1양1_수량1));
        Order order = orderService.create(orderCreateRequest);

        OrderStatus 유효하지_않은_주문_상태 = OrderStatus.COMPLETION;
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(유효하지_않은_주문_상태.name());

        order.changeOrderStatus(유효하지_않은_주문_상태);
        Order 주문 = orderRepository.save(order);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
