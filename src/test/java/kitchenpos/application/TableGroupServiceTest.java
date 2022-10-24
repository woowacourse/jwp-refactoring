package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuService menuService;

    private Menu menu;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);

        this.orderTable1 = tableService.create(orderTable1);
        this.orderTable2 = tableService.create(orderTable2);

        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("테스트");
        final MenuGroup menuGroup1 = menuGroupDao.save(menuGroup);

        final Menu menu = new Menu();
        menu.setMenuGroupId(menuGroup1.getId());
        menu.setName("메뉴");
        menu.setPrice(new BigDecimal(0));
        menu.setMenuProducts(new ArrayList<>());
        this.menu = menuService.create(menu);
    }

    @Test
    @DisplayName("테이블은 그룹으로 묶일 수 있다.")
    void create() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());
        orderTable1.setEmpty(false);
        orderTable1.setTableGroupId(savedTableGroup.getId());
        orderTable2.setEmpty(false);
        orderTable2.setTableGroupId(savedTableGroup.getId());

        assertThat(orderTables).usingElementComparatorIgnoringFields()
                .contains(orderTable1, orderTable2);
    }

    @Test
    @DisplayName("테이블이 두개 이하인 테이블 그룹은 있을 수 없다.")
    void createUnder2() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(List.of(orderTable1));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조리중인 테이블이 있으면 그룹을 해제할 수 없다.")
    void ungroupWithCookingTable() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        orderTable1.setEmpty(false);
        orderTable1.setTableGroupId(savedTableGroup.getId());

        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTable1.getId());
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(10);
        orderLineItem.setMenuId(menu.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        final Order savedOrder = orderService.create(order);

        orderService.create(order);
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("밥먹는 테이블이 있으면 그룹을 해제할 수 없다.")
    void ungroupWithMealTable() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        orderTable1.setEmpty(false);
        orderTable1.setTableGroupId(savedTableGroup.getId());

        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTable1.getId());
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(10);
        orderLineItem.setMenuId(menu.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        final Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(OrderStatus.MEAL.name());
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        orderService.create(order);
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}