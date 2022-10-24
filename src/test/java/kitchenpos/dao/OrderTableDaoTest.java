package kitchenpos.dao;

import static kitchenpos.support.TestFixtureFactory.단체_지정을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class OrderTableDaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void 주문_테이블을_저장하면_id가_채워진다() {
        Long tableGroupId = tableGroupDao.save(단체_지정을_생성한다(LocalDateTime.now(), null))
                .getId();
        OrderTable orderTable = 주문_테이블을_생성한다(tableGroupId, 0, true);

        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(orderTable)
        );
    }

    @Test
    void 저장하는_주문_테이블의_id가_null이_아니면_업데이트한다() {
        Long tableGroupId = tableGroupDao.save(단체_지정을_생성한다(LocalDateTime.now(), null))
                .getId();
        Long orderTableId = orderTableDao.save(주문_테이블을_생성한다(tableGroupId, 0, true))
                .getId();
        OrderTable updateOrderTable = 주문_테이블을_생성한다(tableGroupId, 1, false);
        updateOrderTable.setId(orderTableId);

        OrderTable savedOrderTable = orderTableDao.save(updateOrderTable);

        assertThat(savedOrderTable).usingRecursiveComparison()
                .isEqualTo(updateOrderTable);
    }

    @Test
    void 단체_지정_id는_null일_수_있다() {
        OrderTable orderTable = 주문_테이블을_생성한다(null, 0, true);

        assertDoesNotThrow(() -> orderTableDao.save(orderTable));
    }

    @Test
    void id로_주문_테이블을_조회할_수_있다() {
        Long tableGroupId = tableGroupDao.save(단체_지정을_생성한다(LocalDateTime.now(), null))
                .getId();
        OrderTable orderTable = orderTableDao.save(주문_테이블을_생성한다(tableGroupId, 0, true));

        OrderTable actual = orderTableDao.findById(orderTable.getId())
                .orElseGet(Assertions::fail);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderTable);
    }

    @Test
    void 없는_id로_주문_테이블을_조회하면_Optional_empty를_반환한다() {
        Optional<OrderTable> actual = orderTableDao.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_주문_테이블을_조회할_수_있다() {
        Long tableGroupId = tableGroupDao.save(단체_지정을_생성한다(LocalDateTime.now(), null))
                .getId();
        OrderTable orderTable1 = orderTableDao.save(주문_테이블을_생성한다(tableGroupId, 0, true));
        OrderTable orderTable2 = orderTableDao.save(주문_테이블을_생성한다(tableGroupId, 1, false));

        List<OrderTable> actual = orderTableDao.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(orderTable1, orderTable2);
    }

    @Test
    void id_목록에_있는_주문_테이블을_조회할_수_있다() {
        Long tableGroupId = tableGroupDao.save(단체_지정을_생성한다(LocalDateTime.now(), null))
                .getId();
        OrderTable orderTable1 = orderTableDao.save(주문_테이블을_생성한다(tableGroupId, 0, true));
        orderTableDao.save(주문_테이블을_생성한다(tableGroupId, 1, false));
        List<Long> ids = List.of(orderTable1.getId());

        List<OrderTable> actual = orderTableDao.findAllByIdIn(ids);

        assertThat(actual).hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactly(orderTable1);
    }

    @Test
    void 단체_지정_id로_주문_테이블을_조회할_수_있다() {
        Long tableGroupId1 = tableGroupDao.save(단체_지정을_생성한다(LocalDateTime.now(), null))
                .getId();
        Long tableGroupId2 = tableGroupDao.save(단체_지정을_생성한다(LocalDateTime.now(), null))
                .getId();
        OrderTable orderTable1 = orderTableDao.save(주문_테이블을_생성한다(tableGroupId1, 0, true));
        orderTableDao.save(주문_테이블을_생성한다(tableGroupId2, 0, true));

        List<OrderTable> actual = orderTableDao.findAllByTableGroupId(tableGroupId1);

        assertThat(actual).hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactly(orderTable1);
    }
}