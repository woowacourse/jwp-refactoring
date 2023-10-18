package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.anyList;
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
    private OrderTable emptyOrderTable;

    @BeforeEach
    void setUp() {
        emptyOrderTable = new OrderTable(1L, null, 0, true);
    }

    @Test
    @DisplayName("테이블 생성 테스트")
    public void createTableTest() {
        //given
        given(orderTableDao.save(any(OrderTable.class))).willReturn(emptyOrderTable);

        //when
        OrderTable result = tableService.create(new OrderTable());

        //then
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(emptyOrderTable);
    }

    @Test
    @DisplayName("테이블 상태 변경 테스트 - 비어있지 않은 테이블로 변경")
    public void changeEmptyTest() {
        //given
        final OrderTable notEmptyTable = new OrderTable(1L, null, 0, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(emptyOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(notEmptyTable);

        //when
        OrderTable result = tableService.changeEmpty(1L, notEmptyTable);

        //then
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("비어있지 않은 테이블 손님 수 변경 테스트")
    public void changeNumberOfGuestsTest() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 0, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);
        OrderTable changedOrderTable = new OrderTable(2L, null, 3, false);

        //when
        OrderTable result = tableService.changeNumberOfGuests(1L, changedOrderTable);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    @DisplayName("테이블 손님 수 변경 실패 테스트 - 빈 테이블인 경우")
    public void changeNumberOfGuestsFailTest() {
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(emptyOrderTable));

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
