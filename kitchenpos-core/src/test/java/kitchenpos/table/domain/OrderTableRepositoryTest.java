package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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
        OrderTable orderTable = orderTableRepository.save(new OrderTable(10, true));

        assertThat(orderTable).isEqualTo(orderTableRepository.findById(orderTable.getId()).orElseThrow());
    }

    @Test
    @DisplayName("모든 OrderTable을 조회한다.")
    void findAll() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5, true));

        List<OrderTable> orderTables = orderTableRepository.findAll();
        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
    }

    @Test
    @DisplayName("Id에 포함되는 모든 OrderTable을 조회한다.")
    void findAllByIdIn() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5, true));
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(
                List.of(orderTable1.getId(), orderTable2.getId()));

        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
    }
}
