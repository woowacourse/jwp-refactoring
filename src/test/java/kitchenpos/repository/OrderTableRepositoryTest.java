package kitchenpos.repository;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTableRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void findAllByIdIn() {
        // given
        final TableGroup tableGroup = createTableGroup(LocalDateTime.now());
        final OrderTable orderTable1 = createOrderTable(tableGroup, 4, false);
        final OrderTable orderTable2 = createOrderTable(tableGroup, 5, false);

        em.flush();
        em.close();

        // when
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId()));

        // then
        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
    }

    @Test
    void findAllByTableGroupId() {
        // given
        final TableGroup tableGroup = createTableGroup(LocalDateTime.now());
        final OrderTable orderTable1 = createOrderTable(tableGroup, 4, false);
        final OrderTable orderTable2 = createOrderTable(tableGroup, 5, false);

        em.flush();
        em.close();

        // when
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        // then
        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
    }
}
