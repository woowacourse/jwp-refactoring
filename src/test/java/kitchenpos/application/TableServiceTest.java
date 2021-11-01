package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.TableFixtures;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TableServiceTest extends ServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable emptyTable;
    private OrderTable nonEmptyTable;
    private OrderTableRequest request;

    @BeforeEach
    void setUp() {
        emptyTable = TableFixtures.createOrderTable(true);
        nonEmptyTable = TableFixtures.createOrderTable(false);
        request = TableFixtures.createOrderTableRequest(emptyTable);
    }

    @Test
    void 주문_테이블을_생성한다() {
        given(orderTableRepository.save(any())).willReturn(emptyTable);

        assertDoesNotThrow(() -> tableService.create(request));
        verify(orderTableRepository, times(1)).save(any());
    }

    @Test
    void 주문_테이블_리스트를_반환한다() {
        given(orderTableRepository.findAll()).willReturn(TableFixtures.createOrderTables(true));

        List<OrderTableResponse> orderTables = assertDoesNotThrow(() -> tableService.list());
        assertThat(orderTables).isNotEmpty();
    }

    @Test
    void 주문_테이블의_상태를_변경한다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(emptyTable));
        given(orderTableRepository.save(any())).willReturn(nonEmptyTable);

        assertDoesNotThrow(() -> tableService.changeEmpty(emptyTable.getId(), false));
        verify(orderTableRepository).save(ArgumentMatchers.refEq(nonEmptyTable, "tableGroup", "orders"));
    }

    @Test
    void 상태_변경_시_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> tableService.changeEmpty(emptyTable.getId(), false));
    }

    @Test
    void 주문_테이블의_손님_수를_변경한다() {
        OrderTable crowdTable = TableFixtures.createOrderTable(1L, null, OrderFixtures.createMealOrders(), 3000, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(nonEmptyTable));
        given(orderTableRepository.save(any())).willReturn(crowdTable);

        assertDoesNotThrow(() -> tableService.changeNumberOfGuests(nonEmptyTable.getId(), 3000));
    }

    @Test
    void 손님_수_변경_시_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(
            NoSuchElementException.class,
            () -> tableService.changeNumberOfGuests(nonEmptyTable.getId(), 3000)
        );
    }
}
