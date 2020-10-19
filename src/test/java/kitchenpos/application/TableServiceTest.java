package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SpringBootTest
@Transactional
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("create: 테이블의 수용 인원, 빈 테이블 여부를 입력 받아, 새로운 테이블 entity를 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"true, 0", "false, 10"})
    void create(boolean isEmpty, int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        orderTable.setNumberOfGuests(numberOfGuests);

        final OrderTable createdTable = tableService.create(orderTable);
        assertAll(
                () -> assertThat(createdTable.getId()).isNotNull(),
                () -> assertThat(createdTable.getNumberOfGuests()).isEqualTo(numberOfGuests),
                () -> assertThat(createdTable.getTableGroupId()).isNull(),
                () -> assertThat(createdTable.isEmpty()).isEqualTo(isEmpty)
        );
    }

    @DisplayName("list: 현재 저장 되어 있는 테이블의 전체 목록을 반환한다.")
    @Test
    void list() {
        final List<OrderTable> tables = tableService.list();

        assertThat(tables).hasSize(8);
    }

    @DisplayName("changeEmpty: groupId가 없고, 현재 테이블의 모든 주문이 완료 상태라면, 테이블의 비어있는 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(10);
        orderTable.setTableGroupId(null);
        final OrderTable savedTable = orderTableDao.save(orderTable);


        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);


        final Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.of(2020, 8, 20, 20, 20));
        orderDao.save(order);

        final OrderTable emptyTable = new OrderTable();
        emptyTable.setEmpty(true);
        final Long savedTableId = savedTable.getId();

        final OrderTable updatedOrderTable = tableService.changeEmpty(savedTableId, emptyTable);
        assertAll(
                () -> assertThat(updatedOrderTable.getId()).isNotNull(),
                () -> assertThat(updatedOrderTable.isEmpty()).isEqualTo(true),
                () -> assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(10),
                () -> assertThat(updatedOrderTable.getTableGroupId()).isNull()
        );
    }
}