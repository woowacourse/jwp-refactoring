package kitchenpos.core.table.application;

import static kitchenpos.fixture.TableFixture.getOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.common.ServiceTest;
import kitchenpos.core.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = getOrderTable();
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(orderTable)).willReturn(orderTable);
    }

    @Test
    @DisplayName("테이블의 비운상태을 수정할 수 있다.")
    void changeEmpty() {
        // given
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        final OrderTable newStatusOrderTable = getOrderTable(false);
        final OrderTable updateOrderTable = tableService.changeEmpty(orderTable.getId(), newStatusOrderTable);

        // then
        assertThat(updateOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블의 손님 수를 바꾼다.")
    void changeNumberOfGuests() {
        // given
        final OrderTable newStatusOrderTable = getOrderTable(5);

        // when
        final OrderTable updateOrderTable = tableService.changeNumberOfGuests(1L, newStatusOrderTable);

        // then
        assertThat(updateOrderTable.getNumberOfGuests()).isEqualTo(5);
    }
}
