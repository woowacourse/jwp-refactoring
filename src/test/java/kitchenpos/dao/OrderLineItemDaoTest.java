package kitchenpos.dao;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderLineItemDaoTest extends DaoTest {

    private OrderLineItemDao orderLineItemDao;
    private OrderDao orderDao;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
        orderDao = new JdbcTemplateOrderDao(dataSource);
    }

    @Test
    @DisplayName("OrderLineItem을 저장한다.")
    void save() {
        Menu menu = createMenu();
        Order order = createOrder();

        OrderLineItem orderLineItem = orderLineItemDao.save(new OrderLineItem(order.getId(), menu.getId(), 1));
        assertThat(orderLineItem).isEqualTo(orderLineItemDao.findById(orderLineItem.getSeq()).orElseThrow());
    }

    @Test
    @DisplayName("모든 OrderLineItem을 조회한다.")
    void findAll() {
        Menu menu = createMenu();
        Order order = createOrder();

        OrderLineItem orderLineItem1 = orderLineItemDao.save(new OrderLineItem(order.getId(), menu.getId(), 1));
        OrderLineItem orderLineItem2 = orderLineItemDao.save(new OrderLineItem(order.getId(), menu.getId(), 2));

        List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();
        assertThat(orderLineItems).contains(orderLineItem1, orderLineItem2);
    }

    @Test
    @DisplayName("Order에 포함된 모든 OrderLineItem을 조회한다.")
    void findAllByOrderId() {
        Menu menu = createMenu();
        Order order = createOrder();

        OrderLineItem orderLineItem1 = orderLineItemDao.save(new OrderLineItem(order.getId(), menu.getId(), 1));
        OrderLineItem orderLineItem2 = orderLineItemDao.save(new OrderLineItem(order.getId(), menu.getId(), 2));

        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());
        assertThat(orderLineItems).containsExactly(orderLineItem1, orderLineItem2);
    }

    private Menu createMenu() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
        return menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId()));
    }

    private Order createOrder() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(10, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));
        return orderDao.save(new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()));
    }
}
