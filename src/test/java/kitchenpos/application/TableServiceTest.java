package kitchenpos.application;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.dto.order.OrderTableChangeEmptyRequest;
import kitchenpos.dto.order.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.order.OrderTableCreateRequest;
import kitchenpos.dto.order.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    private static final Integer 테이블_사람_1명 = 1;
    private static final Integer 테이블_사람_2명 = 2;
    private static final Boolean 테이블_비어있음 = true;
    private static final Boolean 테이블_비어있지않음 = false;
    private static final Long 테이블_ID_1 = 1L;
    private static final Long 테이블_ID_2 = 2L;

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;

    private TableService tableService;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderTableRepository, orderRepository);
        orderTable = new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있음, null);
    }

    @DisplayName("OrderTable 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(테이블_사람_1명, 테이블_비어있음);
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(orderTable);

        OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest);

        assertThat(orderTableResponse.getId()).isEqualTo(테이블_ID_1);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(테이블_사람_1명);
        assertThat(orderTableResponse.getEmpty()).isEqualTo(테이블_비어있음);
    }

    @DisplayName("예외 테스트 : OrderTable을 생성할 때, Null인 요청을 전달하면 예외가 발생한다.")
    @Test
    void createWithNullRequestTest() {
        assertThatThrownBy(() -> tableService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 테이블 생성 요청이 전달되었습니다.");
    }

    @DisplayName("OrderTable 전체 목록 요청 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있음, null),
                new OrderTable(테이블_ID_2, 테이블_사람_2명, 테이블_비어있음, null)
        );
        when(orderTableRepository.findAll()).thenReturn(orderTables);

        List<OrderTableResponse> foundTables = tableService.list();

        assertThat(foundTables)
                .hasSize(2)
                .extracting("id")
                .containsOnly(테이블_ID_1, 테이블_ID_2);
    }

    @DisplayName("OrderTable을 빈 테이블로 변경 시, 올바르게 변경된다.")
    @Test
    void changeEmptyTest() {
        OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(테이블_비어있지않음);
        OrderTable updatedOrderTable = new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음, null);
        when(orderTableRepository.findById(anyLong())).thenReturn(java.util.Optional.of(updatedOrderTable));
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(updatedOrderTable);

        OrderTableResponse orderTableResponse = tableService.changeEmpty(테이블_ID_1, orderTableChangeEmptyRequest);

        assertThat(orderTableResponse.getId()).isEqualTo(테이블_ID_1);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(테이블_사람_1명);
        assertThat(orderTableResponse.getEmpty()).isEqualTo(테이블_비어있지않음);
    }

    @DisplayName("예외 테스트 : OrderTable을 빈 테이블로 변경 시, 잘못된 ID를 전달하면 예외가 발생한다.")
    @Test
    void changeEmptyWithInvalidIdTest() {
        OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(테이블_비어있지않음);
        Long invalidOrderTableId = -1L;

        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTableId, orderTableChangeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테이블을 찾을 수 없습니다.");
    }

    @DisplayName("예외 테스트 : OrderTable을 빈 테이블로 변경 시, NULL값인 요청을 전달하면 예외가 발생한다.")
    @Test
    void changeEmptyWithNullRequestTest() {
        OrderTableChangeEmptyRequest invalidRequest = null;

        assertThatThrownBy(() -> tableService.changeEmpty(테이블_ID_1, invalidRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 테이블 비우기 요청이 전달되었습니다.");
    }

    @DisplayName("예외 테스트 : OrderTable을 빈 테이블로 변경 시, 테이블에 아직 완료되지 않은 주문이 있다면 예외가 발생한다.")
    @Test
    void changeEmptyWithProceedingOrderTest() {
        OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(테이블_비어있지않음);
        OrderTable updatedOrderTable = new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음, null);
        when(orderTableRepository.findById(anyLong())).thenReturn(java.util.Optional.of(updatedOrderTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(테이블_ID_1, orderTableChangeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 완료되지 않아, 테이블을 비울 수 없습니다.");
    }

    @DisplayName("OrderTable의 고객 수 변경을 요청 시, 동작이 올바르게 수행된다.")
    @Test
    void changeNumberOfGuestTest() {
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(테이블_사람_2명);
        OrderTable updatedOrderTable = new OrderTable(테이블_ID_1, 테이블_사람_2명, 테이블_비어있지않음, null);
        when(orderTableRepository.findById(anyLong())).thenReturn(java.util.Optional.of(updatedOrderTable));
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(updatedOrderTable);

        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(테이블_ID_1, request);

        assertThat(orderTableResponse.getId()).isEqualTo(테이블_ID_1);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(테이블_사람_2명);
        assertThat(orderTableResponse.getEmpty()).isEqualTo(테이블_비어있지않음);
    }

    @DisplayName("예외 테스트 : OrderTable의 고객 수 변경을 요청 시, 요청이 Null이면 예외가 발생한다.")
    @Test
    void changeNumberOfGuestWithNullExceptionTest() {
        OrderTableChangeNumberOfGuestsRequest request = null;

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블_ID_1, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 테이블 인원 변경 요청이 전달되었습니다.");
    }

    @DisplayName("예외 테스트 : OrderTable의 고객 수 변경을 요청 시, 고객수가 0 미만이면 예외가 발생한다.")
    @ValueSource(ints = {-1000, 0})
    @ParameterizedTest
    void changeNumberOfGuestWithNegativeNumberExceptionTest(int invalidNumberOfGuests) {
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(invalidNumberOfGuests);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블_ID_1, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 인원이 입력되었습니다.");
    }

    @DisplayName("예외 테스트 : OrderTable의 고객 수 변경을 요청 시, 잘못된 ID가 전달되면 예외가 발생한다.")
    @Test
    void changeNumberOfGuestWithInvalidIdExceptionTest() {
        Long invalidOrderTableId = -1L;
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(테이블_사람_2명);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테이블을 찾을 수 없습니다.");
    }
}
