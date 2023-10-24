package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.MenuProducts;
import kitchenpos.ui.dto.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.CreateOrderLineItemRequest;
import kitchenpos.ui.dto.CreateOrderRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private Menu 후라이드_2개_메뉴;

    @BeforeEach
    void setUpMenu() {
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = new MenuProduct(후라이드, 2l);
        후라이드_2개_메뉴 = menuRepository.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴));
        후라이드_2개_메뉴.addMenuProducts(new MenuProducts(List.of(후라이드_2개)));
    }

    @Test
    @DisplayName("주문을 등록한다")
    void create() {
        // given
        final OrderTable 주문_테이블 = orderTableRepository.save(new OrderTable(3, false));
        final CreateOrderLineItemRequest 주문항목 = new CreateOrderLineItemRequest(후라이드_2개_메뉴.getId(), 1l);

        final CreateOrderRequest order = new CreateOrderRequest(주문_테이블.getId(), List.of(주문항목));

        // when
        final Order actual = orderService.create(order);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            softAssertions.assertThat(actual.getOrderLineItems().getOrderLineItems()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("주문을 등록할 때 주문항목이 없으면 예외가 발생한다")
    void create_emptyOrderLineItems() {
        // given
        final OrderTable 주문_테이블 = orderTableRepository.save(new OrderTable(3, false));
        final List<CreateOrderLineItemRequest> 빈_주문_항목 = Collections.emptyList();

        final CreateOrderRequest invalidOrder = new CreateOrderRequest(주문_테이블.getId(), 빈_주문_항목);

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 비어있습니다.");
    }

    @Test
    @DisplayName("주문을 등록할 때 주문항목의 개수와 메뉴 개수가 다르면 예외가 발생한다.")
    void create_invalidNumberOfOrderLineItems() {
        // given
        final OrderTable 주문_테이블 = orderTableRepository.save(new OrderTable(3, false));

        final CreateOrderLineItemRequest 후라이드_2개_메뉴_1개 = new CreateOrderLineItemRequest(후라이드_2개_메뉴.getId(), 1l);
        final CreateOrderLineItemRequest 후라이드_2개_메뉴_2개 = new CreateOrderLineItemRequest(후라이드_2개_메뉴.getId(), 2l);

        final CreateOrderRequest invalidOrder = new CreateOrderRequest(주문_테이블.getId(), List.of(후라이드_2개_메뉴_1개, 후라이드_2개_메뉴_2개));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목의 메뉴는 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("주문을 등록할 때 주문테이블을 찾을 수 없으면 예외가 발생한다")
    void create_invalidOrderTable() {
        // given
        final long invalidOrderTableId = -999L;
        final CreateOrderLineItemRequest 주문항목 = new CreateOrderLineItemRequest(후라이드_2개_메뉴.getId(), 1l);
        final CreateOrderRequest invalidOrder = new CreateOrderRequest(invalidOrderTableId, List.of(주문항목));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 테이블이 비어있으면 예외가 발생한다")
    void create_emptyTable() {
        // given
        final OrderTable 비어있는_테이블 = orderTableRepository.save(new OrderTable(3, true));

        final CreateOrderLineItemRequest 주문항목 = new CreateOrderLineItemRequest(후라이드_2개_메뉴.getId(), 1l);

        final CreateOrderRequest invalidOrder = new CreateOrderRequest(비어있는_테이블.getId(), List.of(주문항목));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 주문을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 목록을 조회한다")
    void list() {
        // given
        final OrderTable 세명_테이블 = orderTableRepository.save(new OrderTable(3, false));
        final OrderTable 네명_테이블 = orderTableRepository.save(new OrderTable(4, false));

        final OrderLineItem 후라이드_2개_메뉴_1개_주문항목 = new OrderLineItem(후라이드_2개_메뉴, 1l);

        final Order 세명_테이블_주문 = orderRepository.save(new Order(세명_테이블, OrderStatus.COOKING));
        세명_테이블_주문.addOrderLineItems(new OrderLineItems(List.of(후라이드_2개_메뉴_1개_주문항목)));
        final Order 네명_테이블_주문 = orderRepository.save(new Order(네명_테이블, OrderStatus.COOKING));
        네명_테이블_주문.addOrderLineItems(new OrderLineItems(List.of(후라이드_2개_메뉴_1개_주문항목)));

        // when
        final List<Order> actual = orderService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(세명_테이블_주문);
            softAssertions.assertThat(actual.get(1)).isEqualTo(네명_테이블_주문);
        });
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        // given
        final OrderTable 주문_테이블 = orderTableRepository.save(new OrderTable(3, false));
        final OrderLineItem 주문항목 = new OrderLineItem(후라이드_2개_메뉴, 1l);
        final Order 주문 = orderRepository.save(new Order(주문_테이블, OrderStatus.COOKING));
        주문.addOrderLineItems(new OrderLineItems(List.of(주문항목)));

        final OrderStatus expect = OrderStatus.MEAL;
        final ChangeOrderStatusRequest order = new ChangeOrderStatusRequest(expect.name());

        // when
        final Order actual = orderService.changeOrderStatus(주문.getId(), order);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(expect);
    }

    @Test
    @DisplayName("주문 상태를 변경할 때 주문 상태가 이미 COMPLETION이면 예외가 발생한다")
    void changeOrderStatus_orderStatusCompletion() {
        // given
        final OrderTable 주문_테이블 = orderTableRepository.save(new OrderTable(3, false));
        final OrderLineItem 주문항목 = new OrderLineItem(후라이드_2개_메뉴, 1l);
        final Order 완료된_주문 = orderRepository.save(new Order(주문_테이블, OrderStatus.COMPLETION));
        완료된_주문.addOrderLineItems(new OrderLineItems(List.of(주문항목)));

        final OrderStatus newOrderStatus = OrderStatus.MEAL;
        final ChangeOrderStatusRequest order = new ChangeOrderStatusRequest(newOrderStatus.name());

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(완료된_주문.getId(), order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태를 변경할 수 없습니다.");
    }
}
