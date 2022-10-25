package kitchenpos.application;

import static kitchenpos.fixture.MenuTestFixture.떡볶이;
import static kitchenpos.fixture.ProductFixture.불맛_떡볶이;
import static kitchenpos.fixture.ProductFixture.짜장맛_떡볶이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.fixture.MenuGroupFixture;
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
            menuProduct.setMenuId(menuId);
            menuProductDao.save(menuProduct);
        }

        orderTableId = 주문_테이블_생성().getId();
    }

    @Test
    void 주문_정상_생성() {
        // given
        Order order = new Order();
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);

        // when
        Order savedOrder = orderService.create(order);

        // then
        Optional<Order> actual = orderDao.findById(savedOrder.getId());
        assertThat(actual).isNotEmpty();
    }

    @Test
    void 주문_항목_0개인_경우_실패() {
        // given
        Order order = new Order();
        order.setOrderLineItems(new ArrayList<>());
        order.setOrderTableId(orderTableId);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void menu_id가_null인_주문_항목을_주문하는_경우_실패() {
        // given
        Order order = new Order();
        List<OrderLineItem> orderLineItems = Collections.singletonList(menuId가_null인_주문_항목());
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록_조회() {
        // given
        Order order = new Order();
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);
        Order savedOrder = orderService.create(order);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders)
                .usingRecursiveComparison()
                .ignoringFields("orderLineItems")
                .isEqualTo(Collections.singletonList(savedOrder));

        assertThat(orders.get(0).getOrderLineItems())
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(orderLineItems);
    }

    @Test
    void 주문_상태_정상_변경() {
        // given
        Order order = new Order();
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);
        Order savedOrder = orderService.create(order);

        String changedStatus = OrderStatus.COOKING.name();
        savedOrder.setOrderStatus(changedStatus);

        // when
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // then
        Optional<Order> actual = orderDao.findById(savedOrder.getId());
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getOrderStatus()).isEqualTo(changedStatus);
    }

    @Test
    void 존재하지_않는_주문_상태_변경_시_실패() {
        // given
        Order order = new Order();
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);
        orderService.create(order);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void COMPLETION_주문_상태_변경_시_실패() {
        // given
        Order order = new Order();
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);

        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(savedOrder);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), savedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderLineItem menuId가_null인_주문_항목() {
        return 주문_항목(null);
    }

    private OrderLineItem 주문_항목() {
        return 주문_항목(menuId);
    }
}
