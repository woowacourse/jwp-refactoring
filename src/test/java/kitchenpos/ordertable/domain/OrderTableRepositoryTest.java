package kitchenpos.ordertable.domain;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(value = {"/h2-truncate.sql"})
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("ID 리스트를 받아서 해당 ID에 해당하는 OrderTable들을 반환한다.")
    void findAllByIdIn() {
        // given
        final TableGroup tableGroup = TableGroup.create();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        final OrderTable orderTable1 = ORDER_TABLE1();
        final OrderTable orderTable2 = ORDER_TABLE1();
        orderTable1.updateTableGroupId(savedTableGroup.getId());
        orderTable2.updateTableGroupId(savedTableGroup.getId());

        final OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

        final List<Long> ids = List.of(savedOrderTable1.getId(), savedOrderTable2.getId());
        final List<OrderTable> expectedOrderTables = List.of(savedOrderTable1, savedOrderTable2);

        // when
        final List<OrderTable> actual = orderTableRepository.findAllByIdIn(ids);

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .isEqualTo(expectedOrderTables);
    }

    @Test
    @DisplayName("TableGroupId에 해당하는 모든 OrderTable을 반환한다.")
    void findAllByTableGroupId() {
        // given
        final TableGroup tableGroup = TableGroup.create();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        OrderTable orderTable = ORDER_TABLE1();
        orderTable.updateTableGroupId(savedTableGroup.getId());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final List<OrderTable> expectedOrderTables = List.of(savedOrderTable);

        // when
        List<OrderTable> actual = orderTableRepository.findAllByTableGroupId(savedTableGroup.getId());

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedOrderTables);
    }
}
