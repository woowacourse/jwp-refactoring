package kitchenpos.application;

import static kitchenpos.Fixture.ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;

    @Test
    void create() {
        //given
        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(ORDER_TABLE);

        //when
        OrderTableCreateRequest dto = new OrderTableCreateRequest(0, true);
        OrderTableResponse savedOrderTable = tableService.create(dto);

        //then
        assertAll(
                () -> assertThat(savedOrderTable.getId()).isEqualTo(ORDER_TABLE.getId()),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(ORDER_TABLE.getNumberOfGuests()),
                () -> assertThat(savedOrderTable.getTableGroupId()).isEqualTo(ORDER_TABLE.getTableGroupId()),
                () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(ORDER_TABLE.isEmpty())
        );
    }

    @Test
    void list() {
        //given
        given(orderTableDao.findAll())
                .willReturn(List.of(ORDER_TABLE));

        //when
        List<OrderTableResponse> orderTables = tableService.list();

        //then
        assertThat(orderTables).hasSize(1);
    }

    @Test
    void changeEmpty() {
        //given
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(ORDER_TABLE));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(false);
        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(new OrderTable(ORDER_TABLE.getId(), ORDER_TABLE.getNumberOfGuests(), false));

        //when
        OrderTableChangeEmptyRequest dto = new OrderTableChangeEmptyRequest(false);
        OrderTableResponse changedOrderTable = tableService.changeEmpty(1L, dto);

        //then
        boolean expected = false;
        assertThat(changedOrderTable.isEmpty()).isEqualTo(expected);
    }

    @Test
    void changeNumberOfGuests() {
        //given
        int expected = 4;
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(ORDER_TABLE));
        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(new OrderTable(ORDER_TABLE.getId(), expected, ORDER_TABLE.isEmpty()));

        //when
        Long orderTableId = 1L;
        OrderTableChangeNumberOfGuestRequest dto = new OrderTableChangeNumberOfGuestRequest(4);
        OrderTableResponse savedOrderTable = tableService.changeNumberOfGuests(orderTableId, dto);

        //then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(expected);
    }
}
