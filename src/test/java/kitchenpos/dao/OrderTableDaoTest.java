package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTableDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<OrderTable> orderTables = orderTableDao.findAll();

        assertAll(
            () -> assertThat(orderTables).hasSize(2),
            () -> assertThat(orderTables.get(0)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1),
            () -> assertThat(orderTables.get(1)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_2)
        );
    }

    @DisplayName("단건조회 예외 테스트: id에 해당하는 테이블이 존재하지 않을때")
    @Test
    void findByIdFailByNotExistTest() {
        Optional<OrderTable> orderTable = orderTableDao.findById(-1L);

        assertThat(orderTable).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        OrderTable orderTable = orderTableDao.findById(ORDER_TABLE_ID_1).get();

        assertThat(orderTable).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1);
    }

    @DisplayName("id목록으로 전체조회 테스트")
    @Test
    void findAllByIdInTest() {
        List<OrderTable> orderTables =
            orderTableDao.findAllByIdIn(Arrays.asList(ORDER_TABLE_ID_1));

        assertAll(
            () -> assertThat(orderTables).hasSize(1),
            () -> assertThat(orderTables.get(0)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1)
        );
    }

    @DisplayName("테이블 그룹id로 전체조회 테스트")
    @Test
    void findAllByTableGroupIdTest() {
        List<OrderTable> orderTables =
            orderTableDao.findAllByTableGroupId(TABLE_GROUP_ID);

        assertAll(
            () -> assertThat(orderTables).hasSize(2),
            () -> assertThat(orderTables.get(0)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1),
            () -> assertThat(orderTables.get(1)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_2)
        );
    }

    @DisplayName("기존에 존재하는 테이블 업데이트 테스트")
    @Test
    void updateTest() {
        OrderTable updatingOrderTable = new OrderTable();
        updatingOrderTable.setId(ORDER_TABLE_ID_1);
        updatingOrderTable.setTableGroupId(TABLE_GROUP_ID);
        updatingOrderTable.setNumberOfGuests(ORDER_TABLE_NUMBER_OF_GUESTS_2);
        updatingOrderTable.setEmpty(true);

        orderTableDao.save(updatingOrderTable);

        OrderTable updatedOrderTable = orderTableDao.findById(ORDER_TABLE_ID_1).get();
        assertAll(
            () -> assertThat(updatedOrderTable.getId()).isEqualTo(ORDER_TABLE_ID_1),
            () -> assertThat(updatedOrderTable.getTableGroupId()).isEqualTo(TABLE_GROUP_ID),
            () -> assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(ORDER_TABLE_NUMBER_OF_GUESTS_2),
            () -> assertThat(updatedOrderTable.isEmpty()).isEqualTo(true)
        );
    }
}