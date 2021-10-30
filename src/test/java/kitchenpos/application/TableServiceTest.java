package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.factory.OrderTableFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("모든 OrderTable 을 조회한다")
    @Test
    void list() {
        // given // when
        tableService.list();

        // then
        verify(orderTableDao, times(1)).findAll();
    }

    @Nested
    class CreateTest {

        OrderTable orderTable;

        OrderTable savedOrderTable;

        @BeforeEach
        void setUp() {
            orderTable = OrderTableFactory.builder()
                    .numberOfGuests(0)
                    .empty(true)
                    .build();

            savedOrderTable = OrderTableFactory.copy(orderTable)
                    .id(1L)
                    .tableGroupId(null)
                    .build();
        }

        @DisplayName("OrderTable 을 생성한다")
        @Test
        void create() {
            // given
            when(orderTableDao.save(orderTable)).thenReturn(savedOrderTable);

            // when
            OrderTable result = tableService.create(orderTable);

            // then
            verify(orderTableDao, times(1)).save(orderTable);
            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(savedOrderTable);
        }
    }

    @Nested
    class ModifyTest {

        OrderTable orderTable;
        Long orderTableId;

        OrderTable savedOrderTable;

        List<String> notCompletionOrderStatuses;

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
                    OrderStatus.COOKING.name(),
                    OrderStatus.MEAL.name()
            );

        }

        @DisplayName("OrderTable 의 empty 필드를 변경한다")
        @Test
        void changeEmpty() {
            // given
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));
            when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                    orderTableId,
                    notCompletionOrderStatuses
            )).thenReturn(false);
            when(orderTableDao.save(savedOrderTable)).thenReturn(savedOrderTable);

            // when
            OrderTable result = tableService.changeEmpty(orderTableId, orderTable);

            // then
            verify(orderTableDao, times(1)).findById(orderTableId);
            verify(orderDao, times(1)).existsByOrderTableIdAndOrderStatusIn(
                    orderTableId,
                    notCompletionOrderStatuses
            );
            verify(orderTableDao, times(1)).save(savedOrderTable);
            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(savedOrderTable);
        }

        @DisplayName("OrderTable 의 empty 필드 변경 실패한다 - orderTable 이 존재하지 않는 경우")
        @Test
        void changeEmpty_whenOrderTableDoesNotExist() {
            // given
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.empty());

            // when // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
            verify(orderTableDao, times(1)).findById(orderTableId);
        }

        @DisplayName("OrderTable 의 empty 필드 변경 실패한다 - tableGroup 이 존재하는 경우")
        @Test
        void changeEmpty_whenTableGroupExists() {
            // given
            savedOrderTable = OrderTableFactory.builder()
                    .tableGroupId(1L)
                    .build();
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));

            // when // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
            verify(orderTableDao, times(1)).findById(orderTableId);
        }

        @DisplayName("OrderTable 의 empty 필드 변경 실패한다 - order 의 상태가 COMPLETION 이 아닌 경우")
        @Test
        void changeEmpty_whenOrderStatusIsNotCompletion() {
            // given
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));
            when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                    orderTableId,
                    notCompletionOrderStatuses
            )).thenReturn(true);

            // when // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
            verify(orderTableDao, times(1)).findById(orderTableId);
            verify(orderDao, times(1)).existsByOrderTableIdAndOrderStatusIn(
                    orderTableId,
                    notCompletionOrderStatuses
            );
        }

        @DisplayName("OrderTable 의 numberOfGuest 필드를 변경한다")
        @Test
        void changeNumberOfGuests() {
            // given
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));
            when(orderTableDao.save(savedOrderTable)).thenReturn(savedOrderTable);

            // when
            OrderTable result = tableService.changeNumberOfGuests(orderTableId, orderTable);

            // then
            verify(orderTableDao, times(1)).findById(orderTableId);
            verify(orderTableDao, times(1)).save(savedOrderTable);
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

            // when // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTable 의 numberOfGuest 필드 변경 실패한다 - 저장된 orderTable 이 존재하지 않는 경우")
        @Test
        void changeNumberOfGuests_whenOrderTableDoesNotExist() {
            // given
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.empty());

            // when // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
            verify(orderTableDao, times(1)).findById(orderTableId);
        }

        @DisplayName("OrderTable 의 numberOfGuest 필드 변경 실패한다 - 저장된 orderTable 이 비어있는 경우")
        @Test
        void changeNumberOfGuests_whenSavedOrderTableIsEmpty() {
            // given
            savedOrderTable = OrderTableFactory.builder()
                    .empty(true)
                    .build();
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));

            // when // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
            verify(orderTableDao, times(1)).findById(orderTableId);
        }
    }
}
