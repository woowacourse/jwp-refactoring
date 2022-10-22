package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.support.TestFixtureFactory.단체_지정을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableServiceTest {

    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderDao orderDao;

    @Test
    void 주문_테이블을_생성할_수_있다() {
        OrderTable orderTable = 주문_테이블을_생성한다(null, 1, false);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(orderTable)
        );
    }

    @Test
    void 주문_테이블_목록을_조회할_수_있다() {
        OrderTable orderTable1 = tableService.create(주문_테이블을_생성한다(null, 1, false));
        OrderTable orderTable2 = tableService.create(주문_테이블을_생성한다(null, 0, true));

        List<OrderTable> actual = tableService.list();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(orderTable1, orderTable2);
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경할_수_있다() {
        OrderTable orderTable = tableService.create(주문_테이블을_생성한다(null, 1, false));

        OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), 주문_테이블을_생성한다(null, 1, true));

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void 변경_대상_테이블이_존재하지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> tableService.changeEmpty(0L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 변경_대상_테이블이_단체_지정되어_있으면_예외를_반환한다() {
        OrderTable orderTable1 = tableService.create(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = tableService.create(주문_테이블을_생성한다(null, 0, true));
        orderTable1.setTableGroupId(
                tableGroupDao.save(단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2))).getId());
        orderTableDao.save(orderTable1);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 변경_대상_테이블의_주문_목록_중_식사_중인_주문이_있을_경우_예외를_반환한다() {
        OrderTable orderTable = tableService.create(주문_테이블을_생성한다(null, 1, false));
        orderDao.save(주문을_생성한다(orderTable.getId(), COOKING.name(), LocalDateTime.now(), null));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 변경_대상_테이블의_주문_목록_중_조리_중인_주문이_있을_경우_예외를_반환한다() {
        OrderTable orderTable = tableService.create(주문_테이블을_생성한다(null, 1, false));
        orderDao.save(주문을_생성한다(orderTable.getId(), MEAL.name(), LocalDateTime.now(), null));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_방문한_손님_수를_변경할_수_있다() {
        OrderTable orderTable = tableService.create(주문_테이블을_생성한다(null, 0, false));
        orderTable.setNumberOfGuests(1);

        tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        assertThat(orderTable.getNumberOfGuests()).isOne();
    }

    @Test
    void 변경하려는_인원이_0명_미만이면_예외를_반환한다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 주문_테이블을_생성한다(0L, -1, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 인원_변경_테이블이_존재하지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, 주문_테이블을_생성한다(0L, 1, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 인원_변경_테이블이_빈_테이블이면_예외를_반환한다() {
        OrderTable orderTable = tableService.create(주문_테이블을_생성한다(null, 0, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}