package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductDao productDao;

    @Autowired
    MenuProductDao menuProductDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    OrderTableDao orderTableDao;

    @BeforeEach
    void setup() {
        productDao.save(Fixtures.상품_후라이드());
        menuProductDao.save(Fixtures.메뉴상품_후라이드());
        orderTableDao.save(Fixtures.테이블_1());
    }

    @DisplayName("주문을 추가하면 주문 목록에 추가된다.")
    @Test
    void create() {
        Order 주문 = Fixtures.주문_테이블1_후라이드();

        Order saved = orderService.create(주문);

        assertThat(orderService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(saved);
    }

    @DisplayName("하나 이상의 메뉴를 주문해야 한다.")
    @Test
    void create_noMenu() {
        ArrayList<OrderLineItem> emptyItem = new ArrayList<>();
        Order 주문 = new Order(1L, 1L, OrderStatus.MEAL.name(),
                LocalDateTime.now(), emptyItem);

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("하나 이상의 메뉴를 주문해야 한다.");
    }

    @DisplayName("주문한 메뉴들은 모두 DB에 등록되어야 한다.")
    @Test
    void create_invalidMenu() {
        long menuId = 500L;
        OrderLineItem invalidItem = new OrderLineItem(1L, 1L, menuId, 10);
        Order 주문 = new Order(1L, 1L, OrderStatus.MEAL.name(),
                LocalDateTime.now(), List.of(invalidItem));

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문한 메뉴들은 모두 DB에 등록되어야 한다.");
    }

    @DisplayName("주문 테이블은 DB에 등록되어야 한다.")
    @Test
    void create_invalidTable() {
        long orderTableId = 500L;
        Order 주문 = new Order(1L, orderTableId, OrderStatus.MEAL.name(),
                LocalDateTime.now(), List.of(Fixtures.주문아이템_후라이드()));

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 DB에 등록되어야 한다.");
    }

    @DisplayName("주문 테이블은 손님이 존재해야 한다.")
    @Test
    void create_noCustomer() {
        OrderTable orderTable = orderTableDao.save(Fixtures.빈테이블_1());
        Order 주문 = new Order(1L, orderTable.getId(), OrderStatus.MEAL.name(),
                LocalDateTime.now(), List.of(Fixtures.주문아이템_후라이드()));

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 손님이 존재해야 한다.");
    }

    @DisplayName("주문 상태를 변경하면 변경된 주문 상태가 반영된다.")
    @Test
    void changeOrderStatus() {
        Order saved = orderService.create(Fixtures.주문_테이블1_후라이드());
        String 주문상태 = OrderStatus.MEAL.name();
        saved.updateOrderStatus(주문상태);
        assertThat(saved.getOrderStatus()).isEqualTo(주문상태);

        orderService.changeOrderStatus(saved.getId(), saved);

        assertThat(orderService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(saved);
    }

    @DisplayName("주문 상태가 COMPLETION일 시 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus_noComplete() {
        Order 주문_후라이드 = orderService.create(Fixtures.주문_테이블1_후라이드());

        주문_후라이드.updateOrderStatus(OrderStatus.COMPLETION.name());
        Order saved = orderService.changeOrderStatus(주문_후라이드.getId(), 주문_후라이드);
        assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());

        saved.updateOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(saved.getId(), saved))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 COMPLETION일 시 주문 상태를 변경할 수 없다.");
    }
}
