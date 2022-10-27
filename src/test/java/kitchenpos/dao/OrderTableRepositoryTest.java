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
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("OrderTable을 저장한다.")
    void save() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 10, false));

        assertThat(orderTable).isEqualTo(orderTableRepository.findById(orderTable.getId()).orElseThrow());
    }

    @Test
    @DisplayName("모든 OrderTable을 조회한다.")
    void findAll() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(tableGroup.getId(), 10, false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(tableGroup.getId(), 5, false));

        List<OrderTable> orderTables = orderTableRepository.findAll();
        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
    }

    @Test
    @DisplayName("Id에 포함되는 모든 OrderTable을 조회한다.")
    void findAllByIdIn() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(tableGroup.getId(), 10, false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(tableGroup.getId(), 5, false));
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId()));

        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
    }

    @Test
    @DisplayName("TableGroup 내 모든 OrderTable을 조회한다.")
    void findAllByTableGroupId() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(tableGroup.getId(), 10, false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(tableGroup.getId(), 5, false));
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
    }
}
