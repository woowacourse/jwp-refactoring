package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderTableRepositoryTest {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    private Long tableGroupId = 1L;
    private int numberOfGuests = 3;
    private boolean empty = true;

    @Autowired
    public OrderTableRepositoryTest(final OrderTableRepository orderTableRepository,
                                    final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @BeforeEach
    void setup() {
        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now());
        tableGroupId = tableGroupRepository.save(tableGroup).getId();
    }

    @Test
    void 저장한다() {
        // given
        OrderTable orderTable = order_table을_생성한다(numberOfGuests, empty);

        // when
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // then
        Assertions.assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable).usingRecursiveComparison()
                        .ignoringFields("id", "tableGroupId")
                        .isEqualTo(new OrderTable(3, true))
        );
    }

    private OrderTable order_table을_생성한다(final int numberOfGuests, final boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    @Test
    void 이미_ID가_있으면_저장시_update를_진행한다() {
        // given
        OrderTable orderTable = order_table을_생성한다(numberOfGuests, empty);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        OrderTable updatedOrderTable = new OrderTable(savedOrderTable.getId(), tableGroupId, 5, false);

        // when
        OrderTable actual = orderTableRepository.save(updatedOrderTable);

        // then
        Assertions.assertAll(
                () -> assertThat(actual.getId()).isEqualTo(actual.getId()),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(5),
                () -> assertThat(actual.isEmpty()).isEqualTo(false)
        );
    }

    @Test
    void ID로_조회한다() {
        // given
        OrderTable orderTable = order_table을_생성한다(numberOfGuests, empty);
        orderTableRepository.save(orderTable);

        // when
        Optional<OrderTable> foundOrderTable = orderTableRepository.findById(orderTable.getId());

        // then
        Assertions.assertAll(
                () -> assertThat(foundOrderTable).isPresent(),
                () -> assertThat(foundOrderTable.get())
                        .extracting("numberOfGuests", "empty")
                        .contains(3, true)
        );
    }

    @Test
    void 일치하는_ID가_없는_경우_empty를_반환한다() {
        // given
        OrderTable orderTable = order_table을_생성한다(numberOfGuests, empty);
        orderTableRepository.save(orderTable);
        Long notExistId = -1L;

        // when
        Optional<OrderTable> foundOrderTable = orderTableRepository.findById(notExistId);

        // then
        assertThat(foundOrderTable).isEmpty();
    }

    @Test
    void 목록을_조회한다() {
        // given & when
        List<OrderTable> orderTables = orderTableRepository.findAll();

        // then
        assertThat(orderTables).hasSize(8)
                .usingRecursiveComparison()
                .ignoringFields("tableGroupId")
                .isEqualTo(
                        Arrays.asList(
                                new OrderTable(1L, 1L, 0, true),
                                new OrderTable(2L, 1L, 0, true),
                                new OrderTable(3L, 1L, 0, true),
                                new OrderTable(4L, 1L, 0, true),
                                new OrderTable(5L, 1L, 0, true),
                                new OrderTable(6L, 1L, 0, true),
                                new OrderTable(7L, 1L, 0, true),
                                new OrderTable(8L, 1L, 0, true)
                        )
                );
    }

    @Test
    void 포함된_ID를_조회할_수_있다() {
        // given
        List<Long> ids = Arrays.asList(1L, 8L, 9L);

        // when
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(ids);

        // then
        assertThat(orderTables).hasSize(2)
                .usingRecursiveComparison()
                .ignoringFields("tableGroupId")
                .isEqualTo(
                        Arrays.asList(
                                new OrderTable(1L, 1L, 0, true),
                                new OrderTable(8L, 1L, 0, true)
                        )
                );
    }

    @Test
    void table_group_id로_조회할_수_있다() {
        // given
        OrderTable orderTable = order_table을_생성한다(10, true);
        orderTable.fillOrderTableGroup(tableGroupId);
        orderTableRepository.save(orderTable);

        // when
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        // then
        assertThat(orderTables).hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("id", "tableGroupId")
                .isEqualTo(Arrays.asList(new OrderTable(10, false)));
    }

    @Test
    void order_table_id들이_포함된_order를_조회한다() {
        List<OrderTable> orderTables = orderTableRepository.findAllByOrderTableIdsIn(Arrays.asList(1L, 2L, 9L));

        // then
        assertThat(orderTables).hasSize(2);
    }

    @Test
    void 모두_save_한다() {
        // given
        OrderTable orderTable = order_table을_생성한다(numberOfGuests, empty);
        OrderTable orderTable2 = order_table을_생성한다(7, empty);

        // when
        orderTableRepository.saveAll(Arrays.asList(orderTable, orderTable2));
        List<OrderTable> orderTables = orderTableRepository.findAll();
        // then
        assertThat(orderTables).hasSize(10);
    }
}
