package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ServiceTest
class TableServiceTest {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 추가한다.")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(true);

        OrderTable actual = tableService.create(orderTable);

        assertAll(
            () -> assertThat(actual).extracting(OrderTable::getId).isNotNull(),
            () -> assertThat(actual).extracting(OrderTable::getNumberOfGuests).isEqualTo(
                orderTable.getNumberOfGuests()),
            () -> assertThat(actual).extracting(OrderTable::isEmpty, BOOLEAN).isEqualTo(orderTable.isEmpty())
        );
    }

    @DisplayName("전체 테이블 목록을 조회한다.")
    @Test
    void list() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(true);

        orderTableDao.save(orderTable);

        List<OrderTable> actual = tableService.list();

        assertThat(actual).hasSize(1);
    }

    @DisplayName("테이블을 비우거나 채울 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(1);

        OrderTable savedTable = orderTableDao.save(orderTable);

        OrderTable updateInfo = new OrderTable();
        updateInfo.setEmpty(true);

        OrderTable actual = tableService.changeEmpty(savedTable.getId(), updateInfo);

        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 비우거나 채울 때 해당되는 테이블 번호가 없다면 예외 처리한다.")
    @Test
    void changeEmptyWithNotExistingTableId() {
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 비우거나 채울 때 테이블 그룹이 지정된 테이블이면 예외 처리한다.")
    @Test
    void changeEmptyWithTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(savedTableGroup.getId());

        OrderTable savedTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 비우거나 채울 때 테이블에 완료되지 않은 주문이 있는 경우 예외 처리한다.")
    @Test
    void changeEmptyWithNotComplementedOrder() {
        OrderTable orderTable = new OrderTable();

        OrderTable savedTable = orderTableDao.save(orderTable);

        Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(savedTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());

        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 현재 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);

        OrderTable updateInfo = new OrderTable();
        updateInfo.setNumberOfGuests(6);

        OrderTable savedTable = orderTableDao.save(orderTable);

        OrderTable actual = tableService.changeNumberOfGuests(savedTable.getId(), updateInfo);

        assertThat(actual).extracting(OrderTable::getNumberOfGuests).isEqualTo(updateInfo.getNumberOfGuests());
    }

    @DisplayName("테이블의 손님 수를 변경할 시 수정될 값이 음수일 경우 예외 처리한다.")
    @Test
    void changeNumberOfGuestsWithNegativeNumberOfGuests() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경할 시 존재하지 않는 테이블일 경우 예외 처리한다.")
    @Test
    void changeNumberOfGuestsWithNotExistingTableId() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경할 시 비어있는 테이블일 경우 예외 처리한다.")
    @Test
    void changeNumberOfGuestsWith() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(5);

        OrderTable updateInfo = new OrderTable();
        updateInfo.setNumberOfGuests(1);

        OrderTable savedTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), updateInfo))
            .isInstanceOf(IllegalArgumentException.class);
    }
}