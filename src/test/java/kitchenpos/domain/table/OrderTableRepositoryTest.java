package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.Optional;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.common.NumberOfGuests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class OrderTableRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 주문_테이블을_저장하면_id가_채워진다() {
        OrderTable orderTable = new OrderTable(0, true);

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(orderTable)
        );
    }

    @Test
    void 저장하는_주문_테이블의_id가_null이_아니면_업데이트한다() {
        Long orderTableId = orderTableRepository.save(new OrderTable(0, true))
                .getId();
        OrderTable updateOrderTable = new OrderTable(orderTableId, null, new NumberOfGuests(1), false);

        OrderTable savedOrderTable = orderTableRepository.save(updateOrderTable);

        assertAll(
                () -> assertThat(savedOrderTable.getId()).isEqualTo(orderTableId),
                () -> assertThat(savedOrderTable).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(updateOrderTable)
        );
    }

    @Test
    void 단체_지정_id는_null일_수_있다() {
        OrderTable orderTable = new OrderTable(0, true);

        assertDoesNotThrow(() -> orderTableRepository.save(orderTable));
    }

    @Test
    void id로_주문_테이블을_조회할_수_있다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));

        OrderTable actual = orderTableRepository.findById(orderTable.getId())
                .orElseGet(Assertions::fail);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderTable);
    }

    @Test
    void 없는_id로_주문_테이블을_조회하면_Optional_empty를_반환한다() {
        Optional<OrderTable> actual = orderTableRepository.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_주문_테이블을_조회할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(1, false));

        List<OrderTable> actual = orderTableRepository.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(orderTable1, orderTable2);
    }

    @Test
    void id_목록에_있는_주문_테이블을_조회할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        orderTableRepository.save(new OrderTable(1, false));
        List<Long> ids = List.of(orderTable1.getId());

        List<OrderTable> actual = orderTableRepository.findAllByIdIn(ids);

        assertThat(actual).hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactly(orderTable1);
    }

    @Test
    void 단체_지정_id로_주문_테이블을_조회할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));
        TableGroup tableGroup = tableGroupRepository
                .save(new TableGroup(List.of(orderTable1, orderTable2)));

        List<OrderTable> actual = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(orderTable1, orderTable2);
    }
}
