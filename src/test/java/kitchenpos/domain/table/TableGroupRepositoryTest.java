package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("테이블 그룹을 저장한다.")
    void save() {
        final LocalDateTime startTime = LocalDateTime.now();
        final TableGroup tableGroup = TableGroup.of(List.of(orderTable1, orderTable2));

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isBefore(LocalDateTime.now()),
                () -> assertThat(savedTableGroup.getCreatedDate()).isAfter(startTime),
                () -> assertThat(savedTableGroup.getOrderTables()).extracting("id")
                        .contains(orderTable1.getId(), orderTable2.getId())
        );
    }

    @Test
    @DisplayName("id로 테이블 그룹을 찾는다.")
    void findById() {
        final TableGroup tableGroup = TableGroup.of(List.of(orderTable1, orderTable2));
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        final TableGroup findTableGroup = tableGroupRepository.findById(savedTableGroup.getId())
                .orElseThrow();

        assertAll(
                () -> assertThat(findTableGroup.getId()).isEqualTo(savedTableGroup.getId()),
                () -> assertThat(findTableGroup.getCreatedDate()).isEqualTo(savedTableGroup.getCreatedDate()),
                () -> assertThat(findTableGroup.getOrderTables()).extracting("id")
                        .contains(orderTable1.getId(), orderTable2.getId())
        );
    }

    @Test
    @DisplayName("전체 테이블 그룹을 조회한다.")
    void findAll() {
        final TableGroup tableGroup1 = TableGroup.of(List.of(orderTable1, orderTable2));
        final OrderTable orderTable3 = orderTableRepository.save(OrderTable.create());
        final OrderTable orderTable4 = orderTableRepository.save(OrderTable.create());
        final TableGroup tableGroup2 = TableGroup.of(List.of(orderTable3, orderTable4));

        final TableGroup savedTableGroup1 = tableGroupRepository.save(tableGroup1);
        final TableGroup savedTableGroup2 = tableGroupRepository.save(tableGroup2);

        final List<TableGroup> tableGroups = tableGroupRepository.findAll();

        assertThat(tableGroups).extracting("id")
                .contains(savedTableGroup1.getId(), savedTableGroup2.getId());
    }
}