package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:truncate.sql")
@Transactional
@SpringBootTest
class TableGroupGeneratorTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("테이블 그룹을 생성할 때 주문 테이블이 비어있지 않은 경우 예외가 발생한다.")
    void throwsExceptionWhenOrderTableIsNotEmpty() {
        // given
        final TableGroupGenerator tableGroupGenerator = new TableGroupGenerator(orderTableRepository);
        final OrderTable orderTableA = new OrderTable(null, 2, false);
        final OrderTable orderTableB = new OrderTable(null, 3, false);
        orderTableRepository.save(orderTableA);
        orderTableRepository.save(orderTableB);

        final List<Long> orderTableIds = List.of(orderTableA.getId(), orderTableB.getId());

        // when, then
        assertThatThrownBy(() -> tableGroupGenerator.validate(orderTableIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 비어있어야 합니다.");
    }

    @Test
    @DisplayName("테이블 그룹ID를 설정한다.")
    void setOrderTableGroupId() {
        // given
        final TableGroupGenerator tableGroupGenerator = new TableGroupGenerator(orderTableRepository);
        final TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);

        final OrderTable orderTableA = new OrderTable(null, 2, false);
        final OrderTable orderTableB = new OrderTable(null, 3, false);
        orderTableRepository.save(orderTableA);
        orderTableRepository.save(orderTableB);

        final List<Long> orderTableIds = List.of(orderTableA.getId(), orderTableB.getId());

        // when
        tableGroupGenerator.setOrderTableGroupId(tableGroup.getId(), orderTableIds);

        // then
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        assertAll(
                () -> assertThat(orderTables.get(0).getTableGroupId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(orderTables.get(1).getTableGroupId()).isEqualTo(tableGroup.getId())
        );
    }
}
