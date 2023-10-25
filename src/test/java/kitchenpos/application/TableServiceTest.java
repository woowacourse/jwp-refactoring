package kitchenpos.application;

import kitchenpos.order.OrderDao;
import kitchenpos.ordertable.OrderTableDao;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableDto;
import kitchenpos.ordertable.TableService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @MockBean
    private OrderTableDao orderTableDao;

    @MockBean
    private OrderDao orderDao;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        OrderTable orderTable = new OrderTable(null, null, 1, true);
        OrderTableDto orderTableDto = OrderTableDto.from(orderTable);

        given(orderTableDao.save(any()))
                .willReturn(orderTable);

        // when
        OrderTableDto result = tableService.create(orderTableDto);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @Test
    void 주문_테이블을_전체_조회한다() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 1L, 2, true);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 2, true);

        given(orderTableDao.findAll())
                .willReturn(List.of(orderTable1, orderTable2));

        // when
        List<OrderTableDto> result = tableService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 주문_테이블을_빈_공간으로_변경한다() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 2, false);

        OrderTable changedOrderTable = new OrderTable(1L, null, 2, true);

        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                .willReturn(false);
        given(orderTableDao.save(any()))
                .willReturn(changedOrderTable);

        // when
        OrderTableDto result = tableService.changeEmpty(orderTable.getId(), OrderTableDto.from(changedOrderTable));

        // then
        assertThat(result.isEmpty()).isEqualTo(changedOrderTable.isEmpty());
    }

    @Test
    void 주문_테이블의_게스트_수를_변경한다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);

        OrderTable changedOrderTable = new OrderTable(orderTable.getId(), orderTable.getTableGroupId(), 5, orderTable.isEmpty());

        given(orderTableDao.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any()))
                .willReturn(orderTable);

        // when
        OrderTableDto result = tableService.changeNumberOfGuests(orderTable.getId(), OrderTableDto.from(changedOrderTable));

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(changedOrderTable.getNumberOfGuests());
    }

    @Test
    void 빈_테이블의_게스트_수를_변경하면_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 1, true);

        OrderTable changedOrderTable = new OrderTable(orderTable.getId(), orderTable.getTableGroupId(), 5, orderTable.isEmpty());

        given(orderTableDao.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any()))
                .willReturn(orderTable);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), OrderTableDto.from(changedOrderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_게스트_수를_0명보다_적게_변경하면_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);

        OrderTable changedOrderTable = new OrderTable(orderTable.getId(), orderTable.getTableGroupId(), -1, false);

        given(orderTableDao.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any()))
                .willReturn(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), OrderTableDto.from(changedOrderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
