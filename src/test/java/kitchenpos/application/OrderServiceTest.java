package kitchenpos.application;

import static kitchenpos.common.constants.Constants.루나세트_이름;
import static kitchenpos.common.constants.Constants.메뉴_상품_수량;
import static kitchenpos.common.constants.Constants.사용가능_테이블;
import static kitchenpos.common.constants.Constants.사용중인_테이블;
import static kitchenpos.common.constants.Constants.야채곱창_가격;
import static kitchenpos.common.constants.Constants.야채곱창_이름;
import static kitchenpos.common.constants.Constants.테이블_손님_수;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.builder.MenuBuilder;
import kitchenpos.common.builder.MenuGroupBuilder;
import kitchenpos.common.builder.MenuProductBuilder;
import kitchenpos.common.builder.OrderBuilder;
import kitchenpos.common.builder.OrderLineItemBuilder;
import kitchenpos.common.builder.OrderTableBuilder;
import kitchenpos.common.builder.ProductBuilder;
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
import kitchenpos.domain.OrderMenu;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderUpdateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrdersResponse;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    private Menu 야채곱창_메뉴;
    private OrderMenu 주문한_야채곱창_메뉴;
    private OrderTable 야채곱창_주문_테이블;

    @Autowired
    OrderServiceTest(final OrderService orderService, final OrderRepository orderRepository,
                     final OrderTableRepository orderTableRepository, final ProductRepository productRepository,
                     final MenuGroupRepository menuGroupRepository, final MenuRepository menuRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
    }

    @BeforeEach
    void setUp() {
        MenuGroup 루나세트 = menuGroupRepository.save(메뉴_그룹_생성(루나세트_이름));

        Product 야채곱창 = productRepository.save(상품_생성(야채곱창_이름, 야채곱창_가격));
        MenuProduct 루나_야채곱창 = 메뉴_상품_생성(야채곱창, 메뉴_상품_수량);

        야채곱창_메뉴 = menuRepository.save(메뉴_생성(야채곱창_이름, 야채곱창_가격, 루나세트, 루나_야채곱창));
        주문한_야채곱창_메뉴 = new OrderMenu(야채곱창_메뉴.getName(), 야채곱창_메뉴.getPrice());
        야채곱창_주문_테이블 = orderTableRepository.save(주문_테이블_생성(테이블_손님_수, 사용중인_테이블));
    }

    @DisplayName("주문을 등록할 때, 주문 테이블이 빈 테이블 이면 예외가 발생한다")
    @Test
    void 주문을_등록할_때_주문_테이블이_빈_테이블_이면_예외가_발생() {
        // given
        OrderTable 빈_테이블 = orderTableRepository.save(주문_테이블_생성(테이블_손님_수, 사용가능_테이블));
        OrderLineItemRequest 야채곱창_주문항목_생성_요청 = new OrderLineItemRequest(야채곱창_메뉴.getId(), 메뉴_상품_수량);
        OrderCreateRequest 야채곱창_주문_생성_요청 = new OrderCreateRequest(빈_테이블.getId(), List.of(야채곱창_주문항목_생성_요청));

        // when & then
        assertThatThrownBy(() -> orderService.create(야채곱창_주문_생성_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void 주문을_등록한다() {
        // given
        OrderLineItemRequest 야채곱창_주문항목_생성_요청 = new OrderLineItemRequest(야채곱창_메뉴.getId(), 메뉴_상품_수량);
        OrderCreateRequest 야채곱창_주문_생성_요청 = new OrderCreateRequest(야채곱창_주문_테이블.getId(), List.of(야채곱창_주문항목_생성_요청));

        // when
        OrderResponse actual = orderService.create(야채곱창_주문_생성_요청);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(COOKING.name()),
                () -> assertThat(actual.getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("주문을 등록할 때, 주문 항목이 메뉴에 없으면 예외가 발생한다")
    @Test
    void 주문을_등록할_때_주문_항목이_메뉴에_없으면_예외가_발생한다() {
        // given
        Long 없는_메뉴_아이디 = 999L;
        OrderLineItemRequest 없는메뉴_주문항목_생성_요청 = new OrderLineItemRequest(없는_메뉴_아이디, 메뉴_상품_수량);
        OrderCreateRequest 야채곱창_주문_생성_요청 = new OrderCreateRequest(야채곱창_주문_테이블.getId(), List.of(없는메뉴_주문항목_생성_요청));

        // when & then
        assertThatThrownBy(() -> orderService.create(야채곱창_주문_생성_요청))
                .isInstanceOf(MenuNotFoundException.class);
    }

    @DisplayName("주문을 등록할 때, 주문 항목이 없으면 예외가 발생한다")
    @Test
    void 주문을_등록할_때_주문_항목이_없으면_예외가_발생한다() {
        // given
        OrderCreateRequest 야채곱창_주문_생성_요청 = new OrderCreateRequest(야채곱창_주문_테이블.getId(), Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> orderService.create(야채곱창_주문_생성_요청))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 목록이 없습니다.");
    }

    @DisplayName("주문을 등록할 때, 주문 테이블이 빈 테이블 이면 예외가 발생한다.")
    @Test
    void 주문을_등록할_때_주문_테이블이_빈_테이블_이면_예외가_발생한다() {
        // given
        OrderTable 빈_테이블 = orderTableRepository.save(주문_테이블_생성(테이블_손님_수, 사용가능_테이블));
        OrderLineItemRequest 야채곱창_주문항목_생성_요청 = new OrderLineItemRequest(야채곱창_메뉴.getId(), 메뉴_상품_수량);
        OrderCreateRequest 야채곱창_주문_생성_요청 = new OrderCreateRequest(빈_테이블.getId(), List.of(야채곱창_주문항목_생성_요청));

        // when & then
        assertThatThrownBy(() -> orderService.create(야채곱창_주문_생성_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록을_조회한다() {
        // given
        OrderLineItem 야채곱창_주문항목 = 주문_항목_생성(주문한_야채곱창_메뉴);
        Order 야채곱창_주문 = 주문_생성(야채곱창_주문_테이블, List.of(야채곱창_주문항목), COOKING);
        orderRepository.save(야채곱창_주문);

        // when
        OrdersResponse 주문들 = orderService.list();

        // then
        assertThat(주문들.getOrderResponses()).hasSize(1);
    }

    @DisplayName("주문의 주문 상태를 변경한다.")
    @Test
    void 주문의_주문_상태를_변경한다() {
        // given
        OrderLineItem 야채곱창_주문항목 = 주문_항목_생성(주문한_야채곱창_메뉴);
        Order 야채곱창_주문 = orderRepository.save(주문_생성(야채곱창_주문_테이블, List.of(야채곱창_주문항목), COOKING));

        // when
        OrderResponse 변경된_야채곱창_주문 = orderService.changeOrderStatus(야채곱창_주문.getId(),
                new OrderUpdateRequest(COOKING.name()));

        // then
        assertThat(변경된_야채곱창_주문.getOrderStatus()).isEqualTo(COOKING.name());
    }

    @DisplayName("주문의 주문 상태를 변경할 때 주문이 존재하지 않으면 예외가 발생한다.")
    @Test
    void 주문의_주문_상태를_변경할_때_주문이_존재하지_않으면_예외가_발생한다() {
        // given
        Long 없는_주문_아이디 = 999L;

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(없는_주문_아이디, new OrderUpdateRequest(COOKING.name())))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @DisplayName("주문의 주문 상태를 변경할 때 주문 상태가 계산이면 예외가 발생한다.")
    @Test
    void 주문의_주문_상태를_변경할_때_주문_상태가_계산이면_예외가_발생한다() {
        // given
        OrderLineItem 야채곱창_주문항목 = 주문_항목_생성(주문한_야채곱창_메뉴);
        Order 야채곱창_주문 = orderRepository.save(주문_생성(야채곱창_주문_테이블, List.of(야채곱창_주문항목), COMPLETION));
        Long 야채곱창_주문_아이디 = 야채곱창_주문.getId();

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(야채곱창_주문_아이디, new OrderUpdateRequest(COOKING.name())));
    }

    private Order 주문_생성(final OrderTable orderTable, final List<OrderLineItem> orderLineItems,
                        final OrderStatus orderStatus) {
        return new OrderBuilder()
                .orderTableId(orderTable.getId())
                .orderLineItems(orderLineItems)
                .orderStatus(orderStatus.name())
                .build();
    }

    private OrderLineItem 주문_항목_생성(final OrderMenu orderMenu) {
        return new OrderLineItemBuilder()
                .orderMenu(orderMenu)
                .quantity(1)
                .build();
    }

    private OrderTable 주문_테이블_생성(final int numberOfGuests, final boolean empty) {
        return new OrderTableBuilder()
                .numberOfGuests(numberOfGuests)
                .empty(empty)
                .build();
    }

    private Menu 메뉴_생성(final String name, final BigDecimal price, final MenuGroup menuGroup,
                       final MenuProduct menuProduct) {
        return new MenuBuilder()
                .name(name)
                .price(price)
                .menuGroupId(menuGroup.getId())
                .menuProducts(List.of(menuProduct))
                .build();
    }

    private MenuProduct 메뉴_상품_생성(final Product 야채곱창, final long quantity) {
        return new MenuProductBuilder()
                .product(야채곱창)
                .quantity(quantity)
                .build();
    }

    private MenuGroup 메뉴_그룹_생성(final String name) {
        return new MenuGroupBuilder()
                .name(name)
                .build();
    }

    private Product 상품_생성(final String name, final BigDecimal price) {
        return new ProductBuilder()
                .name(name)
                .price(price)
                .build();
    }
}
