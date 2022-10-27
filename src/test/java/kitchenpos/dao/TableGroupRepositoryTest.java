package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TableGroupRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("TableGroup을 저장한다.")
    void save() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5, true));
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        assertThat(tableGroup).isEqualTo(tableGroupRepository.findById(tableGroup.getId()).orElseThrow());
    }

    @Test
    @DisplayName("모든 TableGroup을 조회한다.")
    void findAll() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5, true));
        TableGroup tableGroup1 = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        List<TableGroup> tableGroups = tableGroupRepository.findAll();
        assertThat(tableGroups).containsExactly(tableGroup1);
    }
}
