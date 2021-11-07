package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ServiceTest
class TableServiceTest {

    @Mock
    private OrderDao mockOrderDao;

    @Mock
    private OrderTableDao mockOrderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        OrderTableRequest orderTableRequest = createOrderTableRequest();
        when(mockOrderTableDao.save(any())).thenReturn(orderTableRequest.toEntity(1L));
        OrderTableResponse savedOrderTable = tableService.create(orderTableRequest);
        assertAll(
                () -> assertThat(savedOrderTable).isNotNull(),
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable.getTableGroupId()).isEqualTo(orderTableRequest.getTableGroupId()),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests()),
                () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTableRequest.isEmpty())
        );
    }

    @DisplayName("테이블 목록을 반환한다.")
    @Test
    void list() {
        List<OrderTable> tables = Arrays.asList(createOrderTable(1L), createOrderTable(2L));
        when(mockOrderTableDao.findAll()).thenReturn(tables);
        List<OrderTableResponse> list = tableService.list();
        assertAll(
                () -> assertThat(list).hasSize(tables.size()),
                () -> assertThat(list).usingRecursiveComparison().isEqualTo(OrderTableResponse.listOf(tables))
        );
    }

    @DisplayName("테이블의 Empty 상태 변경")
    @Nested
    class ChangeEmptyStatus {

        @Captor
        private ArgumentCaptor<OrderTable> argument;
        private OrderTable savedOrderTable;

        @BeforeEach
        void setUp() {
            savedOrderTable = createOrderTable(1L, null, true);
            when(mockOrderTableDao.findById(savedOrderTable.getId())).thenReturn(Optional.of(savedOrderTable));
            when(mockOrderDao.existsByOrderTableIdAndOrderStatusIn(
                    savedOrderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
            ).thenReturn(false);
        }

        @DisplayName("테이블의 empty 상태를 변경한다.")
        @Test
        void changeEmpty() {
            OrderTableRequest orderTableRequest = createOrderTableRequest(false);
            when(mockOrderTableDao.save(any())).thenReturn(orderTableRequest.toEntity(savedOrderTable.getId()));
            tableService.changeEmpty(savedOrderTable.getId(), orderTableRequest);

            verify(mockOrderTableDao).save(argument.capture());
            assertThat(argument.getValue().isEmpty()).isEqualTo(savedOrderTable.isEmpty());
        }

        @DisplayName("조리중이거나, 식사 중 상태의 테이블의 empty 상태를 변경할 수 없다.")
        @Test
        void changeEmptyWithInvalidOrderStatus() {
            when(mockOrderDao.existsByOrderTableIdAndOrderStatusIn(
                    savedOrderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
            ).thenReturn(true);
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), createOrderTableRequest()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("그룹 지정이 되어있는 테이블의 empty 상태를 변경할 수 없다.")
        @Test
        void changeEmptyWithGroupedTable() {
            OrderTable savedOrderTable = createOrderTable(1L, 1L, false);
            when(mockOrderTableDao.findById(savedOrderTable.getId())).thenReturn(Optional.of(savedOrderTable));
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), createOrderTableRequest()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("테이블의 손님 수 변경")
    @Nested
    class ChangeNumberOfGuests {

        @Captor
        ArgumentCaptor<OrderTable> argument;
        OrderTable savedOrderTable;

        @BeforeEach
        void setUp() {
            savedOrderTable = createOrderTable(1);
            when(mockOrderTableDao.findById(savedOrderTable.getId())).thenReturn(Optional.of(savedOrderTable));
        }

        @DisplayName("테이블의 손님 수를 변경한다.")
        @Test
        void changeNumberOfGuests() {
            OrderTableRequest orderTableRequest = createOrderTableRequest(2);
            when(mockOrderTableDao.save(any())).thenReturn(orderTableRequest.toEntity(savedOrderTable.getId()));
            tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableRequest);

            verify(mockOrderTableDao).save(argument.capture());
            assertThat(argument.getValue().getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
        }

        @DisplayName("테이블의 손님 수를 음수로 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsWithInvalid() {
            OrderTableRequest orderTableRequest =  createOrderTableRequest(-1);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("비어있는 테이블의 손님 수를 변경할 수 없다.")
        @Test
        void changeNumberOfGuestWithEmptyTable() {
            savedOrderTable = createOrderTable(true);
            when(mockOrderTableDao.findById(savedOrderTable.getId())).thenReturn(Optional.of(savedOrderTable));
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTableRequest()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
