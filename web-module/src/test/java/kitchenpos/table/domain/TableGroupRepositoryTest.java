package kitchenpos.table.domain;

import kitchenpos.config.RepositoryTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableGroupRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("[SUCCESS] 단체 지정 식별자값으로 단체 지정을 조회한다.")
    @Test
    void success_findTableGroupByTableGroupId() {
        // given
        final OrderTables orderTables = new OrderTables(List.of(
                new OrderTable(null, 10, true),
                new OrderTable(null, 10, true)
        ));

        final TableGroup tableGroup = new TableGroup(orderTables);
        final TableGroup expected = persistTableGroup(tableGroup);

        // when
        final TableGroup actual = tableGroupRepository.findTableGroupByTableGroupId(expected.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isPositive();
            softly.assertThat(actual.getOrderTables()).isEqualTo(orderTables);
        });
    }
}
