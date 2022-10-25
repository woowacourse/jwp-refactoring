package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fakedao.MenuFakeDao;
import kitchenpos.fakedao.MenuGroupFakeDao;
import kitchenpos.fakedao.OrderFakeDao;
import kitchenpos.fakedao.OrderLineItemFakeDao;
import kitchenpos.fakedao.OrderTableFakeDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    private MenuDao menuDao = new MenuFakeDao();
    private MenuGroupDao menuGroupDao = new MenuGroupFakeDao();
    private OrderDao orderDao = new OrderFakeDao();
    private OrderLineItemDao orderLineItemDao = new OrderLineItemFakeDao();
    private OrderTableDao orderTableDao = new OrderTableFakeDao();

    private OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    @DisplayName("주문을 생성할 때")
    @Nested
    class Create {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
            Menu menu = menuDao.save(new Menu("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), new ArrayList<>()));
            ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
            orderLineItems.add(orderLineItemDao.save(new OrderLineItem(menu.getId(), 3)));
            // when
            Order order = orderService.create(new Order(
                    orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems)
            );

            // then
            assertThat(order).isNotNull();
        }

    }
}
