package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create() {
        // given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(true);
        given(orderTableDao.save(orderTable)).willReturn(savedOrderTable);

        // when
        OrderTable result = tableService.create(orderTable);

        // then
        assertThat(result).isEqualTo(savedOrderTable);
    }

    @DisplayName("전체 테이블을 조회한다.")
    @Test
    void list() {
        // given
        orderTable.setId(1L);
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(orderTable));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).containsExactly(orderTable);
    }

    @DisplayName("테이블의 공석 유무를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        orderTable.setId(1L);
        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        OrderTable changedOrderTable = new OrderTable();
        changedOrderTable.setNumberOfGuests(0);
        changedOrderTable.setEmpty(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(changedOrderTable);

        // when
        OrderTable result = tableService.changeEmpty(1L, this.orderTable);

        // then
        assertThat(result).isEqualTo(changedOrderTable);
    }

    @DisplayName("테이블의 손님 인원을 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        orderTable.setId(1L);
        orderTable.setEmpty(false);
        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable));

        OrderTable changedOrderTable = new OrderTable();
        changedOrderTable.setNumberOfGuests(10);
        changedOrderTable.setEmpty(false);
        orderTable.setNumberOfGuests(10);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        // when
        OrderTable result = tableService.changeNumberOfGuests(1L, changedOrderTable);

        // then
        assertThat(result).isEqualTo(orderTable);
    }
}