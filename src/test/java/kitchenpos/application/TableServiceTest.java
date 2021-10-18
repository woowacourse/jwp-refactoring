package kitchenpos.application;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private TableService tableService;
    private OrderTable orderTable;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
        orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(1);
        orderTable.setId(1L);
    }

    @DisplayName("table 생성")
    @Test
    void create() {
        tableService.create(orderTable);

        verify(orderTableDao).save(orderTable);
    }

    @DisplayName("table 불러오기")
    @Test
    void list() {
        tableService.list();

        verify(orderTableDao).findAll();
    }

    @DisplayName("table 비우기")
    @Test
    void emptyTable() {
        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
            .willReturn(false);

        tableService.changeEmpty(1L, orderTable);

        verify(orderTableDao).findById(anyLong());
        verify(orderDao).existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList());
    }
}
