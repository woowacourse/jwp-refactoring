package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 주문을_생성한다() {
        // given
        final OrderCreateRequest request = 후라이드_세트_메뉴_주문_요청();

        // when
        OrderResponse actual = orderService.create(request);

        // then
        assertAll(
                () -> assertThat(actual.getOrderTableId()).isEqualTo(request.getOrderTableId()),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderedTime()).isCloseTo(LocalDateTime.now(),
                        new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS)),
                () -> assertThat(actual.getOrderLineItems()).extracting("orderId")
                        .containsExactly(actual.getId())
        );
    }

    @Test
    void 주문항목이_null이면_주문을_생성할_수_없다() {
        // given
        OrderCreateRequest request = 주문_항목들로_주문_요청(null);

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문항목이_없으면_주문을_생성할_수_없다() {
        // given
        OrderCreateRequest request = 주문_항목들로_주문_요청(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문항목에_들어있는_메뉴가_존재하지않으면_주문을_생성할_수_없다() {
        // given
        OrderLineItemRequest invalidOrderItem = new OrderLineItemRequest(-1L, 2L);

        OrderCreateRequest request = 주문_항목들로_주문_요청(Collections.singletonList(invalidOrderItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 같은_메뉴가_다른_주문항목에_존재하면_주문을_생성할_수_없다() {
        // given
        Menu menu = 후라이드_세트_메뉴();

        OrderLineItemRequest request1 = new OrderLineItemRequest(menu.getId(), 2L);
        OrderLineItemRequest request2 = new OrderLineItemRequest(menu.getId(), 1L);

        OrderCreateRequest request = 주문_항목들로_주문_요청(Arrays.asList(request1, request2));

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문에_등록된_주문_테이블이_존재하지않으면_주문을_생성할_수_없다() {
        // given
        OrderTable invalidOrderTable = new OrderTable(-1L, null, 0, false);
        OrderCreateRequest request = 주문_요청(invalidOrderTable, 후라이드_세트_메뉴_주문_항목_요청());

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_상태라면_주문을_생성할_수_없다() {
        // given
        OrderCreateRequest request = 주문_요청(빈_상태의_주문_테이블(), 후라이드_세트_메뉴_주문_항목_요청());

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록을_조회한다() {
        // given
        OrderCreateRequest request = 후라이드_세트_메뉴_주문_요청();
        orderService.create(request);

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COOKING"})
    void 주문_상태를_변경한다(String orderStatus) {
        // given
        OrderResponse response = orderService.create(후라이드_세트_메뉴_주문_요청());

        final OrderChangeOrderStatusRequest request = new OrderChangeOrderStatusRequest(orderStatus);

        // when
        OrderResponse actual = orderService.changeOrderStatus(response.getId(), request);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(orderStatus);
    }

    @Test
    void 주문이_등록되어있지_않으면_주문_상태를_변경할_수_없다() {
        // given
        long invalidOrderId = -1L;

        OrderChangeOrderStatusRequest updateOrder = new OrderChangeOrderStatusRequest(OrderStatus.MEAL.name());

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId, updateOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문이_계산완료_상태이면_주문_상태를_변경할_수_없다() {
        // given
        OrderResponse response = orderService.create(후라이드_세트_메뉴_주문_요청());
        final OrderChangeOrderStatusRequest request = new OrderChangeOrderStatusRequest(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(response.getId(), request);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(response.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public OrderTable 주문_테이블() {
        return orderTableDao.save(new OrderTable(null, null, 0, false));
    }

    public OrderTable 빈_상태의_주문_테이블() {
        OrderTable emptyOrderTable = new OrderTable(null, null, 0, true);
        return orderTableDao.save(emptyOrderTable);
    }

    private OrderCreateRequest 후라이드_세트_메뉴_주문_요청() {
        return 주문_요청(주문_테이블(), 후라이드_세트_메뉴_주문_항목_요청());
    }

    private OrderCreateRequest 주문_항목들로_주문_요청(List<OrderLineItemRequest> orderLineItems) {
        return 주문_요청(주문_테이블(), orderLineItems);
    }

    private OrderCreateRequest 주문_요청(OrderTable orderTable, List<OrderLineItemRequest> orderLineItems) {
        return new OrderCreateRequest(orderTable.getId(), orderLineItems);
    }

    private List<OrderLineItemRequest> 후라이드_세트_메뉴_주문_항목_요청() {
        return Collections.singletonList(new OrderLineItemRequest(후라이드_세트_메뉴().getId(), 1));
    }

    public Menu 후라이드_세트_메뉴() {
        Menu menu = new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), 추천메뉴().getId());

        return menuDao.save(menu);
    }

    public MenuGroup 추천메뉴() {
        return menuGroupDao.save(new MenuGroup("추천메뉴"));
    }
}
