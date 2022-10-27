package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
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
    private Menu savedMenu;

    @BeforeEach
    void saveData() {
        savedUnEmptyTable = dataSupport.saveOrderTable(2, false);

        final int price = 3500;
        final Product savedProduct = dataSupport.saveProduct("치킨마요", price);
        final MenuGroup savedMenuGroup = dataSupport.saveMenuGroup("추천 메뉴");
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);
        savedMenu = dataSupport.saveMenu("치킨마요", price, savedMenuGroup.getId(), menuProduct);
    }

    @DisplayName("테이블에 대해 메뉴를 주문하고 주문 상태를 조리로 변경한다.")
    @Test
    void create() {
        // given, when
        final Order request = RequestBuilder.ofOrder(savedMenu, savedUnEmptyTable);
        final Order savedOrder = orderService.create(request);

        // then
        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @DisplayName("주문 항목 없이 주문하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifNoItem() {
        final Order request = RequestBuilder.ofOrderWithoutMenu(savedUnEmptyTable);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("존재하지 않는 메뉴를 주문하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifMenuNotFound() {
        // given
        final Menu unsavedMenu = new Menu();
        unsavedMenu.setId(0L);

        // when, then
        final Order request = RequestBuilder.ofOrder(unsavedMenu, savedUnEmptyTable);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("존재하지 않는 테이블에 대해 주문하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableNotFound() {
        // given
        final OrderTable unsavedTable = new OrderTable();
        unsavedTable.setId(0L);

        // when, then
        final Order request = RequestBuilder.ofOrder(savedMenu, unsavedTable);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("빈 테이블에 대해 주문하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableIsEmpty() {
        // given
        final OrderTable emptyTable = dataSupport.saveOrderTable(0, true);

        // when, then
        final Order request = RequestBuilder.ofOrder(savedMenu, emptyTable);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문의 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1);

        final Order savedOrder1 =
                dataSupport.saveOrder(savedUnEmptyTable.getId(), OrderStatus.COOKING.name(), orderLineItem);
        final Order savedOrder2 =
                dataSupport.saveOrder(savedUnEmptyTable.getId(), OrderStatus.COOKING.name(), orderLineItem);

        // when
        final List<Order> orders = orderService.list();

        // then
        assertThat(orders)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList(savedOrder1, savedOrder2));
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order savedOrder = dataSupport.saveOrder(savedUnEmptyTable.getId(), OrderStatus.COOKING.name());
        final OrderStatus status = OrderStatus.MEAL;

        // when
        final Order request = RequestBuilder.ofOrder(status);
        orderService.changeOrderStatus(savedOrder.getId(), request);

        // then
        final Order order = dataSupport.findOrder(savedOrder.getId());
        assertThat(order.getOrderStatus()).isEqualTo(status.name());
    }

    @DisplayName("존재하지 않는 주문의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeOrderStatus_throwsException_ifOrderNotFound() {
        final Order request = RequestBuilder.ofOrder(OrderStatus.MEAL);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(0L, request));
    }

    @DisplayName("완료된 주문의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeOrderStatus_throwsException_whenOrderIsComplete() {
        // given
        final Order completeOrder = dataSupport.saveOrder(savedUnEmptyTable.getId(), OrderStatus.COMPLETION.name());

        // when, then
        final Order request = RequestBuilder.ofOrder(OrderStatus.MEAL);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(completeOrder.getId(), request));
    }
}
