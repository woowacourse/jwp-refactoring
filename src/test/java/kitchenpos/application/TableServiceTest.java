package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
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
        final OrderTable input = new OrderTable();
        final OrderTable result = OrderTableTestSupport.builder().build();
        given(orderTableDao.save(any())).willReturn(result);

        //when

        //then
        assertDoesNotThrow(() -> target.create(input));
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
        final OrderTable input = new OrderTable();
        input.setEmpty(false);
        final OrderTable savedOrder = OrderTableTestSupport.builder().tableGroupId(null).empty(true).build();
        given(orderTableDao.findById(any())).willReturn(Optional.of(savedOrder));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(savedOrder)).willReturn(savedOrder);

        //when
        final OrderTable result = target.changeEmpty(savedOrder.getId(), input);

        //then
        assertThat(result.isEmpty()).isEqualTo(input.isEmpty());
    }

    @DisplayName("테이블이 단체 지정되어있으면 예외 처리한다.")
    @Test
    void changeEmpty_fail_hasGroup() {
        //given
        final OrderTable input = new OrderTable();
        input.setEmpty(false);
        final OrderTable savedOrder = OrderTableTestSupport.builder().tableGroupId(1L).empty(true).build();
        given(orderTableDao.findById(any())).willReturn(Optional.of(savedOrder));

        //when

        //then
        assertThatThrownBy(() -> target.changeEmpty(savedOrder.getId(), input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아닐 때, 계산 완료 상태가 아니면 예외 처리한다.")
    @Test
    void changeEmpty_fail_notEmpty() {
        //given
        final OrderTable input = new OrderTable();
        input.setEmpty(false);
        final OrderTable savedOrder = OrderTableTestSupport.builder().tableGroupId(null).empty(false).build();
        given(orderTableDao.findById(any())).willReturn(Optional.of(savedOrder));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        //when

        //then
        assertThatThrownBy(() -> target.changeEmpty(savedOrder.getId(), input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        final OrderTable input = new OrderTable();
        input.setNumberOfGuests(10);
        final OrderTable savedOrder = OrderTableTestSupport.builder().numberOfGuests(1).build();
        given(orderTableDao.findById(any())).willReturn(Optional.of(savedOrder));
        given(orderTableDao.save(savedOrder)).willReturn(savedOrder);

        //when
        final OrderTable result = target.changeNumberOfGuests(savedOrder.getId(), input);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(input.getNumberOfGuests());
    }

    @DisplayName("손님의 수가 0보다 작으면 예외 처리한다.")
    @Test
    void changeNumberOfGuests_fail_numberOfGuests_minus() {
        //given
        final OrderTable input = new OrderTable();
        input.setNumberOfGuests(0);
        final OrderTable savedOrder = OrderTableTestSupport.builder().numberOfGuests(1).build();

        //when

        //then
        assertThatThrownBy(() -> target.changeNumberOfGuests(savedOrder.getId(), input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이면 예외 처리한다.")
    @Test
    void changeNumberOfGuests_fail_empty() {
        //given
        final OrderTable input = new OrderTable();
        input.setNumberOfGuests(10);
        final OrderTable savedOrder = OrderTableTestSupport.builder().numberOfGuests(1).empty(true).build();
        given(orderTableDao.findById(any())).willReturn(Optional.of(savedOrder));

        //when

        //then
        assertThatThrownBy(() -> target.changeNumberOfGuests(savedOrder.getId(), input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
