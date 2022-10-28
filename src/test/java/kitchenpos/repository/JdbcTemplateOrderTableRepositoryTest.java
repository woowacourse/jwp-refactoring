package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcTemplateOrderTableRepositoryTest {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    private Long tableGroupId = 1L;
    private int numberOfGuests = 3;
    private boolean empty = true;

    @Autowired
    public JdbcTemplateOrderTableRepositoryTest(final OrderTableRepository orderTableRepository,
                                                final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Test
    void 저장한다() {
        // given
        OrderTable orderTable = order_table을_생성한다(tableGroupId, numberOfGuests, empty);

        // when
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // then
        Assertions.assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable).usingRecursiveComparison()
                        .ignoringFields("id", "tableGroupId")
                        .isEqualTo(new OrderTable(null, null, 3, true))
        );
    }

    private OrderTable order_table을_생성한다(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

    @Test
    void 이미_ID가_있으면_저장시_update를_진행한다() {
        // given
        OrderTable orderTable = order_table을_생성한다(tableGroupId, numberOfGuests, empty);
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
        OrderTable orderTable = order_table을_생성한다(tableGroupId, numberOfGuests, empty);

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
        OrderTable orderTable = order_table을_생성한다(tableGroupId, numberOfGuests, empty);
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
        // before
        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now());
        Long tableGroupId = tableGroupRepository.save(tableGroup).getId();

        // given
        OrderTable orderTable = order_table을_생성한다(tableGroupId, 10, true);
        orderTableRepository.save(orderTable);

        // when
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        // then
        assertThat(orderTables).hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(Arrays.asList(new OrderTable(null, tableGroupId, 10, true)));
    }

    @Test
    void order_table_id들이_포함된_order를_조회한다() {
        List<OrderTable> orderTables = orderTableRepository.findAllByOrderTableIdsIn(Arrays.asList(1L, 2L, 9L));

        // then
        assertThat(orderTables).hasSize(3);
    }

    @Test
    void 모두_save_한다() {
        // given
        OrderTable orderTable = order_table을_생성한다(tableGroupId, numberOfGuests, empty);
        OrderTable orderTable2 = order_table을_생성한다(tableGroupId, 7, empty);

        // when
        int affectedQueryCount = orderTableRepository.saveAll(Arrays.asList(orderTable, orderTable2));

        // then
        assertThat(affectedQueryCount).isEqualTo(2);
    }
}
