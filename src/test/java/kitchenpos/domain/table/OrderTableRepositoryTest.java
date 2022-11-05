package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("테이블을 저장한다.")
    void save() {
        final OrderTable orderTable = OrderTable.create();
        final OrderTable savedTable = orderTableRepository.save(orderTable);

        assertAll(
                () -> assertThat(savedTable.getTableGroup()).isEqualTo(orderTable.getTableGroup()),
                () -> assertThat(savedTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests())
        );
    }

    @Test
    @DisplayName("id로 테이블을 찾는다.")
    void findById() {
        final OrderTable savedTable = orderTableRepository.save(OrderTable.create());

        final OrderTable findTable = orderTableRepository.findById(savedTable.getId())
                .orElseThrow();

        assertAll(
                () -> assertThat(findTable.getId()).isEqualTo(savedTable.getId()),
                () -> assertThat(findTable.getTableGroup()).isEqualTo(savedTable.getTableGroup()),
                () -> assertThat(findTable.getNumberOfGuests()).isEqualTo(savedTable.getNumberOfGuests())
        );
    }

    @Test
    @DisplayName("모든 테이블을 조회한다.")
    void findAll() {
        final OrderTable savedTable1 = orderTableRepository.save(OrderTable.create());
        final OrderTable savedTable2 = orderTableRepository.save(OrderTable.create());

        final List<OrderTable> tables = orderTableRepository.findAll();

        assertThat(tables).extracting("id").contains(savedTable1.getId(), savedTable2.getId());
    }

    @Test
    @DisplayName("id 목록에 들어있는 테이블 목록을 조회한다.")
    void findAllByIdIn() {
        final OrderTable savedTable1 = orderTableRepository.save(OrderTable.create());
        final OrderTable savedTable2 = orderTableRepository.save(OrderTable.create());

        final List<OrderTable> tables
                = orderTableRepository.findAllByIdIn(List.of(savedTable1.getId(), savedTable2.getId()));

        assertThat(tables).extracting("id").contains(savedTable1.getId(), savedTable2.getId());
    }

    @Test
    @DisplayName("테이블 그룹에 속한 테이블 목록을 찾는다.")
    void findAllByTableGroupId() {
        final OrderTable orderTable1 = OrderTable.create();
        final OrderTable orderTable2 = OrderTable.create();
        final TableGroup tableGroup = TableGroup.of(List.of(orderTable1, orderTable2));
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        orderTable1.joinTableGroup(savedTableGroup);
        orderTable2.joinTableGroup(savedTableGroup);
        final OrderTable savedTable1 = orderTableRepository.save(orderTable1);
        final OrderTable savedTable2 = orderTableRepository.save(orderTable2);

        final List<OrderTable> tables = orderTableRepository.findAllByTableGroupId(savedTableGroup.getId());

        assertThat(tables).extracting("id").contains(savedTable1.getId(), savedTable2.getId());
    }
}