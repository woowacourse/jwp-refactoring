package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Transactional
class TableServiceTest {

    private final OrderTable orderTable = new OrderTable(1L, 1, false);

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("존재하지 않는 테이블을 빈 테이블로 수정하려고 하면 예외를 발생시킨다.")
    void changeEmptyNotExistTableError() {
        assertThatThrownBy(() -> tableService.changeEmpty(9999L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Group으로 묶인 테이블의 경우 빈 테이블로 수정하려고 하면 예외를 발생시킨다.")
    void changeEmptyWithGroupTableError() {
        //given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable(1L, 1L, 1, false);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 1, false);

        tableGroup.setOrderTables(convertToList(orderTable1, orderTable2));

        tableGroupService.create(tableGroup);

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable2))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 상태가 COOKING 또는 MEAL일 때 빈 테이블로 변경하려고 할 경우 예외를 발생시킨다.")
    void changeEmptyInvalidStatusError() {
        //given
        OrderTable newOrderTable =         new OrderTable(1L, 1, false);
        Long notEmptyOrderTableId = tableService.create(newOrderTable).getId();

        //when
        Order order = new Order(notEmptyOrderTableId, "COOKING", LocalDateTime.now(),
            generateOrderLineItemAsList(1L, 1));
        orderService.create(order);

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(notEmptyOrderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객의 수를 수정한다.")
    void changeNumberOfGuests() {
        //given
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(false);
        Long savedOrderTableId = tableService.create(orderTable).getId();

        //when
        orderTable.setNumberOfGuests(5);
        OrderTable actual = tableService.changeNumberOfGuests(savedOrderTableId, orderTable);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(savedOrderTableId),
            () -> assertThat(actual.getNumberOfGuests()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("변경하려는 고객의 수가 음수일 경우 예외를 발생시킨다.")
    void changeNumberOfGuestsNegativeNumberError() {
        //given
        orderTable.setNumberOfGuests(-1);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블의 고객 수를 수정하려고 할 경우 예외를 발생시킨다.")
    void changeNumberOfGuestsNotExistTableError() {
        //given
        orderTable.setNumberOfGuests(10);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(999999L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 테이블의 고객 수를 수정하려고 할 경우 예외를 발생시킨다.")
    void changeNumberOfGuestsEmptyTableError() {
        //given
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(true);

        OrderTable savedOrderTable = orderTableDao.findById(5L).get();

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private <T> List<T> convertToList(T... values) {
        return Arrays.stream(values)
            .collect(Collectors.toList());
    }

    private ArrayList<OrderLineItem> generateOrderLineItemAsList(Long menuId, int quantity) {
        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();

        OrderLineItem orderLineItem = new OrderLineItem(1L, menuId, quantity);

        orderLineItems.add(orderLineItem);
        return orderLineItems;
    }
}
