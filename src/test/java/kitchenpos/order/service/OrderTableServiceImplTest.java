package kitchenpos.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.fixtures.ServiceTest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.service.OrderTableServiceImpl;
import kitchenpos.ordertable.service.dto.OrderTableRequest;
import kitchenpos.ordertable.service.dto.OrderTableResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.TableFixtures;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class OrderTableServiceImplTest extends ServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderTableServiceImpl orderTableServiceImpl;

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

        assertDoesNotThrow(() -> orderTableServiceImpl.create(request));
        verify(orderTableRepository, times(1)).save(any());
    }

    @Test
    void 주문_테이블_리스트를_반환한다() {
        given(orderTableRepository.findAll()).willReturn(TableFixtures.createOrderTables(true));

        List<OrderTableResponse> orderTables = assertDoesNotThrow(() -> orderTableServiceImpl.list());
        assertThat(orderTables).isNotEmpty();
    }

    @Test
    void 주문_테이블의_상태를_변경한다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(emptyTable));
        given(orderTableRepository.save(any())).willReturn(nonEmptyTable);
        given(orderRepository.findAllByOrderTableId(any())).willReturn(OrderFixtures.createCompletedOrders());

        assertDoesNotThrow(() -> orderTableServiceImpl.changeEmpty(emptyTable.getId(), false));
        verify(orderTableRepository).save(ArgumentMatchers.refEq(nonEmptyTable, "tableGroup", "orders"));
    }

    @Test
    void 상태_변경_시_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderTableServiceImpl.changeEmpty(emptyTable.getId(), false));
    }

    @Test
    void 상태_변경_시_완료되지_않은_주문이_있으면_예외를_반환한다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(emptyTable));
        given(orderRepository.findAllByOrderTableId(any())).willReturn(OrderFixtures.createMealOrders());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> orderTableServiceImpl.changeEmpty(emptyTable.getId(), false));
        assertThat(exception.getMessage()).isEqualTo("주문 상태가 완료되지 않았습니다.");
    }

    @Test
    void 주문_테이블의_손님_수를_변경한다() {
        OrderTable crowdTable = TableFixtures.createOrderTable(1L, null, 3000, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(nonEmptyTable));
        given(orderTableRepository.save(any())).willReturn(crowdTable);

        assertDoesNotThrow(() -> orderTableServiceImpl.changeNumberOfGuests(nonEmptyTable.getId(), 3000));
    }

    @Test
    void 손님_수_변경_시_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(
            NoSuchElementException.class,
            () -> orderTableServiceImpl.changeNumberOfGuests(nonEmptyTable.getId(), 3000)
        );
    }
}
