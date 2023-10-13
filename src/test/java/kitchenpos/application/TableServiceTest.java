package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.ChangeEmptyRequest;
import kitchenpos.ui.dto.NumberOfGuestsRequest;
import kitchenpos.ui.dto.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @InjectMocks
    private TableService tableService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("테이블에 대한 주문을 생성한다.")
    void create() {
        // given
        final OrderTableRequest request = new OrderTableRequest(3, false);

        // when
        final OrderTable result = tableService.create(request);

        // then
        verify(orderTableDao, times(1)).save(any());
    }

    @Test
    @DisplayName("테이블에 대한 주문을 조회한다.")
    void list() {
        final List<OrderTable> result = tableService.list();
        verify(orderTableDao, times(1)).findAll();
    }

    @Nested
    class ChangeEmptyTest {
        @Test
        @DisplayName("이미 테이블이 다른 테이블 그룹에 속해있다면 예외가 발생한다.")
        void emptyTableGroupId() {
            // given
            final OrderTable orderTable = mock(OrderTable.class);
            given(orderTable.getTableGroupId()).willReturn(3L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, new ChangeEmptyRequest(false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("해당 테이블이 COOKING, MEAL 상태가 아니라면 예외가 발생한다.")
        void existsByOrderTableIdAndOrderStatusIn() {
            // given
            final OrderTable orderTable = mock(OrderTable.class);
            given(orderTable.getTableGroupId()).willReturn(null);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(true);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, new ChangeEmptyRequest(false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블의 비어 있는 상태를 수정한다.")
        void create() {
            // given
            final OrderTable orderTable = mock(OrderTable.class);
            given(orderTable.getTableGroupId()).willReturn(null);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(false);

            // when
            tableService.changeEmpty(1L, new ChangeEmptyRequest(false));

            // then
            verify(orderTableDao, times(1)).save(any());
        }
    }

    @Nested
    class ChangeNumberOfGuestsTest {
        @DisplayName("한 테이블의 손님 수가 0보다 작으면 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {-1, -10, 0})
        void numberOfGuestsNegative(int value) {
            // given
            final NumberOfGuestsRequest request = new NumberOfGuestsRequest(value);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블의 손님 숫자를 변경한다.")
        void changeNumberOfGuests() {
            // given
            final NumberOfGuestsRequest request = new NumberOfGuestsRequest(3);
            final OrderTable orderTable = new OrderTable(4, false);
            final OrderTable savedOrderTable = new OrderTable(3, false);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderTableDao.save(any())).willReturn(savedOrderTable);

            // when
            final OrderTable result = tableService.changeNumberOfGuests(1L, request);

            // then
            assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        }
    }
}
