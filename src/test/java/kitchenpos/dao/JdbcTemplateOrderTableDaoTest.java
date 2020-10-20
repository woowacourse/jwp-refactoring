package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JdbcTemplateOrderTableDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAll();

        assertAll(
            () -> assertThat(orderTables).hasSize(2),
            () -> assertThat(orderTables.get(0)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1),
            () -> assertThat(orderTables.get(1)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_2)
        );
    }

    @DisplayName("단건조회 예외 테스트: id에 해당하는 테이블이 존재하지 않을때")
    @Test
    void findByIdFailByNotExistTest() {
        Optional<OrderTable> orderTable = jdbcTemplateOrderTableDao.findById(-1L);

        assertThat(orderTable).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        OrderTable orderTable = jdbcTemplateOrderTableDao.findById(ORDER_TABLE_ID_1).get();

        assertThat(orderTable).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1);
    }

    @DisplayName("id목록으로 전체조회 테스트")
    @Test
    void findAllByIdInTest() {
        List<OrderTable> orderTables =
            jdbcTemplateOrderTableDao.findAllByIdIn(Arrays.asList(ORDER_TABLE_ID_1));

        assertAll(
            () -> assertThat(orderTables).hasSize(1),
            () -> assertThat(orderTables.get(0)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1)
        );
    }

    @DisplayName("테이블 그룹id로 전체조회 테스트")
    @Test
    void findAllByTableGroupIdTest() {
        List<OrderTable> orderTables =
            jdbcTemplateOrderTableDao.findAllByTableGroupId(TABLE_GROUP_ID);

        assertAll(
            () -> assertThat(orderTables).hasSize(2),
            () -> assertThat(orderTables.get(0)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1),
            () -> assertThat(orderTables.get(1)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_2)
        );
    }
}