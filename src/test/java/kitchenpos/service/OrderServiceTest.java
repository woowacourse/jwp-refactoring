package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        // given
        Order order = createOrderFixture();

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder).usingRecursiveComparison()
            .ignoringFields("id", "orderedTime")
            .isEqualTo(order);
    }

    @Test
    @DisplayName("전체 주문을 조회한다.")
    void list() {
        // given
        Order savedOrder = orderService.create(createOrderFixture());

        // when
        List<Order> result = orderService.list();

        // then
        assertThat(result).contains(savedOrder);
        assertThat(result).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus_meal(String orderStatus) {
        // given
        Order order = orderService.create(createOrderFixture());
        order.setOrderStatus(orderStatus);

        // when
        orderService.changeOrderStatus(order.getId(), order);
        Order changedOrder = orderDao.findById(order.getId()).orElseThrow();

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(orderStatus);
    }

    private Order createOrderFixture() {
        MenuGroup menuGroup = new MenuGroup("세마리메뉴");
        long menuGroupId = menuGroupService.create(menuGroup).getId();

        Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        long productId = productService.create(product).getId();

        Menu menu = new Menu("후라이드+후라이드+후라이드", new BigDecimal(48000), menuGroupId,
            List.of(new MenuProduct(productId, 3)));
        long menuId = menuService.create(menu).getId();

        OrderTable orderTable = new OrderTable(1, false);
        long tableId = tableService.create(orderTable).getId();

        OrderLineItem orderLineItem = new OrderLineItem(1L, menuId, 1);
        return new Order(tableId, List.of(orderLineItem));
    }
}
