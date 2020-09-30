package kitchenpos.dao;

import static kitchenpos.constants.DaoConstants.TEST_ORDER_TABLE_EMPTY;
import static kitchenpos.constants.DaoConstants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableDaoTest extends KitchenPosDaoTest {

    @DisplayName("OrderTable 저장 - 성공")
    @Test
    void save_Success() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests())
            .isEqualTo(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        assertThat(savedOrderTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY);
        assertThat(savedOrderTable.getTableGroupId()).isNull();
    }

    @DisplayName("OrderTable ID로 OrderTable 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderTable foundOrderTable = orderTableDao.findById(savedOrderTable.getId())
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 orderTable이 없습니다."));

        assertThat(foundOrderTable.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(foundOrderTable.getTableGroupId()).isEqualTo(savedOrderTable.getTableGroupId());
        assertThat(foundOrderTable.getNumberOfGuests())
            .isEqualTo(savedOrderTable.getNumberOfGuests());
        assertThat(foundOrderTable.isEmpty()).isEqualTo(savedOrderTable.isEmpty());
    }

    @DisplayName("OrderTable ID로 OrderTable 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Optional<OrderTable> foundOrderTable = orderTableDao.findById(savedOrderTable.getId() + 1);
        assertThat(foundOrderTable.isPresent()).isFalse();
    }

    @DisplayName("전체 OrderTable 조회 - 성공")
    @Test
    void findAll_Success() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        List<OrderTable> orderTables = orderTableDao.findAll();
        assertThat(orderTables).isNotNull();
        assertThat(orderTables).isNotEmpty();

        List<Long> orderTableIds = getIds(orderTables);
        assertThat(orderTableIds).contains(savedOrderTable.getId());
    }

    @DisplayName("OrderTable ID들로 OrderTable들 조회 - 조회됨, OrderTable ID들이 존재하는 경우")
    @Test
    void findAllByIdIn_ExistsOrderTableIds_ReturnOrderTables() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderTable otherOrderTable = new OrderTable();
        otherOrderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        otherOrderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        OrderTable savedOtherOrderTable = orderTableDao.save(otherOrderTable);

        OrderTable anotherOrderTable = new OrderTable();
        anotherOrderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        anotherOrderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        orderTableDao.save(anotherOrderTable);

        List<Long> ids = Arrays.asList(savedOrderTable.getId(), savedOtherOrderTable.getId());

        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(ids);
        assertThat(orderTables).hasSize(ids.size());

        List<Long> orderTableIds = getIds(orderTables);
        assertThat(orderTableIds).containsAll(orderTableIds);
    }

    @DisplayName("OrderTable ID들로 OrderTable들 조회 - 조회되지 않음, OrderTable ID들이 존재하지 않는 경우")
    @Test
    void findAllByIdIn_NotExistsOrderTableIds_ReturnEmpty() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        List<Long> ids = Arrays.asList(savedOrderTable.getId() + 1, savedOrderTable.getId() + 2);

        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(ids);
        assertThat(orderTables).isEmpty();
    }

    @DisplayName("TableGroup ID로 OrderTable들 조회 - 조회됨, TableGroup ID에 매치되는 경우")
    @Test
    void findAllByTableGroupId_MatchedTableGroupId_ReturnOrderTable() {
        Long tableGroupId = getCreatedTableGroupId();

        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        orderTable.setTableGroupId(tableGroupId);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderTable otherOrderTable = new OrderTable();
        otherOrderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        otherOrderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        otherOrderTable.setTableGroupId(tableGroupId);
        OrderTable savedOtherOrderTable = orderTableDao.save(otherOrderTable);

        OrderTable anotherOrderTable = new OrderTable();
        anotherOrderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        anotherOrderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        orderTableDao.save(anotherOrderTable);

        List<Long> ids = Arrays.asList(savedOrderTable.getId(), savedOtherOrderTable.getId());

        List<OrderTable> foundOrderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        assertThat(foundOrderTables).hasSize(ids.size());

        List<Long> foundOrderTableIds = getIds(foundOrderTables);
        assertThat(foundOrderTableIds).containsAll(ids);
    }

    @DisplayName("TableGroup ID로 OrderTable들 조회 - 조회되지 않음, TableGroup ID에 매치되지 않는 경우")
    @Test
    void findAllByTableGroupId_NotMatchedTableGroupId_ReturnOrderTable() {
        Long tableGroupId = getCreatedTableGroupId();

        List<OrderTable> foundOrderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        assertThat(foundOrderTables).isEmpty();
    }

    private List<Long> getIds(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }
}
