package kitchenpos.repository;

import static kitchenpos.fixture.DomainFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.NotFoundOrderTableException;
import org.junit.jupiter.api.Test;

class TableGroupRepositoryTest extends RepositoryTest {

    @Test
    void 테이블그룹을_저장한다() {
        OrderTable saved1 = orderTableDao.save(createOrderTable(true));
        OrderTable saved2 = orderTableDao.save(createOrderTable(true));

        TableGroup savedTableGroup = tableGroupRepository.save(List.of(saved1.getId(), saved2.getId()));

        assertThat(tableGroupDao.findById(savedTableGroup.getId())).isPresent();
    }

    @Test
    void 테이블그룹을_저장할때_저장된_orderTables와_일치하지않으면_예외를_발생한다() {
        assertThatThrownBy(() -> tableGroupRepository.save(List.of(0L)))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    void 그룹을_해제한다() {
        OrderTable saved1 = orderTableDao.save(createOrderTable(true));
        OrderTable saved2 = orderTableDao.save(createOrderTable(true));

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(saved1, saved2));
        tableGroupDao.save(tableGroup);

        tableGroupRepository.ungroup(List.of(saved1, saved2));

        assertThat(orderTableDao.findById(saved1.getId()).get().getTableGroupId()).isNull();
    }
}
