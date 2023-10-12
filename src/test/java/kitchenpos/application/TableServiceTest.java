package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블 생성 테스트")
    public void createTableTest() {
        //given
        OrderTable orderTable = new OrderTable();
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        //when
        OrderTable result = tableService.create(new OrderTable());

        //then
        assertThat(result).isEqualTo(orderTable);
    }

    @Test
    @DisplayName("테이블 상태 변경 테스트")
    public void changeEmptyTest() {
        //given
        OrderTable orderTable = new OrderTable();
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        //when
        OrderTable result = tableService.changeEmpty(1L, new OrderTable());

        //then
        assertThat(result).isEqualTo(orderTable);
    }

    @Test
    @DisplayName("테이블 상태 변경 실패 테스트")
    public void changeEmptyFailTest() {
        //given
        OrderTable orderTable = new OrderTable();
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(true);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 손님 수 변경 테스트")
    public void changeNumberOfGuestsTest() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        //when
        OrderTable result = tableService.changeNumberOfGuests(1L, new OrderTable());

        //then
        assertThat(result).isEqualTo(orderTable);
    }

    @Test
    @DisplayName("테이블 손님 수 변경 실패 테스트")
    public void changeNumberOfGuestsFailTest() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
