package kitchenpos.service;

import kitchenpos.application.TableService;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.OrdersRepository;
import kitchenpos.ui.dto.TableRequest;
import kitchenpos.ui.dto.TableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("테이블 서비스 테스트")
class TableServiceTest extends ServiceTest {

    @InjectMocks
    private TableService tableService;
    @Mock
    private OrdersRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    private TableRequest tableRequest;
    private TableRequest emptyTableRequest;

    @BeforeEach
    void setUp() {
        tableRequest = new TableRequest(1L, 3, false);
        emptyTableRequest = new TableRequest(2L, 0, true);

        when(orderTableRepository.save(any())).thenReturn(OrderTableFixture.create());
    }

    @DisplayName("테이블 생성")
    @Test
    void create() {
        //given
        //when
        TableResponse tableResponse = tableService.create(tableRequest);
        //then
        assertThat(tableResponse.getId()).isNotNull();
    }

    @DisplayName("테이블 조회")
    @Test
    void findAll() {
        //given
        when(orderTableRepository.findAll()).thenReturn(Collections.singletonList(OrderTableFixture.create()));
        //when
        List<TableResponse> allTables = tableService.findAll();
        //then
        assertThat(allTables).hasSize(1);
    }

    @DisplayName("테이블 empty 여부 변경")
    @Test
    void changeEmpty() {
        //given
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(OrderTableFixture.nullTableGroup()));
        when(orderRepository.findAllByOrderTableId(anyLong())).thenReturn(Collections.singletonList(OrderFixture.complete()));
        //when
        tableService.changeEmpty(anyLong(), emptyTableRequest);
        //then
        verify(orderTableRepository, times(1)).save(any());
    }

    @DisplayName("테이블 손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        //given
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(OrderTableFixture.create()));
        //when
        tableService.changeNumberOfGuests(anyLong(), new TableRequest(1L, 10, false));
        //then
        verify(orderTableRepository, times(1)).save(any());
    }
}
