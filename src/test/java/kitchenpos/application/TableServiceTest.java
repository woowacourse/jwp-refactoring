package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.factory.OrderTableFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.util.ReflectionTestUtils;

@MockitoSettings
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("모든 OrderTable 을 조회한다")
    @Test
    void list() {
        // given // when
        tableService.list();

        // then
        verify(orderTableRepository, times(1)).findAll();
    }

    @Nested
    class CreateTest {

        private Long savedOrderTableId;

        private OrderTable savedOrderTable;

        private OrderTableRequest orderTableRequest;

        @BeforeEach
        void setUp() {
            OrderTable orderTable = OrderTableFactory.builder()
                .numberOfGuests(0)
                .empty(true)
                .build();

            savedOrderTableId = 1L;

            savedOrderTable = OrderTableFactory.copy(orderTable)
                .id(savedOrderTableId)
                .tableGroupId(null)
                .build();

            orderTableRequest = OrderTableFactory.dto(orderTable);
        }

        @DisplayName("OrderTable 을 생성한다")
        @Test
        void create() {
            // given
            given(orderTableRepository.save(any(OrderTable.class))).willAnswer(
                invocation -> {
                    OrderTable toSave = invocation.getArgument(0);
                    ReflectionTestUtils.setField(toSave, "id", savedOrderTableId);
                    return null;
                }
            );

            // when
            OrderTableResponse result = tableService.create(orderTableRequest);

            // then
            assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(savedOrderTable);
        }
    }

    @Nested
    class ModifyTest {

        private OrderTable orderTable;

        private Long orderTableId;

        private OrderTable savedOrderTable;

        private List<OrderStatus> notCompletionOrderStatuses;

        private OrderTableRequest orderTableRequest;

        @BeforeEach
        void setUp() {
            orderTable = OrderTableFactory.builder()
                .id(1L)
                .numberOfGuests(2)
                .tableGroupId(null)
                .empty(false)
                .build();

            orderTableId = orderTable.getId();

            savedOrderTable = OrderTableFactory.copy(orderTable)
                .build();

            notCompletionOrderStatuses = Arrays.asList(
                OrderStatus.COOKING,
                OrderStatus.MEAL
            );

            orderTableRequest = OrderTableFactory.dto(orderTable);
        }

        @DisplayName("OrderTable 의 empty 필드를 변경한다")
        @Test
        void changeEmpty() {
            // given
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));
            given(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId,
                notCompletionOrderStatuses
            )).willReturn(false);

            // when
            OrderTableResponse result = tableService.changeEmpty(orderTableId, orderTableRequest);

            // then
            assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(savedOrderTable);
        }

        @DisplayName("OrderTable 의 empty 필드 변경 실패한다 - orderTable 이 존재하지 않는 경우")
        @Test
        void changeEmpty_whenOrderTableDoesNotExist() {
            // given
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.empty());

            // when // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTableRequest))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTable 의 empty 필드 변경 실패한다 - tableGroup 이 존재하는 경우")
        @Test
        void changeEmpty_whenTableGroupExists() {
            // given
            savedOrderTable = OrderTableFactory.builder()
                .tableGroupId(1L)
                .build();
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));

            // when // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTableRequest))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTable 의 empty 필드 변경 실패한다 - order 의 상태가 COMPLETION 이 아닌 경우")
        @Test
        void changeEmpty_whenOrderStatusIsNotCompletion() {
            // given
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));
            given(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId,
                notCompletionOrderStatuses
            )).willReturn(true);

            // when // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTableRequest))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTable 의 numberOfGuest 필드를 변경한다")
        @Test
        void changeNumberOfGuests() {
            // given
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));

            // when
            OrderTableResponse result =
                tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

            // then
            assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(savedOrderTable);
        }

        @DisplayName("OrderTable 의 numberOfGuest 필드 변경 실패한다 - numberOfGuests 가 음수인 경우")
        @Test
        void changeNumberOfGuests_whenNumberOfGuestIsNegative() {
            // given
            orderTable = OrderTableFactory.copy(orderTable)
                .numberOfGuests(-1)
                .build();

            // when
            final ThrowingCallable throwingCallable =
                () -> tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTable 의 numberOfGuest 필드 변경 실패한다 - 저장된 orderTable 이 존재하지 않는 경우")
        @Test
        void changeNumberOfGuests_whenOrderTableDoesNotExist() {
            // given
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.empty());

            // when
            final ThrowingCallable throwingCallable =
                () -> tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTable 의 numberOfGuest 필드 변경 실패한다 - 저장된 orderTable 이 비어있는 경우")
        @Test
        void changeNumberOfGuests_whenSavedOrderTableIsEmpty() {
            // given
            savedOrderTable = OrderTableFactory.builder()
                .empty(true)
                .build();
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));

            // when
            final ThrowingCallable throwingCallable =
                () -> tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }
}
