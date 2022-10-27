package kitchenpos.application;

import static kitchenpos.support.DomainFixture.givenEmptyTable;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ApplicationTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 조회한다.")
    @Test
    void list() {
        MenuGroup menuGroup = 메뉴그룹_생성(new MenuGroup("메뉴그룹"));
        Menu menu = 메뉴_생성(new Menu("메뉴1", BigDecimal.valueOf(17_000), menuGroup.getId()));
        OrderTable orderTable = 주문테이블_생성(givenEmptyTable());
        Order order = 주문_생성(new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()));
        주문아이템_생성(new OrderLineItem(order.getId(), menu.getId(), 2));

        List<Order> orders = orderService.list();

        assertThat(orders).hasSize(1);
    }
}
