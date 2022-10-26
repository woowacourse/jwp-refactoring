package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixtures.MenuFixtures;
import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.OrderTableFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;

@JdbcTest
class OrderLineItemDaoTest {

    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;
    private final MenuDao menuDao;

    @Autowired
    private OrderLineItemDaoTest(final DataSource dataSource) {
        this.orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
        this.orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        this.orderDao = new JdbcTemplateOrderDao(dataSource);
        this.menuDao = new JdbcTemplateMenuDao(dataSource);
    }

    @Test
    @DisplayName("주문 항목을 저장한다")
    void save() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableId(savedOrderTable.getId());
        final Order savedOrder = orderDao.save(order);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem(null, savedOrder.getId(), savedMenu.getId(), 2);

        // when
        final OrderLineItem saved = orderLineItemDao.save(orderLineItem);

        // then
        assertAll(
                () -> assertThat(saved.getSeq()).isNotNull(),
                () -> assertThat(saved.getOrderId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(saved.getMenuId()).isEqualTo(savedMenu.getId()),
                () -> assertThat(saved.getQuantity()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("존재하지 않은 주문으로 주문 항목을 저장하면 예외가 발생한다")
    void saveExceptionNotExistOrder() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        orderTableDao.save(orderTable);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem(null, -1L, savedMenu.getId(), 2);

        // when, then
        assertThatThrownBy(() -> orderLineItemDao.save(orderLineItem))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("존재하지 않은 메뉴로 주문 항목을 저장하면 예외가 발생한다")
    void saveExceptionNotExistMenu() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableId(savedOrderTable.getId());
        final Order savedOrder = orderDao.save(order);

        final OrderLineItem orderLineItem = new OrderLineItem(null, savedOrder.getId(), -1L, 2);

        // when, then
        assertThatThrownBy(() -> orderLineItemDao.save(orderLineItem))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("id로 주문 항목을 조회한다")
    void findById() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableId(savedOrderTable.getId());
        final Order savedOrder = orderDao.save(order);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem(null, savedOrder.getId(), savedMenu.getId(), 2);
        final OrderLineItem saved = orderLineItemDao.save(orderLineItem);

        // when
        final OrderLineItem foundOrderLineItem = orderLineItemDao.findById(saved.getSeq())
                .get();

        // then
        assertThat(foundOrderLineItem).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("id로 주문 항목을 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<OrderLineItem> orderLineItem = orderLineItemDao.findById(-1L);

        // then
        assertThat(orderLineItem).isEmpty();
    }

    @Test
    @DisplayName("모든 주문 항목을 조회한다")
    void findByAll() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableId(savedOrderTable.getId());
        final Order savedOrder = orderDao.save(order);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem(null, savedOrder.getId(), savedMenu.getId(), 2);
        final OrderLineItem saved = orderLineItemDao.save(orderLineItem);

        // when
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();

        // then
        assertAll(
                () -> assertThat(orderLineItems).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(orderLineItems).extracting("seq")
                        .contains(saved.getSeq())
        );
    }

    @Test
    @DisplayName("주문 id로 모든 주문 항목을 조회한다")
    void findByAllByOrderId() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableId(savedOrderTable.getId());
        final Order savedOrder = orderDao.save(order);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem(null, savedOrder.getId(), savedMenu.getId(), 2);
        final OrderLineItem saved = orderLineItemDao.save(orderLineItem);

        // when
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(savedOrder.getId());

        // then
        assertAll(
                () -> assertThat(orderLineItems).hasSize(1),
                () -> assertThat(orderLineItems).extracting("seq")
                        .containsExactly(saved.getSeq())
        );
    }
}
