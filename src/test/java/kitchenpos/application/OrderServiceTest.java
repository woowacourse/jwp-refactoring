package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.Fixtures;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
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

    @DisplayName("주문을 추가하면 주문_후라이드 목록에 추가된다.")
    @Test
    void create() {
        Order 주문 = Fixtures.주문_후라이드();

        Order saved = orderService.create(주문);

        assertThat(orderService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(saved);
    }

    @DisplayName("주문 상태를 변경하면 변경된 주문 상태가 반영된다.")
    @Test
    void changeOrderStatus() {
        Order saved = orderService.create(Fixtures.주문_후라이드());
        String 주문상태 = OrderStatus.MEAL.name();
        saved.updateOrderStatus(주문상태);
        assertThat(saved.getOrderStatus()).isEqualTo(주문상태);

        orderService.changeOrderStatus(saved.getId(), saved);

        assertThat(orderService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(saved);
    }
}
