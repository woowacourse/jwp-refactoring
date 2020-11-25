package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<TableGroup> tableGroups = tableGroupDao.findAll();

        assertAll(
            () -> assertThat(tableGroups).hasSize(1),
            () -> assertThat(tableGroups.get(0)).usingRecursiveComparison().isEqualTo(TABLE_GROUP)
        );
    }

    @DisplayName("단건조회 예외 테스트: id에 해당하는 주문이 존재하지 않을때")
    @Test
    void findByIdFailByNotExistTest() {
        Optional<TableGroup> tableGroup = tableGroupDao.findById(-1L);

        assertThat(tableGroup).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        TableGroup tableGroup = tableGroupDao.findById(TABLE_GROUP_ID).get();

        assertAll(
            () -> assertThat(tableGroup).usingRecursiveComparison().isEqualTo(TABLE_GROUP)
        );
    }
}