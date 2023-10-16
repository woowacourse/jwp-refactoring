package kitchenpos.application;

import fixture.OrderBuilder;
import fixture.OrderTableBuilder;
import fixture.TableGroupBuilder;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableServiceTest extends ServiceTest {

    @Autowired
    TableService tableService;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Test
    void 테이블그룹을_저장한다() {
        OrderTable orderTable = OrderTableBuilder.init()
                .build();

        OrderTable created = tableService.create(orderTable);

        assertThat(created.getId()).isNotNull();
    }

    @Test
    void 모든_주문테이블을_조회한다() {
        List<OrderTable> all = tableService.list();

        assertThat(all).hasSize(9);
    }

    @Test
    void 주문테이블을_빈_테이블로_바꾼다() {
        OrderTable orderTable = OrderTableBuilder.init()
                .id(9L)
                .empty(true)
                .build();

        OrderTable edit = tableService.changeEmpty(9L, orderTable);

        assertThat(edit.isEmpty()).isTrue();
    }

    @Test
    void 테이블그룹에_속해있지_않으면_에외를_발생한다() {
        OrderTable orderTable = OrderTableBuilder.init()
                .id(1L)
                .tableGroupId(null)
                .build();

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블의_아이디로_조리중_식사중_상태인_주문이_존재하면_예외를_발생한다() {
        TableGroup saveTableGroup = tableGroupDao.save(TableGroupBuilder.init().build());
        OrderTable saveOrderTable = orderTableDao.save(OrderTableBuilder.init().tableGroupId(saveTableGroup.getId()).empty(false).build());
        orderDao.save(OrderBuilder.init().orderTableId(saveOrderTable.getId()).orderStatus(OrderStatus.MEAL.name()).build());

        OrderTable orderTable = OrderTableBuilder.init()
                .id(saveOrderTable.getId())
                .empty(true)
                .numberOfGuests(saveOrderTable.getNumberOfGuests())
                .tableGroupId(saveOrderTable.getTableGroupId())
                .build();

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_고객수를_변경한다() {
        int changeGuestCount = 10;
        OrderTable orderTable = OrderTableBuilder.init()
                .id(9L)
                .numberOfGuests(changeGuestCount)
                .empty(false)
                .build();

        OrderTable edit = tableService.changeNumberOfGuests(9L, orderTable);

        assertThat(edit.getNumberOfGuests()).isEqualTo(changeGuestCount);
    }

    @Test
    void 고객수가_음수이면_예외를_발생한다() {
        int changeGuestCount = -1;
        OrderTable orderTable = OrderTableBuilder.init()
                .id(1L)
                .numberOfGuests(changeGuestCount)
                .build();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블이_존재하지_않으면_예외를_발생한다() {
        int changeGuestCount = 10;
        OrderTable orderTable = OrderTableBuilder.init()
                .id(100L)
                .numberOfGuests(changeGuestCount)
                .build();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable)).isInstanceOf(IllegalArgumentException.class);
    }
}
