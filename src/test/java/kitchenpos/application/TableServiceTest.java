package kitchenpos.application;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kitchenpos.Fixtures;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;
    private OrderTable orderTable;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        orderTable = Fixtures.makeOrderTable();
    }

    @DisplayName("table 생성")
    @Test
    void create() {
        tableService.create(orderTable);

        verify(orderTableRepository).save(orderTable);
    }

    @DisplayName("table 불러오기")
    @Test
    void list() {
        tableService.list();

        verify(orderTableRepository).findAll();
    }

    @DisplayName("table 비우기")
    @Test
    void emptyTable() {
        given(orderTableRepository.findById(anyLong()))
            .willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
            .willReturn(false);

        tableService.changeEmpty(1L, orderTable);

        verify(orderTableRepository).findById(anyLong());
        verify(orderRepository).existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList());
    }
}
