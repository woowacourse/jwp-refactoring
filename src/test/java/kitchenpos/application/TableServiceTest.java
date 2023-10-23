package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.support.domain.OrderTableTestSupport;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @InjectMocks
    TableService target;

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        //given
        final OrderTableTestSupport.Builder builder = OrderTableTestSupport.builder();
        final OrderTable orderTable = builder.build();
        final OrderTableCreateRequest request = builder.buildToOrderTableCreateRequest();
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        //when

        //then
        assertDoesNotThrow(() -> target.create(request));
    }

    @DisplayName("테이블을 전체 조회한다.")
    @Test
    void list() {
        //given
        final OrderTable table1 = OrderTableTestSupport.builder().build();
        final OrderTable table2 = OrderTableTestSupport.builder().build();
        final OrderTable table3 = OrderTableTestSupport.builder().build();
        final List<OrderTable> orderTables = List.of(table1, table2, table3);
        given(orderTableDao.findAll()).willReturn(orderTables);

        //when

        //then
        assertDoesNotThrow(() -> target.list());
    }

    @DisplayName("테이블을 빈 테이블/사용 테이블인지 상태를 변경한다.")
    @Test
    void changeEmpty() {
        //given
        final OrderTable orderTable = OrderTableTestSupport.builder().tableGroupId(null).empty(true).build();
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        final var request = new OrderTableChangeEmptyRequest(true);

        //when
        final OrderTable result = target.changeEmpty(orderTable.getId(), request);

        //then
        assertThat(result.isEmpty()).isEqualTo(request.isEmpty());
    }

    @DisplayName("테이블이 단체 지정되어있으면 예외 처리한다.")
    @Test
    void changeEmpty_fail_hasGroup() {
        //given
        final var request = new OrderTableChangeEmptyRequest(false);
        final OrderTable savedOrder = OrderTableTestSupport.builder().tableGroupId(1L).empty(true).build();
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrder));

        //when

        //then
        assertThatThrownBy(() -> target.changeEmpty(savedOrder.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아닐 때, 계산 완료 상태가 아니면 예외 처리한다.")
    @Test
    void changeEmpty_fail_notEmpty() {
        //given
        final var request = new OrderTableChangeEmptyRequest(false);
        final OrderTable savedOrder = OrderTableTestSupport.builder().tableGroupId(null).empty(false).build();
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrder));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        //when

        //then
        assertThatThrownBy(() -> target.changeEmpty(savedOrder.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        final var request = new OrderTableChangeNumberOfGuestsRequest(10);
        final OrderTable savedOrder = OrderTableTestSupport.builder().numberOfGuests(1).build();
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrder));
        given(orderTableDao.save(savedOrder)).willReturn(savedOrder);

        //when
        final OrderTable result = target.changeNumberOfGuests(savedOrder.getId(), request);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("손님의 수가 0보다 작으면 예외 처리한다.")
    @Test
    void changeNumberOfGuests_fail_numberOfGuests_minus() {
        //given
        final var request = new OrderTableChangeNumberOfGuestsRequest(0);
        final OrderTable savedOrder = OrderTableTestSupport.builder().numberOfGuests(1).build();

        //when

        //then
        assertThatThrownBy(() -> target.changeNumberOfGuests(savedOrder.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이면 예외 처리한다.")
    @Test
    void changeNumberOfGuests_fail_empty() {
        //given
        final var request = new OrderTableChangeNumberOfGuestsRequest(10);
        final OrderTable savedOrder = OrderTableTestSupport.builder().numberOfGuests(1).empty(true).build();
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrder));

        //when

        //then
        assertThatThrownBy(() -> target.changeNumberOfGuests(savedOrder.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
