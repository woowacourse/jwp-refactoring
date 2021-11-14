package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kitchenpos.Fixtures;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.TableGroupRepository;
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

    @Mock
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        orderTable = Fixtures.makeOrderTable();
    }

    @DisplayName("table 생성")
    @Test
    void create() {
        TableGroup tableGroup = Fixtures.makeTableGroup();

        OrderTableRequest orderTableRequest = new OrderTableRequest(1L, 1, true);

        tableService.create(orderTableRequest);

        verify(orderTableRepository).save(any(OrderTable.class));
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

        OrderTableRequest orderTableRequest = new OrderTableRequest(1L, 1, false);

        tableService.changeEmpty(1L, orderTableRequest);

        verify(orderTableRepository).findById(anyLong());
        verify(orderRepository).existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList());
    }
}
