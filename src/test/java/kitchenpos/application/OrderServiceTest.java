package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderStatusRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ApplicationTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 조회한다.")
    @Test
    void list() {
        MenuGroup menuGroup = 메뉴그룹_생성(new MenuGroup("메뉴그룹"));
        Product product = 상품_생성(new Product("상품1", BigDecimal.valueOf(19_000)));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1, BigDecimal.valueOf(19_000));
        Menu menu = 메뉴_생성(Menu.create("메뉴1", BigDecimal.valueOf(17_000), menuGroup.getId(), List.of(menuProduct)));
        OrderTable orderTable = 주문테이블_생성(new OrderTable(null, 5, false));

        orderService.create(new OrderRequest(orderTable.getId(), List.of(new OrderLineItemRequest(menu.getId(), 2))));

        List<OrderResponse> orders = orderService.list();

        assertThat(orders).hasSize(1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatus(String status) {
        MenuGroup menuGroup = 메뉴그룹_생성(new MenuGroup("메뉴그룹"));
        Product product = 상품_생성(new Product("상품1", BigDecimal.valueOf(19_000)));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1, BigDecimal.valueOf(19_000));
        Menu menu = 메뉴_생성(Menu.create("메뉴1", BigDecimal.valueOf(17_000), menuGroup.getId(), List.of(menuProduct)));
        OrderTable orderTable = 주문테이블_생성(new OrderTable(null, 5, false));
        OrderResponse orderResponse = orderService.create(
                new OrderRequest(orderTable.getId(), List.of(new OrderLineItemRequest(menu.getId(), 2))));

        orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusRequest(status));

        Order order = orderDao.findById(orderResponse.getId())
                .orElseThrow();

        assertThat(order.getOrderStatus()).isEqualTo(status);
    }
}
