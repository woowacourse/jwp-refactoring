package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderTableRepositoryTest {

    private TableGroupDao tableGroupDao;
    private OrderTableRepository orderTableRepository;

    @Autowired
    public OrderTableRepositoryTest(TableGroupDao tableGroupDao, OrderTableRepository orderTableRepository) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableRepository = orderTableRepository;
    }

    private Long tableGroupId;

    @BeforeEach
    void setUp() {
        tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null)).getId();
    }

    @Test
    void save() {
        // given
        OrderTable orderTable = new OrderTable(tableGroupId, 4, false);
        // when
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        // then
        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        OrderTable orderTable = new OrderTable(tableGroupId, 4, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // when
        Optional<OrderTable> foundOrderTable = orderTableRepository.findById(savedOrderTable.getId());

        // then
        assertThat(foundOrderTable).isNotNull();
    }

    @Test
    void findAll() {
        // given
        orderTableRepository.save(new OrderTable(tableGroupId, 4, false));
        orderTableRepository.save(new OrderTable(tableGroupId, 2, false));

        // when
        List<OrderTable> orderTables = orderTableRepository.findAll();

        // then
        int defaultSize = 8;
        assertThat(orderTables).hasSize(2 + defaultSize);
    }

    @Test
    void findAllByIdIn() {
        // given
        OrderTable orderTableA = orderTableRepository.save(new OrderTable(tableGroupId, 4, false));
        OrderTable orderTableB = orderTableRepository.save(new OrderTable(tableGroupId, 2, false));

        // when
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(List.of(orderTableA.getId(), orderTableB.getId()));

        // then
        assertThat(orderTables).hasSize(2);
    }

    @Test
    void findAllByTableGroupId() {
        // given
        orderTableRepository.save(new OrderTable(tableGroupId, 4, false));
        orderTableRepository.save(new OrderTable(tableGroupId, 2, false));

        // when
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        // then
        assertThat(orderTables).hasSize(2);
    }
}
