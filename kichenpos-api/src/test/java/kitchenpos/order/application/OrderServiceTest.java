package kitchenpos.order.application;

import kitchenpos.configuration.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.order.dto.CreateOrderLineItemDto;
import kitchenpos.order.dto.CreateOrderDto;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.domain.MenuFixture.menu;
import static kitchenpos.order.domain.OrderFixture.order;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
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
        final MenuProduct 후라이드_2개 = new MenuProduct(후라이드.getId(), 2L);
        후라이드_2개_메뉴 = menuRepository.save(menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개)));
    }

    @Test
    @DisplayName("주문을 등록한다")
    void create() {
        // given
        final OrderTable 주문_테이블 = orderTableRepository.save(new OrderTable(3, false));

        final CreateOrderLineItemDto 주문항목 = new CreateOrderLineItemDto(후라이드_2개_메뉴.getId(), 1L);
        final CreateOrderDto request = new CreateOrderDto(주문_테이블.getId(), List.of(주문항목));

        // when
        final Order actual = orderService.create(request);

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

        final List<CreateOrderLineItemDto> 빈_주문_항목 = Collections.emptyList();
        final CreateOrderDto invalidRequest = new CreateOrderDto(주문_테이블.getId(), 빈_주문_항목);

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 비어있습니다.");
    }

    @Test
    @DisplayName("주문을 등록할 때 주문항목의 개수와 메뉴 개수가 다르면 예외가 발생한다.")
    void create_invalidNumberOfOrderLineItems() {
        // given
        final OrderTable 주문_테이블 = orderTableRepository.save(new OrderTable(3, false));

        final CreateOrderLineItemDto 후라이드_2개_메뉴_1개 = new CreateOrderLineItemDto(후라이드_2개_메뉴.getId(), 1L);
        final CreateOrderLineItemDto 후라이드_2개_메뉴_2개 = new CreateOrderLineItemDto(후라이드_2개_메뉴.getId(), 2L);
        final CreateOrderDto invalidRequest = new CreateOrderDto(주문_테이블.getId(), List.of(후라이드_2개_메뉴_1개, 후라이드_2개_메뉴_2개));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목의 메뉴는 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("주문을 등록할 때 주문테이블을 찾을 수 없으면 예외가 발생한다")
    void create_invalidOrderTable() {
        // given
        final long invalidOrderTableId = -999L;
        final CreateOrderLineItemDto 주문항목 = new CreateOrderLineItemDto(후라이드_2개_메뉴.getId(), 1L);
        final CreateOrderDto invalidRequest = new CreateOrderDto(invalidOrderTableId, List.of(주문항목));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 테이블이 비어있으면 예외가 발생한다")
    void create_emptyTable() {
        // given
        final OrderTable 비어있는_테이블 = orderTableRepository.save(new OrderTable(3, true));
        final CreateOrderLineItemDto 주문항목 = new CreateOrderLineItemDto(후라이드_2개_메뉴.getId(), 1L);
        final CreateOrderDto invalidRequest = new CreateOrderDto(비어있는_테이블.getId(), List.of(주문항목));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 주문을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 목록을 조회한다")
    void list() {
        // given
        final OrderTable 세명_테이블 = orderTableRepository.save(new OrderTable(3, false));
        final OrderTable 네명_테이블 = orderTableRepository.save(new OrderTable(4, false));

        final OrderLineItem 후라이드_2개_메뉴_1개_주문항목1 = new OrderLineItem(후라이드_2개_메뉴.getId(), 1L);
        final OrderLineItem 후라이드_2개_메뉴_1개_주문항목2 = new OrderLineItem(후라이드_2개_메뉴.getId(), 1L);

        final Order 세명_테이블_주문 = orderRepository.save(order(세명_테이블.getId(), OrderStatus.COOKING, new OrderLineItems(List.of(후라이드_2개_메뉴_1개_주문항목1))));
        final Order 네명_테이블_주문 = orderRepository.save(order(네명_테이블.getId(), OrderStatus.COOKING, new OrderLineItems(List.of(후라이드_2개_메뉴_1개_주문항목2))));

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
        final OrderLineItem 주문항목 = new OrderLineItem(후라이드_2개_메뉴.getId(), 1L);
        final Order 주문 = orderRepository.save(order(주문_테이블.getId(), OrderStatus.COOKING, new OrderLineItems(List.of(주문항목))));

        final OrderStatus expect = OrderStatus.MEAL;
        final ChangeOrderStatusDto request = new ChangeOrderStatusDto(expect);

        // when
        final Order actual = orderService.changeOrderStatus(주문.getId(), request);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(expect);
    }

    @Test
    @DisplayName("주문 상태를 변경할 때 주문 상태가 이미 COMPLETION이면 예외가 발생한다")
    void changeOrderStatus_orderStatusCompletion() {
        // given
        final OrderTable 주문_테이블 = orderTableRepository.save(new OrderTable(3, false));
        final OrderLineItem 주문항목 = new OrderLineItem(후라이드_2개_메뉴.getId(), 1L);
        final Order 완료된_주문 = orderRepository.save(order(주문_테이블.getId(), OrderStatus.COMPLETION, new OrderLineItems(List.of(주문항목))));

        final Long completeOrderId = 완료된_주문.getId();
        final OrderStatus newOrderStatus = OrderStatus.MEAL;
        final ChangeOrderStatusDto request = new ChangeOrderStatusDto(newOrderStatus);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(completeOrderId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태를 변경할 수 없습니다.");
    }
}
