package kitchenpos.application;

import static kitchenpos.fixture.MenuTestFixture.떡볶이;
import static kitchenpos.fixture.ProductFixture.불맛_떡볶이;
import static kitchenpos.fixture.ProductFixture.짜장맛_떡볶이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.support.ServiceTestBase;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.OrderUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTestBase {

    private Long menuId;
    private Long orderTableId;

    @BeforeEach
    void 메뉴_및_주문_테이블_생성() {
        Long menuGroupId = menuGroupDao.save(MenuGroupFixture.분식.toEntity())
                .getId();

        List<MenuProduct> menuProducts = 메뉴_상품_목록(
                상품_생성(불맛_떡볶이),
                상품_생성(짜장맛_떡볶이)
        );

        Menu menu = 떡볶이.toEntity(menuGroupId, menuProducts);
        Menu savedMenu = menuDao.save(menu);
        menuId = savedMenu.getId();

        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            menuProduct.changeMenuId(menuId);
            menuProductDao.save(menuProduct);
        }

        orderTableId = 주문_테이블_생성().getId();
    }

    @Test
    void 주문_정상_생성() {
        // given
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        Order order = 주문(orderTableId, orderLineItems);

        // when
        OrderResponse savedOrder = orderService.create(toRequest(order));

        // then
        Optional<Order> actual = orderDao.findById(savedOrder.getId());
        assertThat(actual).isNotEmpty();
    }

    @Test
    void 주문_항목_0개인_경우_실패() {
        // given
        OrderCreateRequest request = new OrderCreateRequest(null, orderTableId, LocalDateTime.now(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void menu_id가_null인_주문_항목을_주문하는_경우_실패() {
        // given
        List<OrderLineItem> orderLineItems = Collections.singletonList(menuId가_null인_주문_항목());
        Order order = 주문(orderTableId, orderLineItems);

        // when & then
        assertThatThrownBy(() -> orderService.create(toRequest(order)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록_조회() {
        // given
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        Order order = 주문(orderTableId, orderLineItems);
        Order savedOrder = orderDao.save(order);

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders)
                .usingRecursiveComparison()
                .ignoringFields("orderLineItems")
                .isEqualTo(Collections.singletonList(OrderResponse.of(savedOrder)));
    }

    @Test
    void 주문_상태_정상_변경() {
        // given
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        Order order = 주문(orderTableId, orderLineItems);
        Order savedOrder = orderDao.save(order);

        OrderStatus changedStatus = OrderStatus.MEAL;
        savedOrder.changeOrderStatus(changedStatus);
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest(changedStatus.name());

        // when
        orderService.changeOrderStatus(savedOrder.getId(), orderUpdateRequest);

        // then
        Optional<Order> actual = orderDao.findById(savedOrder.getId());
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getOrderStatus()).isEqualTo(changedStatus);
    }

    @Test
    void 존재하지_않는_주문_상태_변경_시_실패() {
        // given
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        Order order = 주문(orderTableId, orderLineItems);
        orderDao.save(order);
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest(OrderStatus.COOKING.name());

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, orderUpdateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void COMPLETION_주문_상태_변경_시_실패() {
        // given
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        Order order = 주문(orderTableId, orderLineItems);

        Order savedOrder = orderDao.save(order);
        savedOrder.changeOrderStatus(OrderStatus.COMPLETION);
        orderDao.save(savedOrder);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(),
                new OrderUpdateRequest(OrderStatus.COMPLETION.name())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderLineItem menuId가_null인_주문_항목() {
        return 주문_항목(null);
    }

    private OrderLineItem 주문_항목() {
        return 주문_항목(menuId);
    }

    private OrderCreateRequest toRequest(final Order order) {
        return new OrderCreateRequest(order.getId(), order.getOrderTableId(),
                order.getOrderedTime(), toDtos(order));
    }

    private List<OrderLineItemDto> toDtos(final Order order) {
        return order.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItemDto(it.getSeq(), it.getOrderId(), it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
