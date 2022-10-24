package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
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
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuService menuService;

    private Menu menu;

    private OrderTable dummyTable1;
    private OrderTable dummyTable2;

    @BeforeEach
    void setUp() {
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        dummyTable1 = tableService.create(orderTable1);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(false);
        dummyTable2 = tableService.create(orderTable2);
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
    @DisplayName("주문 테이블을 생성한다.")
    void create() {
        final OrderTable savedOrderTable1 = tableService.create(new OrderTable());
        final OrderTable savedOrderTable2 = tableService.create(new OrderTable());
        final OrderTable savedOrderTable3 = tableService.create(new OrderTable());

        final List<OrderTable> orderTables = tableService.list();

        assertAll(
                () -> assertThat(orderTables).usingElementComparatorIgnoringFields()
                        .contains(savedOrderTable1, savedOrderTable2, savedOrderTable3),
                () -> assertThat(savedOrderTable1.getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("테이블 그룹에 속한 테이블은 빈 테이블이 될 수 없다.")
    void changeEmptyWithTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(List.of(dummyTable1, dummyTable2));
        tableGroupService.create(tableGroup);

        assertThatThrownBy(() -> tableService.changeEmpty(dummyTable1.getId(), dummyTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조리 상태인 테이블은 빈 테이블이 될 수 없다.")
    void changeEmptyWithCookingStatus() {
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(dummyTable2.getId());
        order.setOrderedTime(LocalDateTime.now());
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(10);
        orderLineItem.setMenuId(menu.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        orderService.create(order);

        assertThatThrownBy(() -> tableService.changeEmpty(dummyTable2.getId(), dummyTable2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("식사 상태인 테이블은 빈 테이블이 될 수 없다.")
    void changeEmptyWithMealStatus() {
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(dummyTable2.getId());
        order.setOrderedTime(LocalDateTime.now());
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(10);
        orderLineItem.setMenuId(menu.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        final Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(OrderStatus.MEAL.name());

        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThatThrownBy(() -> tableService.changeEmpty(dummyTable2.getId(), dummyTable2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 손님의 수는 0보다 작을 수 없다.")
    void changeNumberOfGuestsUnder0() {
        dummyTable2.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(dummyTable2.getId(), dummyTable2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블의 손님 수를 변경할 수 없다.")
    void changeNumberOfGuestsEmptyTable() {
        dummyTable1.setNumberOfGuests(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(dummyTable1.getId(), dummyTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}