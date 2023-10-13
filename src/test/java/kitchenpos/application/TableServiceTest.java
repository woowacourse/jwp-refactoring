package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixtures.createEmptyTable;
import static kitchenpos.fixture.ProductFixtures.양념치킨_17000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.MenuFixtures;
import kitchenpos.fixture.MenuGroupFixtures;
import kitchenpos.fixture.MenuProductFixtures;
import kitchenpos.fixture.OrderFixtures;
import kitchenpos.fixture.OrderLineItemFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(scripts = {"/truncate.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TableServiceTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // given
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        tableService.create(orderTable1);
        tableService.create(orderTable2);

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).hasSize(2);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable());

        Menu menu = createMenu("양념1마리", 17_000);
        menuDao.save(menu);

        Order order = createOrder(orderTable, OrderStatus.COMPLETION, menu);
        orderDao.save(order);

        orderTable.setEmpty(true);

        // when
        OrderTable savedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        // then
        assertThat(savedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("단체 지정되어 있는 테이블을 비우려고 하면 예외가 발생한다.")
    @Test
    void changeEmpty_InTableGroup_ExceptionThrown() {
        // given
        OrderTable savedOrderTable1 = tableService.create(createEmptyTable());
        OrderTable savedOrderTable2 = tableService.create(createEmptyTable());

        TableGroup tableGroup = createTableGroup(savedOrderTable1, savedOrderTable2);
        tableGroupService.create(tableGroup);

        Menu menu = createMenu("양념1마리", 17_000);
        menuDao.save(menu);

        Order order = createOrder(savedOrderTable1, OrderStatus.COMPLETION, menu);
        orderDao.save(order);

        OrderTable changeOrderTable = new OrderTable();
        changeOrderTable.setEmpty(true);

        // when, then
        assertThatThrownBy(
                () -> tableService.changeEmpty(savedOrderTable1.getId(), changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리중이거나 식사중인 테이블을 비우려고 하면 예외가 발생한다.")
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
    @ParameterizedTest
    void changeEmpty_InlvaidOrderStatus_ExceptionThrown(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = tableService.create(new OrderTable());

        Menu menu = createMenu("양념1마리", 17_000);
        menuDao.save(menu);

        Order order = createOrder(orderTable, orderStatus, menu);
        orderDao.save(order);

        orderTable.setEmpty(true);
        // when, then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 방문 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        int expected = 5;
        OrderTable orderTable = tableService.create(new OrderTable());
        orderTable.setNumberOfGuests(expected);

        // when
        OrderTable savedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        // then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(expected);
    }

    @DisplayName("테이블 방문 손님 수를 0명 미만으로 변경하면 예외가 발생한다.")
    @ValueSource(ints = {-1, -3, -5, -10})
    @ParameterizedTest
    void changeNumberOfGuests_NegativeNumber_ExceptionThrown(int invalidNumberOfGuests) {
        // given
        OrderTable orderTable = tableService.create(new OrderTable());
        orderTable.setNumberOfGuests(invalidNumberOfGuests);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 수를 변경하면 에외가 발생한다.")
    @Test
    void changeNumberOfGuests_EmptyTable_ExceptionThrown() {
        // given
        OrderTable savedOrderTable = tableService.create(createEmptyTable());

        savedOrderTable.setNumberOfGuests(5);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Menu createMenu(String name, int price) {
        MenuProduct menuProduct = MenuProductFixtures.create(양념치킨_17000원, 1);
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixtures.한마리_메뉴);
        return MenuFixtures.create(
                name,
                price,
                menuGroup,
                List.of(menuProduct)
        );
    }

    private Order createOrder(OrderTable orderTable, OrderStatus orderStatus, Menu menu) {
        return OrderFixtures.create(
                orderTable.getId(),
                orderStatus.name(),
                LocalDateTime.now(),
                List.of(OrderLineItemFixtures.create(menu.getId(), 1))
        );
    }

    private TableGroup createTableGroup(
            OrderTable savedOrderTable1,
            OrderTable savedOrderTable2
    ) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}
