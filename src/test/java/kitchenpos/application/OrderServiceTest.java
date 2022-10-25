package kitchenpos.application;

import static kitchenpos.Fixtures.메뉴상품_후라이드;
import static kitchenpos.Fixtures.빈테이블_1;
import static kitchenpos.Fixtures.상품_후라이드;
import static kitchenpos.Fixtures.주문_테이블1;
import static kitchenpos.Fixtures.주문아이템;
import static kitchenpos.Fixtures.주문아이템_후라이드;
import static kitchenpos.Fixtures.테이블_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

@SuppressWarnings("NonAsciiCharacters")
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
        productDao.save(상품_후라이드());
        menuProductDao.save(메뉴상품_후라이드());
        orderTableDao.save(테이블_1());
    }

    @DisplayName("주문을 추가하면 주문 목록에 추가된다.")
    @Test
    void create() {
        Order 주문 = orderService.create(주문_테이블1());

        assertThat(orderService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(주문);
    }

    @DisplayName("하나 이상의 메뉴를 주문해야 한다.")
    @Test
    void create_noMenu() {
        ArrayList<OrderLineItem> 빈_메뉴들 = new ArrayList<>();
        Order 주문 = 주문_테이블1(빈_메뉴들);

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("하나 이상의 메뉴를 주문해야 한다.");
    }

    @DisplayName("주문한 메뉴들은 모두 DB에 등록되어야 한다.")
    @Test
    void create_invalidMenu() {
        long 잘못된_메뉴_ID = 500L;
        Order 주문 = new Order(1L, 1L, OrderStatus.MEAL.name(),
                LocalDateTime.now(), List.of(주문아이템(잘못된_메뉴_ID)));

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문한 메뉴들은 모두 DB에 등록되어야 한다.");
    }

    @DisplayName("주문 테이블은 DB에 등록되어야 한다.")
    @Test
    void create_invalidTable() {
        long 잘못된_테이블_ID = 500L;
        Order 주문 = new Order(1L, 잘못된_테이블_ID, OrderStatus.MEAL.name(),
                LocalDateTime.now(), List.of(주문아이템_후라이드()));

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 DB에 등록되어야 한다.");
    }

    @DisplayName("주문 테이블은 손님이 존재해야 한다.")
    @Test
    void create_noCustomer() {
        OrderTable 빈테이블_1 = orderTableDao.save(빈테이블_1());
        Order 주문 = new Order(1L, 빈테이블_1.getId(), OrderStatus.MEAL.name(),
                LocalDateTime.now(), List.of(주문아이템_후라이드()));

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 손님이 존재해야 한다.");
    }

    @DisplayName("주문 상태를 변경하면 변경된 주문 상태가 반영된다.")
    @Test
    void changeOrderStatus() {
        Order 주문_테이블1 = orderService.create(주문_테이블1());
        String 주문상태 = OrderStatus.MEAL.name();
        주문_테이블1.updateOrderStatus(주문상태);
        assertThat(주문_테이블1.getOrderStatus()).isEqualTo(주문상태);

        orderService.changeOrderStatus(주문_테이블1.getId(), 주문_테이블1);

        assertThat(orderService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(주문_테이블1);
    }

    @DisplayName("주문 상태가 COMPLETION일 시 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus_noComplete() {
        Order 주문_테이블1 = orderService.create(주문_테이블1());

        주문_테이블1.updateOrderStatus(OrderStatus.COMPLETION.name());
        Order 완료된_주문 = orderService.changeOrderStatus(주문_테이블1.getId(), 주문_테이블1);
        assertThat(완료된_주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(완료된_주문.getId(), 완료된_주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 COMPLETION일 시 주문 상태를 변경할 수 없다.");
    }
}
