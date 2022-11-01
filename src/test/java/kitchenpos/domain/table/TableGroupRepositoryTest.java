package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import kitchenpos.RepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class TableGroupRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 단체_지정을_저장_하면_id가_채워진다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));

        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @Test
    void id로_단체_지정을_조회할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        TableGroup actual = tableGroupRepository.findById(tableGroup.getId())
                .orElseGet(Assertions::fail);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(tableGroup);
    }

    @Test
    void 없는_id로_단체_지정을_조회하면_Optional_empty를_반환한다() {
        Optional<TableGroup> actual = tableGroupRepository.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_단체_지정을_조회할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        List<TableGroup> actual = tableGroupRepository.findAll();

        assertThat(actual).hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactly(tableGroup);
    }
}
