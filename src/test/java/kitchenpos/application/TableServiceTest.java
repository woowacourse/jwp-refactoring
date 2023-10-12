package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    void 테이블을_생성한다() {
        OrderTable orderTable = new OrderTable();

        tableService.create(orderTable);

        then(orderTableDao).should(times(1)).save(orderTable);
    }

    @Test
    void 테이블의_상태를_변경한다() {
        OrderTable orderTable = new OrderTable();
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), eq(List.of("COOKING", "MEAL"))))
                .willReturn(false);

        tableService.changeEmpty(1L, orderTable);
        then(orderTableDao).should(times(1)).save(orderTable);
    }

    @Test
    void 상태를_바꾸려는_테이블은_반드시_존재해야_한다() {
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상태를_바꾸려는_테이블의_테이블_그룹이_존재하면_예외발생() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상태를_바꾸려는_테이블의_주문_상태가_식사중이면_예외발생() {
        OrderTable orderTable = new OrderTable();
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, List.of("COOKING", "MEAL")))
                .willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블에_방문한_손님_수가_0이하면_예외발생() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블에_방문한_손님_수가_조절_시_테이블이_존재하지_않으면_예외발생() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 방문하려는_테이블의_상태가_빈_테이블이면_예외발생() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(true);

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블에_방문한_손님_수를_설정한다() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

        tableService.changeNumberOfGuests(1L, orderTable);
        then(orderTableDao).should(times(1)).save(orderTable);
    }
}
