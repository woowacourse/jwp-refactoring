package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.order.OrderedMenu;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.product.Product;
import kitchenpos.support.DataSupport;
import kitchenpos.support.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private DataSupport dataSupport;

    private OrderTable savedUnEmptyTable;
    private OrderedMenu orderedMenu;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void saveData() {
        savedUnEmptyTable = dataSupport.saveOrderTable(2, false);

        final int price = 3500;
        final Product savedProduct = dataSupport.saveProduct("치킨마요", price);
        final MenuGroup savedMenuGroup = dataSupport.saveMenuGroup("추천 메뉴");
        final MenuProduct menuProduct = MenuProduct.ofUnsaved(null, savedProduct, 1L);
        orderedMenu = dataSupport.saveMenuAndGetInfo("치킨마요", price, savedMenuGroup.getId(), menuProduct);
        orderLineItem = OrderLineItem.ofUnsaved(null, orderedMenu, 1);
    }

    @DisplayName("테이블에 대해 메뉴를 주문하고 주문 상태를 조리로 변경한다.")
    @Test
    void create() {
        // given, when
        final OrderRequest request = RequestBuilder.ofOrder(orderedMenu, savedUnEmptyTable);
        final OrderResponse response = orderService.create(request);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @DisplayName("주문 항목 없이 주문하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifNoItem() {
        final OrderRequest request = RequestBuilder.ofOrderWithoutMenu(savedUnEmptyTable);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("존재하지 않는 메뉴를 주문하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifMenuNotFound() {
        // given
        final Menu unsavedMenu = new Menu(0L, "없는 메뉴", new BigDecimal(0), 0L);

        // when, then
        final OrderRequest request = RequestBuilder.ofOrder(unsavedMenu, savedUnEmptyTable);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("존재하지 않는 테이블에 대해 주문하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableNotFound() {
        // given
        final OrderTable unsavedTable = new OrderTable(0L, null, 0, true);

        // when, then
        final OrderRequest request = RequestBuilder.ofOrder(orderedMenu, unsavedTable);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("빈 테이블에 대해 주문하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableIsEmpty() {
        // given
        final OrderTable emptyTable = dataSupport.saveOrderTable(0, true);

        // when, then
        final OrderRequest request = RequestBuilder.ofOrder(orderedMenu, emptyTable);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문의 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Order savedOrder1 =
                dataSupport.saveOrder(savedUnEmptyTable.getId(), OrderStatus.COOKING, orderLineItem);
        final Order savedOrder2 =
                dataSupport.saveOrder(savedUnEmptyTable.getId(), OrderStatus.COOKING, orderLineItem);
        final List<OrderResponse> expected = OrderResponse.from(Arrays.asList(savedOrder1, savedOrder2));

        // when
        final List<OrderResponse> responses = orderService.list();

        // then
        assertThat(responses)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order savedOrder = dataSupport.saveOrder(savedUnEmptyTable.getId(), OrderStatus.COOKING, orderLineItem);
        final OrderStatus status = OrderStatus.MEAL;

        // when
        final OrderStatusRequest request = RequestBuilder.ofOrderStatus(status);
        orderService.changeOrderStatus(savedOrder.getId(), request);

        // then
        final Order order = dataSupport.findOrder(savedOrder.getId());
        assertThat(order.getOrderStatus()).isEqualTo(status.name());
    }

    @DisplayName("존재하지 않는 주문의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeOrderStatus_throwsException_ifOrderNotFound() {
        final OrderStatusRequest request = RequestBuilder.ofOrderStatus(OrderStatus.MEAL);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(0L, request));
    }

    @DisplayName("완료된 주문의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeOrderStatus_throwsException_whenOrderIsComplete() {
        // given
        final Order completeOrder = dataSupport.saveOrder(savedUnEmptyTable.getId(), OrderStatus.COMPLETION,
                orderLineItem);

        // when, then
        final OrderStatusRequest request = RequestBuilder.ofOrderStatus(OrderStatus.MEAL);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(completeOrder.getId(), request));
    }
}
