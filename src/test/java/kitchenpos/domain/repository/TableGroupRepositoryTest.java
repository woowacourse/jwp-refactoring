package kitchenpos.domain.repository;

import static kitchenpos.support.TestFixtureFactory.단체_지정을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class TableGroupRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 단체_지정을_저장_하면_id가_채워진다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 0, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 0, true));

        TableGroup savedTableGroup = tableGroupRepository.save(
                new TableGroup(null, LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @Test
    void id로_단체_지정을_조회할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 0, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 0, true));
        TableGroup tableGroup = tableGroupRepository.save(
                단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

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
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 0, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 0, true));
        TableGroup tableGroup = tableGroupRepository.save(
                단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        List<TableGroup> actual = tableGroupRepository.findAll();

        assertThat(actual).hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactly(tableGroup);
    }
}
