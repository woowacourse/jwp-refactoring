package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블을 성공적으로 생성한다")
    void testCreateSuccess() {
        //given
        final OrderTable savedOrderTable = new OrderTable(1L, 1L, 1, false);

        final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(1, false);

        when(orderTableDao.save(any()))
                .thenReturn(savedOrderTable);

        //when
        final OrderTable result = tableService.create(orderTableCreateRequest);

        //then
        assertThat(result).isEqualTo(savedOrderTable);
    }

    @Test
    @DisplayName("테이블을 성공적으로 조회한다")
    void testListSuccess() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, 1L, 1, false);
        final OrderTable orderTable2 = new OrderTable(2L, 2L, 1, false);
        final OrderTable orderTable3 = new OrderTable(3L, 3L, 1, false);

        when(tableService.list())
                .thenReturn(List.of(orderTable1, orderTable2, orderTable3));

        //when
        final List<OrderTable> result = tableService.list();

        //then
        assertThat(result).isEqualTo(List.of(orderTable1, orderTable2, orderTable3));
    }

    @Test
    @DisplayName("테이블을 빈 테이블로 성공적으로 수정한다")
    void testChangeEmptySuccess() {
        // given
        final Long orderTableId = 1L;
        final OrderTable savedOrderTable = new OrderTable(1L, null, 1, false);

        final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(
                1L, 1, true);

        when(orderTableDao.findById(any()))
                .thenReturn(Optional.of(savedOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class), anyList()))
                .thenReturn(false);
        when(orderTableDao.save(any()))
                .thenReturn(savedOrderTable);

        // when
        final OrderTable resultOrderTable = tableService.changeEmpty(orderTableId, orderTableChangeEmptyRequest);

        // then
        assertThat(resultOrderTable).isEqualTo(savedOrderTable);
    }

    @Test
    @DisplayName("테이블을 빈 상태로 변경 시 테이블 그룹이 null일 경우 예외가 발생한다")
    void testChangeEmptyWhenTableGroupIdNotNullFailure() {
        // given
        final Long orderTableId = 1L;

        final OrderTable savedOrderTable = new OrderTable(1L, 1L, 1, false);

        final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(
                null, 1, true);

        when(orderTableDao.findById(any()))
                .thenReturn(Optional.of(savedOrderTable));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTableChangeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블을 빈 상태로 변경 시 요리 중이거나 식사 중일 경우 예외가 발생한다")
    void testChangeEmptyWhenOrderAlreadyCookOrMealFailure() {
        // given
        final Long orderTableId = 1L;
        final OrderTable savedOrderTable = new OrderTable(1L, null, 1, false);

        final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(
                null, 1, true);

        when(orderTableDao.findById(any()))
                .thenReturn(Optional.of(savedOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class), anyList()))
                .thenReturn(true);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTableChangeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("테이블의 손님 수를 성공적으로 변경한다")
    void testChangeNumberOfGuestsSuccess() {
        // given
        final Long orderTableId = 1L;
        final int numberOfGuests = 5;

        final OrderTable orderTable = new OrderTable(1L, numberOfGuests, false);
        final OrderTable savedOrderTable = new OrderTable(1L, numberOfGuests, false);

        when(orderTableDao.findById(any()))
                .thenReturn(Optional.of(savedOrderTable));
        when(orderTableDao.save(any()))
                .thenReturn(savedOrderTable);

        // when
        final OrderTable resultOrderTable = tableService.changeNumberOfGuests(orderTableId, orderTable);

        // then
        assertThat(resultOrderTable).isNotNull();
        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("테이블의 손님 수 변경 시 0보다 작은 수로 변경할 경우 예외가 발생한다")
    void testChangeNumberOfGuestsWhenNumberOfGuestsLowerThanZeroFailure() {
        // given
        final Long orderTableId = 1L;
        final int numberOfGuests = -5;

        final OrderTable orderTable = new OrderTable(1L, numberOfGuests, false);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 수 변경 시 테이블이 비어있을 경우 예외가 발생한다")
    void testChangeNumberOfGuestsWhenOrderTableIsEmptyFailure() {
        // given
        final Long orderTableId = 1L;
        final int numberOfGuests = 5;

        final OrderTable orderTable = new OrderTable(1L, numberOfGuests, true);
        final OrderTable savedOrderTable = new OrderTable(1L, numberOfGuests, true);

        when(orderTableDao.findById(any()))
                .thenReturn(Optional.of(savedOrderTable));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 수 변경 시 존재하지 않는 테이블일 경우 예외가 발생한다")
    void testChangeNumberOfGuestsWhenOrderTableNotFoundFailure() {
        // given
        final Long orderTableId = 1L;
        final int numberOfGuests = 5;

        final OrderTable orderTable = new OrderTable(1L, numberOfGuests, false);

        when(orderTableDao.findById(any()))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
