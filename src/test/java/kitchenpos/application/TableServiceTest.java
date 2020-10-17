package kitchenpos.application;

import kitchenpos.config.Dataset;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService service;

    private OrderTable orderTable;

    @BeforeEach
    public void setUp() {
        orderTable = Dataset.orderTable_4_명(false);
    }

    @DisplayName("주문 테이블 생성")
    @Test
    public void createTable() {
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        final OrderTable savedOrderTable = service.create(orderTable);

        assertThat(savedOrderTable).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("주문 테이블 목록 조회")
    @Test
    public void readTables() {
        given(orderTableDao.findAll()).willReturn(Lists.newArrayList(orderTable));

        final List<OrderTable> tables = service.list();

        assertThat(tables).hasSize(1);
    }

    @DisplayName("Empty 상태 변경 불가 - 조리중, 식사중")
    @Test
    public void changeFailEmpty() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        assertThatThrownBy(() -> service.changeEmpty(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty 상태 변경")
    @Test
    public void changeEmpty() {
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(true);

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        final OrderTable table = service.changeEmpty(orderTable.getId(), newOrderTable);

        assertThat(table.isEmpty()).isTrue();
    }

    @DisplayName("인원 수 변경 실패 - 기존 인원 수 음수일 때")
    @Test
    public void changeFailNumberOfGuests() {
        orderTable.setNumberOfGuests(-2);

        assertThatThrownBy(() -> service.changeNumberOfGuests(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인원 수 변경 실패 - 원하는 테이블이 없을 때")
    @Test
    public void changeFailNotExistedTable() {
        given(orderTableDao.findById(anyLong())).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> service.changeNumberOfGuests(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인원 수 변경 실패 - 사용 중인 테이블")
    @Test
    public void changeFailTableEmpty() {
        orderTable.setNumberOfGuests(-2);

        assertThatThrownBy(() -> service.changeNumberOfGuests(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인원 수 변경")
    @Test
    public void changeNumberOfGuests() {
        OrderTable resultTable = new OrderTable();
        resultTable.setId(7L);
        resultTable.setNumberOfGuests(3);

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(3);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(resultTable);

        final OrderTable savedOrderTable = service.changeNumberOfGuests(orderTable.getId(), newOrderTable);

        assertThat(savedOrderTable.getId()).isEqualTo(7L);
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(3);
    }
}
