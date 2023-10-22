package kitchenpos.application;

import static kitchenpos.application.OrderServiceTest.OrderRequestFixture.주문_생성_요청;
import static kitchenpos.common.fixture.MenuFixture.메뉴;
import static kitchenpos.common.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.common.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.common.fixture.OrderFixture.주문;
import static kitchenpos.common.fixture.OrderLineItemFixture.주문_항목;
import static kitchenpos.common.fixture.OrderTableFixture.빈_주문_테이블;
import static kitchenpos.common.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.common.fixture.ProductFixture.상품;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemCreateRequest;
import kitchenpos.dto.order.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class OrderServiceTest {

    private static final BigDecimal PRICE = BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP);

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    private OrderLineItem orderLineItem;
    private Long orderTableId;
    private Long emptyOrderTableId;

    @BeforeEach
    void setUp() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹()).getId();
        Long productId = productDao.save(상품(PRICE)).getId();

        MenuProduct menuProduct = 메뉴_상품(productId);
        Long menuId = menuDao.save(메뉴(menuGroupId, PRICE, List.of(menuProduct))).getId();
        menuProduct.setMenuId(menuId);
        menuProductDao.save(menuProduct);

        orderTableId = orderTableDao.save(주문_테이블()).getId();
        emptyOrderTableId = orderTableDao.save(빈_주문_테이블()).getId();
        orderLineItem = 주문_항목(menuId);
    }

    @Test
    void 주문을_생성한다() {
        // given
        OrderCreateRequest order = 주문_생성_요청(orderTableId, List.of(orderLineItem));

        // when
        OrderResponse createdOrder = orderService.create(order);

        // then
        assertSoftly(softly -> {
            softly.assertThat(createdOrder.getId()).isNotNull();
            softly.assertThat(createdOrder).usingRecursiveComparison()
                    .ignoringFields("id", "orderLineItems.seq", "orderLineItems.orderId")
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(OrderResponse.from(주문(orderTableId, COOKING, List.of(orderLineItem))));
        });
    }

    @Test
    void 주문을_생성할_때_주문_항목이_비었으면_예외를_던진다() {
        // given
        OrderCreateRequest invalidOrder = 주문_생성_요청(orderTableId, List.of());

        // expect
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할_때_주문_항목_개수와_메뉴의_개수가_다르면_예외를_던진다() {
        // given
        OrderCreateRequest invalidOrder = 주문_생성_요청(orderTableId, List.of(orderLineItem, orderLineItem));

        // expect
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할_때_존재하지_않는_주문_테이블이면_예외를_던진다() {
        // given
        OrderCreateRequest invalidOrder = 주문_생성_요청(Long.MIN_VALUE, List.of(orderLineItem));

        // expect
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할_때_주문_테이블이_비었으면_예외를_던진다() {
        // given
        OrderCreateRequest invalidOrder = 주문_생성_요청(emptyOrderTableId, List.of(orderLineItem));

        // expect
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static class OrderRequestFixture {

        public static OrderCreateRequest 주문_생성_요청(Long orderTableId, List<OrderLineItem> orderLineItems) {
            List<OrderLineItemCreateRequest> items = orderLineItems.stream()
                    .map(OrderRequestFixture::주문_항목_생성_요청)
                    .collect(Collectors.toList());
            return new OrderCreateRequest(orderTableId, items);
        }

        public static OrderLineItemCreateRequest 주문_항목_생성_요청(OrderLineItem orderLineItem) {
            return new OrderLineItemCreateRequest(orderLineItem.getMenuId(), orderLineItem.getQuantityValue());
        }
    }
}
