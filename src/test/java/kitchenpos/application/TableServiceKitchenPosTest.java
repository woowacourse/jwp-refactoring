package kitchenpos.application;

import kitchenpos.KitchenPosTestFixture;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableServiceKitchenPosTest extends KitchenPosTestFixture {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = 주문_테이블을_저장한다(null, null, 0, true);
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create() {
        // given
        OrderTable savedOrderTable = 주문_테이블을_저장한다(null, null, 0, true);
        given(orderTableDao.save(orderTable)).willReturn(savedOrderTable);

        // when
        OrderTable result = tableService.create(orderTable);

        // then
        assertThat(result).isEqualTo(savedOrderTable);

        verify(orderTableDao, times(1)).save(orderTable);
    }

    @DisplayName("전체 테이블을 조회한다.")
    @Test
    void list() {
        // given
        orderTable = 주문_테이블을_저장한다(1L, null, orderTable.getNumberOfGuests(), orderTable.isEmpty());
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(orderTable));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).containsExactly(orderTable);
        verify(orderTableDao, times(1)).findAll();
    }

    @DisplayName("테이블의 공석 유무를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        orderTable = 주문_테이블을_저장한다(
                1L,
                null,
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, COOKING_OR_MEAL_STATUS)).willReturn(false);

        OrderTable changedOrderTable = 주문_테이블을_저장한다(null, null, 0, false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(changedOrderTable);

        // when
        OrderTable result = tableService.changeEmpty(1L, this.orderTable);

        // then
        assertThat(result).isEqualTo(changedOrderTable);

        verify(orderTableDao, times(1)).findById(1L);
        verify(orderDao, times(1)).existsByOrderTableIdAndOrderStatusIn(1L, COOKING_OR_MEAL_STATUS);
        verify(orderTableDao, times(1)).save(any(OrderTable.class));
    }

    @DisplayName("단체 손님인 경우 변경할 수 없다.")
    @Test
    void validTableGroupId() {
        // given
        orderTable = 주문_테이블을_저장한다(
                1L,
                1L,
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable)).isInstanceOf(IllegalArgumentException.class);

        verify(orderTableDao, times(1)).findById(1L);
    }

    @DisplayName("조리/식사중인 경우 변경할 수 없다.")
    @Test
    void validOrderTableStatus() {
        // given
        orderTable = 주문_테이블을_저장한다(
                1L,
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, COOKING_OR_MEAL_STATUS)).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable)).isInstanceOf(IllegalArgumentException.class);

        verify(orderTableDao, times(1)).findById(1L);
        verify(orderDao, times(1)).existsByOrderTableIdAndOrderStatusIn(1L, COOKING_OR_MEAL_STATUS);
    }

    @DisplayName("테이블의 손님 인원을 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        orderTable = 주문_테이블을_저장한다(
                1L,
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                false
        );
        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable));

        OrderTable expected = 주문_테이블을_저장한다(
                orderTable.getId(),
                null,
                orderTable.getNumberOfGuests(),
                false
        );
        given(orderTableDao.save(any(OrderTable.class))).willReturn(expected);

        // when
        OrderTable changedOrderTable = 주문_테이블을_저장한다(
                null,
                null,
                10,
                false
        );
        OrderTable result = tableService.changeNumberOfGuests(1L, changedOrderTable);

        // then
        assertThat(result).isEqualTo(expected);

        verify(orderTableDao, times(1)).findById(1L);
        verify(orderTableDao, times(1)).save(any(OrderTable.class));
    }

    @DisplayName("테이블의 손님 인원은 0이상이어야한다.")
    @Test
    void validNumberOfGuests() {
        // given
        OrderTable changedOrderTable = 주문_테이블을_저장한다(
                orderTable.getId(),
                null,
                -1,
                orderTable.isEmpty()
        );

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("변경할 테이블이 존재해야한다.")
    @Test
    void validOrderTable() {
        // given
        given(orderTableDao.findById(1L)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);

        verify(orderTableDao, times(1)).findById(1L);
    }

    @DisplayName("테이블이 공석인 경우 변경할 수 없다.")
    @Test
    void validOrderTableIsEmpty() {
        // given
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);

        verify(orderTableDao, times(1)).findById(1L);
    }
}