package kitchenpos.application;

import static kitchenpos.fixture.MenuFactory.menu;
import static kitchenpos.fixture.MenuGroupFactory.menuGroup;
import static kitchenpos.fixture.OrderFactory.order;
import static kitchenpos.fixture.OrderTableFactory.emptyTable;
import static kitchenpos.fixture.OrderTableFactory.notEmptyTable;
import static kitchenpos.fixture.ProductFactory.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.OrderStatus;
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
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("주문 등록")
    @Test
    void create() {
        final var pizza = productDao.save(product("피자", 10_000));
        final var coke = productDao.save(product("콜라", 1_000));

        final var italian = menuGroupDao.save(menuGroup("양식"));

        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));
        final var cokeMenu = menuDao.save(menu("콜라파티", italian, List.of(coke)));

        final var table = orderTableDao.save(notEmptyTable(2));

        final var order = order(table, pizzaMenu, cokeMenu);

        final var result = orderService.create(order);
        assertAll(
                () -> assertThat(result.getOrderedTime()).isEqualTo(order.getOrderedTime()),
                () -> assertThat(result.getOrderTableId()).isEqualTo(order.getOrderTableId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(order.getOrderStatus())
        );
    }

    @DisplayName("주문 항목이 비어있다면 등록 시 예외 발생")
    @Test
    void create_emptyOrderLineItems_throwsException() {
        final var table = orderTableDao.save(emptyTable(2));

        final var order = order(table);

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴가 중복된다면 등록 시 예외 발생")
    @Test
    void create_duplicatedMenuInOrderLineItems_throwsException() {
        final var pizza = productDao.save(product("피자", 10_000));
        final var italian = menuGroupDao.save(menuGroup("양식"));
        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));

        final var table = orderTableDao.save(emptyTable(2));

        final var order = order(table, pizzaMenu, pizzaMenu);

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 상태라면 등록 시 예외 발생")
    @Test
    void create_orderTableIsEmptyTrue_throwsException() {
        final var pizza = productDao.save(product("피자", 10_000));
        final var italian = menuGroupDao.save(menuGroup("양식"));
        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));

        final var table = orderTableDao.save(emptyTable(2));

        final var order = order(table, pizzaMenu);

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        final var pizza = productDao.save(product("피자", 10_000));
        final var italian = menuGroupDao.save(menuGroup("양식"));
        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));

        final var table = orderTableDao.save(notEmptyTable(2));

        final var saved = orderService.create(order(table, pizzaMenu));
        final var changed = order(table, pizzaMenu);
        changed.setOrderStatus(OrderStatus.MEAL.name());

        final var result = orderService.changeOrderStatus(saved.getId(), changed);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(saved.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(changed.getOrderStatus())
        );
    }

    @DisplayName("주문 상태가 COMPLETION이라면, 주문 상태 변경 시 예외 발생")
    @Test
    void changeOrderStatus_orderStatusIsCompletion_throwsException() {
        final var pizza = productDao.save(product("피자", 10_000));
        final var italian = menuGroupDao.save(menuGroup("양식"));
        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));

        final var table = orderTableDao.save(notEmptyTable(2));

        final var order = order(table, pizzaMenu);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.create(order);

        final var changed = order(table, pizzaMenu);
        changed.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(order.getId(), changed)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
