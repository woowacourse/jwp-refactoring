package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Price;
import kitchenpos.fixture.MenuGroupFixtures;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.request.OrderTableCreateRequest;
import kitchenpos.request.TableChangeEmptyRequest;
import kitchenpos.request.TableChangeNumberOfGuestsRequest;
import kitchenpos.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.application.TableGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixture.ProductFixtures.양념치킨_17000원;
import static kitchenpos.fixture.TableGroupFixtures.getTableGroupCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableServiceTest extends ServiceTest {

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
        OrderTableCreateRequest request = new OrderTableCreateRequest(5, false);

        // when
        OrderTable savedOrderTable = tableService.create(request);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(5, false);

        tableService.create(request);
        tableService.create(request);

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).hasSize(2);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(5, false);
        OrderTable orderTable = tableService.create(request);

        Menu menu = createMenu("양념1마리", 17_000);
        menuDao.save(menu);

        Order order = new Order(
                null,
                orderTable.getId(),
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                List.of(new OrderLineItem(menu.getId(), 1))
        );
        orderDao.save(order);

        // when
        OrderTable savedOrderTable = tableService.changeEmpty(orderTable.getId(), new TableChangeEmptyRequest(true));

        // then
        assertThat(savedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("단체 지정되어 있는 테이블을 비우려고 하면 예외가 발생한다.")
    @Test
    void changeEmpty_InTableGroup_ExceptionThrown() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(5, true);
        OrderTable orderTable1 = tableService.create(orderTableCreateRequest);
        OrderTable orderTable2 = tableService.create(orderTableCreateRequest);
        TableGroupCreateRequest tableGroupCreateRequest = getTableGroupCreateRequest(List.of(orderTable1, orderTable2));

        tableGroupService.create(tableGroupCreateRequest);

        Menu menu = createMenu("양념1마리", 17_000);
        menuDao.save(menu);
        Order order = new Order(
                null,
                orderTable1.getId(),
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                List.of(new OrderLineItem(menu.getId(), 1))
        );
        orderDao.save(order);

        // when, then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable1.getId(), new TableChangeEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리중이거나 식사중인 테이블을 비우려고 하면 예외가 발생한다.")
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
    @ParameterizedTest
    void changeEmpty_InlvaidOrderStatus_ExceptionThrown(OrderStatus orderStatus) {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(5, false);
        OrderTable orderTable = tableService.create(request);

        Menu menu = createMenu("양념1마리", 17_000);
        menuDao.save(menu);
        Order order = new Order(
                null,
                orderTable.getId(),
                orderStatus.name(),
                LocalDateTime.now(),
                List.of(new OrderLineItem(menu.getId(), 1))
        );
        orderDao.save(order);

        // when, then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), new TableChangeEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 방문 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(3, false);
        OrderTable orderTable = tableService.create(request);
        int expected = 5;
        TableChangeNumberOfGuestsRequest changeNumberRequest = new TableChangeNumberOfGuestsRequest(expected);

        // when
        OrderTable savedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), changeNumberRequest);

        // then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(expected);
    }

    @DisplayName("테이블 방문 손님 수를 0명 미만으로 변경하면 예외가 발생한다.")
    @ValueSource(ints = {-1, -3, -5, -10})
    @ParameterizedTest
    void changeNumberOfGuests_NegativeNumber_ExceptionThrown(int invalidNumberOfGuests) {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(5, false);
        OrderTable orderTable = tableService.create(request);
        TableChangeNumberOfGuestsRequest changeNumberRequest = new TableChangeNumberOfGuestsRequest(invalidNumberOfGuests);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeNumberRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_EmptyTable_ExceptionThrown() {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(5, true);
        OrderTable savedOrderTable = tableService.create(request);
        TableChangeNumberOfGuestsRequest changeNumberRequest = new TableChangeNumberOfGuestsRequest(8);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), changeNumberRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Menu createMenu(String name, int price) {
        MenuProduct menuProduct = new MenuProduct(양념치킨_17000원.getId(), 1);
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixtures.한마리_메뉴);
        return new Menu(null, name, new Price(BigDecimal.valueOf(price)), menuGroup.getId(), List.of(menuProduct));
    }
}
