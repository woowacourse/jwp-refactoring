package kitchenpos.repository;

import static kitchenpos.OrderTableFixtures.createOrderTable;
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

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    @Autowired
    public OrderTableRepositoryTest(
            TableGroupRepository tableGroupRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        OrderTable orderTableA = orderTableRepository.save(createOrderTable());
        OrderTable orderTableB = orderTableRepository.save(createOrderTable());
        List<Long> orderTableIds = List.of(orderTableA.getId(), orderTableB.getId());
        this.tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), orderTableIds));
        orderTableA.group(tableGroup.getId());
        orderTableB.group(tableGroup.getId());
    }

    @Test
    void save() {
        // given
        OrderTable orderTable = createOrderTable();
        // when
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        // then
        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTable());

        // when
        Optional<OrderTable> foundOrderTable = orderTableRepository.findById(savedOrderTable.getId());

        // then
        assertThat(foundOrderTable).isNotNull();
    }

    @Test
    void findAll() {
        // given & when
        List<OrderTable> orderTables = orderTableRepository.findAll();

        // then
        int defaultSize = 8;
        assertThat(orderTables).hasSize(2 + defaultSize);
    }

    @Test
    void findAllByIdIn() {
        // given
        OrderTable orderTableA = orderTableRepository.save(new OrderTable(tableGroup.getId(), 4, false));
        OrderTable orderTableB = orderTableRepository.save(new OrderTable(tableGroup.getId(), 2, false));

        // when
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(
                List.of(orderTableA.getId(), orderTableB.getId()));

        // then
        assertThat(orderTables).hasSize(2);
    }

    @Test
    void findAllByTableGroupId() {
        // given & when
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        // then
        assertThat(orderTables).hasSize(2);
    }
}
