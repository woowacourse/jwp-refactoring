package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        final Menu menu = new Menu();
        menu.setName("치킨치킨");
        menu.setPrice(new BigDecimal(3000));
        menu.setMenuGroupId(1L);

        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);

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
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        orderTableDao.save(orderTable);

        final Menu menu = new Menu();
        menu.setName("치킨치킨");
        menu.setPrice(new BigDecimal(3000));
        menu.setMenuGroupId(1L);

        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(-1L);
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);

        // when, then
        assertThatThrownBy(() -> orderLineItemDao.save(orderLineItem))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("존재하지 않은 메뉴로 주문 항목을 저장하면 예외가 발생한다")
    void saveExceptionNotExistMenu() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItem.setMenuId(-1L);
        orderLineItem.setQuantity(2);

        // when, then
        assertThatThrownBy(() -> orderLineItemDao.save(orderLineItem))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("id로 주문 항목을 조회한다")
    void findById() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        final Menu menu = new Menu();
        menu.setName("치킨치킨");
        menu.setPrice(new BigDecimal(3000));
        menu.setMenuGroupId(1L);

        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);

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
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        final Menu menu = new Menu();
        menu.setName("치킨치킨");
        menu.setPrice(new BigDecimal(3000));
        menu.setMenuGroupId(1L);

        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);

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
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        final Menu menu = new Menu();
        menu.setName("치킨치킨");
        menu.setPrice(new BigDecimal(3000));
        menu.setMenuGroupId(1L);

        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);

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
