package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Table;

class TableDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<Table> tables = tableDao.findAll();

        assertAll(
            () -> assertThat(tables).hasSize(2),
            () -> assertThat(tables.get(0)).usingRecursiveComparison().isEqualTo(TABLE_1),
            () -> assertThat(tables.get(1)).usingRecursiveComparison().isEqualTo(TABLE_2)
        );
    }

    @DisplayName("단건조회 예외 테스트: id에 해당하는 테이블이 존재하지 않을때")
    @Test
    void findByIdFailByNotExistTest() {
        Optional<Table> table = tableDao.findById(-1L);

        assertThat(table).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        Table table = tableDao.findById(TABLE_ID_1).get();

        assertThat(table).usingRecursiveComparison().isEqualTo(TABLE_1);
    }

    @DisplayName("id목록으로 전체조회 테스트")
    @Test
    void findAllByIdInTest() {
        List<Table> tables =
            tableDao.findAllByIdIn(Arrays.asList(TABLE_ID_1));

        assertAll(
            () -> assertThat(tables).hasSize(1),
            () -> assertThat(tables.get(0)).usingRecursiveComparison().isEqualTo(TABLE_1)
        );
    }

    @DisplayName("테이블 그룹id로 전체조회 테스트")
    @Test
    void findAllByTableGroupIdTest() {
        List<Table> tables =
            tableDao.findAllByTableGroupId(TABLE_GROUP_ID);

        assertAll(
            () -> assertThat(tables).hasSize(2),
            () -> assertThat(tables.get(0)).usingRecursiveComparison().isEqualTo(TABLE_1),
            () -> assertThat(tables.get(1)).usingRecursiveComparison().isEqualTo(TABLE_2)
        );
    }

    @DisplayName("기존에 존재하는 테이블 업데이트 테스트")
    @Test
    void updateTest() {
        Table updatingTable = new Table(TABLE_ID_1, TABLE_GROUP_ID, TABLE_NUMBER_OF_GUESTS_2, true);

        tableDao.save(updatingTable);

        Table updatedTable = tableDao.findById(TABLE_ID_1).get();
        assertAll(
            () -> assertThat(updatedTable.getId()).isEqualTo(TABLE_ID_1),
            () -> assertThat(updatedTable.getTableGroupId()).isEqualTo(TABLE_GROUP_ID),
            () -> assertThat(updatedTable.getNumberOfGuests()).isEqualTo(TABLE_NUMBER_OF_GUESTS_2),
            () -> assertThat(updatedTable.isEmpty()).isEqualTo(true)
        );
    }
}