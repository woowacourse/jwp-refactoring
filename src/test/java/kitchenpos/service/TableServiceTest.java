package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kitchenpos.application.TableService;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TableServiceTest extends ServiceTest {
    @Mock
    private JdbcTemplateOrderDao orderDao;
    @Mock
    private JdbcTemplateOrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 생성할 수 있다")
    @Test
    void create() {
        tableService.create(OrderTableFixture.orderTable());
    }

    @DisplayName("모든 주문 테이블을 조회 할 수 있다")
    @Test
    void findAll() {
        tableService.list();
    }

    @DisplayName("주문 테이블을 비울 수 있다")
    @Test
    void changeEmpty() {
        when(orderTableDao.findById(any())).thenReturn(Optional.of(OrderTableFixture.notGroupedTable()));
        tableService.changeEmpty(0L, OrderTableFixture.orderTable());
    }

    @DisplayName("주문 테이블을 비울 시 주문 테이블 id가 존재해야 한다")
    @Test
    void changeEmptyNullOrderTableId() {
        assertThatThrownBy(() -> tableService.changeEmpty(0L, OrderTableFixture.orderTable())).
                isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 단체 지정 id가 null이어야 한다")
    @Test
    void changeEmptyNotNullGroupId() {
        assertThatThrownBy(() -> tableService.changeEmpty(0L, OrderTableFixture.orderTable())).
                isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블을 비울 시 요리중이거나 식사중인 상태일 수 없다")
    @Test
    void changeInvalidStatus() {

        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(0L, OrderTableFixture.orderTable())).
                isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경할 수 있다")
    @Test
    void changeNumberOfGuests() {
        when(orderTableDao.findById(any())).thenReturn(Optional.of(OrderTableFixture.orderTable()));

        tableService.changeNumberOfGuests(0L, OrderTableFixture.orderTable());
    }

    @DisplayName("주문 테이블에 방문한 손님이 0명 이하일 수 없다")
    @Test
    void changeMinusNumberOfGuests() {
        OrderTable orderTable = new OrderTable(0L, 0L, -1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTable)).
                isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 인원 수 변경시 주문 테이블 id가 등록되어 있어야 한다")
    @Test
    void changeNotRegisteredTableId() {

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, OrderTableFixture.orderTable())).
                isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 인원 수 변경시 주문 테이블이 비어있지 않아야 한다")
    @Test
    void changeEmptyOrdertable() {
        OrderTable orderTable = new OrderTable(0L, 0L, 0, true);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTable)).
                isInstanceOf(IllegalArgumentException.class);
    }
}
