package kitchenpos.application;

import static kitchenpos.common.fixtures.MenuGroupFixtures.루나세트_이름;
import static kitchenpos.common.fixtures.ProductFixtures.야채곱창_가격;
import static kitchenpos.common.fixtures.ProductFixtures.야채곱창_이름;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.common.builder.MenuBuilder;
import kitchenpos.common.builder.MenuGroupBuilder;
import kitchenpos.common.builder.MenuProductBuilder;
import kitchenpos.common.builder.OrderBuilder;
import kitchenpos.common.builder.OrderLineItemBuilder;
import kitchenpos.common.builder.OrderTableBuilder;
import kitchenpos.common.builder.ProductBuilder;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    private Menu 야채곱창_메뉴;

    private OrderTable 야채곱창_주문_테이블;

    @BeforeEach
    void setUp() {
        Product 야채곱창 = new ProductBuilder()
                .name(야채곱창_이름)
                .price(야채곱창_가격)
                .build();
        야채곱창 = productService.create(야채곱창);

        MenuGroup 루나세트 = new MenuGroupBuilder()
                .name(루나세트_이름)
                .build();
        루나세트 = menuGroupService.create(루나세트);

        MenuProduct 루나_야채곱창 = new MenuProductBuilder()
                .productId(야채곱창.getId())
                .quantity(1)
                .build();

        야채곱창_메뉴 = new MenuBuilder()
                .name(야채곱창_이름)
                .price(야채곱창_가격)
                .menuGroupId(루나세트.getId())
                .menuProducts(List.of(루나_야채곱창))
                .build();
        야채곱창_메뉴 = menuService.create(야채곱창_메뉴);

        야채곱창_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(4)
                .empty(false)
                .build();
        야채곱창_주문_테이블 = tableService.create(야채곱창_주문_테이블);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void 주문을_등록한다() {
        // given
        OrderLineItem 야채곱창_주문항목 = new OrderLineItemBuilder()
                .menuId(야채곱창_메뉴.getId())
                .quantity(1)
                .build();

        Order 야채곱창_주문 = new OrderBuilder()
                .orderTableId(야채곱창_주문_테이블.getId())
                .orderLineItems(List.of(야채곱창_주문항목))
                .build();

        // when
        Order actual = orderService.create(야채곱창_주문);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(COOKING.name()),
                () -> assertThat(actual.getOrderLineItems()).extracting(OrderLineItem::getMenuId)
                        .contains(야채곱창_메뉴.getId())
        );
    }

    @DisplayName("주문을 등록할 때, 주문 항목이 메뉴에 없으면 예외가 발생한다")
    @Test
    void 주문을_등록할_때_주문_항목이_메뉴에_없으면_예외가_발생한다() {
        // given
        Long 잘못된_메뉴_아이디 = -1L;
        OrderLineItem 야채곱창_주문항목 = new OrderLineItemBuilder()
                .menuId(잘못된_메뉴_아이디)
                .quantity(1)
                .build();

        Order 야채곱창_주문 = new OrderBuilder()
                .orderTableId(야채곱창_주문_테이블.getId())
                .orderLineItems(List.of(야채곱창_주문항목))
                .build();

        // when & then
        assertThatThrownBy(() -> orderService.create(야채곱창_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록할 때, 주문 항목이 없으면 예외가 발생한다")
    @Test
    void 주문을_등록할_때_주문_항목이_없으면_예외가_발생한다() {
        // given
        Order 야채곱창_주문 = new OrderBuilder()
                .orderTableId(야채곱창_주문_테이블.getId())
                .orderLineItems(Collections.emptyList())
                .build();

        // when & then
        assertThatThrownBy(() -> orderService.create(야채곱창_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록할 때, 주문 테이블이 없으면 예외가 발생한다.")
    @Test
    void 주문을_등록할_때_주문_테이블이_없으면_예외가_발생한다() {
        // given
        OrderLineItem 야채곱창_주문항목 = new OrderLineItemBuilder()
                .menuId(야채곱창_메뉴.getId())
                .quantity(1)
                .build();

        Order 야채곱창_주문 = new OrderBuilder()
                .orderTableId(null)
                .orderLineItems(List.of(야채곱창_주문항목))
                .build();

        // when & then
        assertThatThrownBy(() -> orderService.create(야채곱창_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록할 때, 주문 테이블이 빈 테이블 이면 예외가 발생한다.")
    @Test
    void 주문을_등록할_때_주문_테이블이_빈_테이블_이면_예외가_발생한다() {
        // given
        OrderTable 빈_테이블 = new OrderTableBuilder()
                .numberOfGuests(4)
                .empty(true)
                .build();
        빈_테이블 = tableService.create(빈_테이블);

        OrderLineItem 야채곱창_주문항목 = new OrderLineItemBuilder()
                .menuId(야채곱창_메뉴.getId())
                .quantity(1)
                .build();

        Order 야채곱창_주문 = new OrderBuilder()
                .orderTableId(빈_테이블.getId())
                .orderLineItems(List.of(야채곱창_주문항목))
                .build();

        // when & then
        assertThatThrownBy(() -> orderService.create(야채곱창_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록을_조회한다() {
        // given
        OrderLineItem 야채곱창_주문항목 = new OrderLineItemBuilder()
                .menuId(야채곱창_메뉴.getId())
                .quantity(1)
                .build();

        Order 야채곱창_주문 = new OrderBuilder()
                .orderTableId(야채곱창_주문_테이블.getId())
                .orderLineItems(List.of(야채곱창_주문항목))
                .build();

        orderService.create(야채곱창_주문);

        // when
        List<Order> 주문들 = orderService.list();

        // then
        assertThat(주문들).extracting(Order::getOrderTableId)
                .contains(야채곱창_주문_테이블.getId());
    }

    @DisplayName("주문의 주문 상태를 변경한다.")
    @Test
    void 주문의_주문_상태를_변경한다() {
        // given
        OrderLineItem 야채곱창_주문항목 = new OrderLineItemBuilder()
                .menuId(야채곱창_메뉴.getId())
                .quantity(1)
                .build();

        Order 야채곱창_주문 = new OrderBuilder()
                .orderTableId(야채곱창_주문_테이블.getId())
                .orderLineItems(List.of(야채곱창_주문항목))
                .build();

        야채곱창_주문 = orderService.create(야채곱창_주문);

        // when
        Order 변경된_야채곱창_주문 = orderService.changeOrderStatus(야채곱창_주문.getId(), new Order(COOKING.name()));

        // then
        assertThat(변경된_야채곱창_주문.getOrderStatus()).isEqualTo(COOKING.name());
    }

    @DisplayName("주문의 주문 상태를 변경할 때 주문이 존재하지 않으면 예외가 발생한다.")
    @Test
    void 주문의_주문_상태를_변경할_때_주문이_존재하지_않으면_예외가_발생한다() {
        // given
        Long 잘못된_주문_아이디 = -1L;

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(잘못된_주문_아이디, new Order(COOKING.name())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문 상태를 변경할 때 주문 상태가 계산이면 예외가 발생한다.")
    @Test
    void 주문의_주문_상태를_변경할_때_주문_상태가_계산이면_예외가_발생한다() {
        // given
        OrderLineItem 야채곱창_주문항목 = new OrderLineItemBuilder()
                .menuId(야채곱창_메뉴.getId())
                .quantity(1)
                .build();

        Order 야채곱창_주문 = new OrderBuilder()
                .orderTableId(야채곱창_주문_테이블.getId())
                .orderLineItems(List.of(야채곱창_주문항목))
                .build();

        야채곱창_주문 = orderService.create(야채곱창_주문);
        Long 야채곱창_주문_아이디 = 야채곱창_주문.getId();

        orderService.changeOrderStatus(야채곱창_주문_아이디, new Order(COMPLETION.name()));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(야채곱창_주문_아이디, new Order(COOKING.name())));
    }
}