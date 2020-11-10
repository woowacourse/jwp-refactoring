package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

class TableServiceTest extends IntegrationTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @DisplayName("주문 테이블 생성")
    @Test
    void createOrderTableValidInput() {
        OrderTable orderTableRequest = new OrderTable();
        orderTableRequest.setNumberOfGuests(0);
        orderTableRequest.setEmpty(true);

        OrderTable savedOrderTable = tableService.create(createOrderTable(null, null, 0, true));

        assertAll(
            () -> assertThat(savedOrderTable.getId()).isNotNull(),
            () -> assertThat(savedOrderTable.getTableGroupId()).isNull(),
            () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests()),
            () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTableRequest.isEmpty())
        );
    }

    @DisplayName("주문 테이블을 빈 테이블로 설정 또는 해지한다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean empty) {
        OrderTable savedOrderTable = tableService.create(createOrderTable(null, null, 0, true));

        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(empty);

        OrderTable emptyResult = tableService.changeEmpty(savedOrderTable.getId(), emptyRequest);

        assertThat(emptyResult.isEmpty()).isEqualTo(empty);
    }

    @DisplayName("존재하지 않는 테이블의 빈 테이블 설정 요청시 예외 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void changeEmptyException(String value) {
        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(true);

        assertThatThrownBy(() -> tableService.changeEmpty(Objects.isNull(value) ? null : 1L, emptyRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹화 된 테이블의 빈 테이블 설정 요청시 예외 발생")
    @Test
    void changeEmptyTableGroupException() {
        OrderTable savedOrderTable1 = tableService.create(createOrderTable(null, null, 0, true));
        OrderTable savedOrderTable2 = tableService.create(createOrderTable(null, null, 0, true));

        tableGroupService.create(createTableGroup(null, null, Arrays.asList(savedOrderTable1, savedOrderTable2)));

        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(false);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), emptyRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 주문 목록 중 '조리' 또는 '식사 중'인 주문이 있을 경우 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmptyOrderStatusException(String status) {
        BigDecimal price = BigDecimal.valueOf(10000);
        OrderTable savedOrderTable = tableService.create(createOrderTable(null, null, 0, true));
        MenuGroup menuGroup = menuGroupService.create(createMenuGroup(null, "한마리 치킨"));
        Product product = productService.create(createProduct(null, "후라이드 치킨", price));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuService.create(
            createMenu(null, "후라이드 치킨", price, menuGroup.getId(), Arrays.asList(menuProduct)));
        tableService.changeEmpty(savedOrderTable.getId(), createOrderTable(null, null, 0, false));
        OrderLineItem orderLineItem = createOrderLineItem(null, null, menu.getId(), 1);
        Order order = orderService.create(
            createOrder(null, savedOrderTable.getId(), null, null, Arrays.asList(orderLineItem)));

        orderService.changeOrderStatus(order.getId(), createOrder(null, null, status, null, null));

        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), emptyRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님의 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedOrderTable = tableService.create(createOrderTable(1L, null, 0, false));

        OrderTable changeGuestsRequest = createOrderTable(null, null, 3, true);

        OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), changeGuestsRequest);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(changeGuestsRequest.getNumberOfGuests());
    }

    @DisplayName("변경할 손님의 수가 음수일 시 예외 발생")
    @Test
    void changeNumberOfGuestsByNegativeNumber() {
        OrderTable changeGuestsRequest = createOrderTable(null, null, -1, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeGuestsRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("변경할 테이블이 비어있을시 예외 발생")
    @Test
    void changeNumberOfGuestsOnEmptyTable() {
        OrderTable savedOrderTable = tableService.create(createOrderTable(1L, null, 0, true));

        OrderTable changeGuestsRequest = createOrderTable(null, null, 1, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), changeGuestsRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블을 조회할 수 있다.")
    @Test
    void findAllOrderTables() {
        OrderTable orderTable1 = tableService.create(createOrderTable(1L, null, 0, true));
        OrderTable orderTable2 = tableService.create(createOrderTable(2L, null, 0, true));

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).size().isEqualTo(2);
    }
}
