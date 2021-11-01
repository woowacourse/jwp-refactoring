package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.TableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = createOrderTable();
        when(orderTableDao.save(any())).thenReturn(createOrderTable(1L));

        assertDoesNotThrow(() -> tableService.create(orderTable));
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        OrderTable orderTable1 = createOrderTable();
        OrderTable orderTable2 = createOrderTable();

        when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable1, orderTable2));

        List<OrderTable> actual = tableService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(orderTable1, orderTable2)
        );
    }

    @DisplayName("빈 테이블로의 변경은")
    @Nested
    class ChangeEmpty {

        private final Long orderTableId = 1L;
        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            orderTable = createOrderTable(orderTableId);
        }

        void subject() {
            tableService.changeEmpty(orderTableId, orderTable);
        }

        @DisplayName("존재하지 않는 주문 테이블은 변경할 수 없다.")
        @Test
        void changeEmptyIfNotExist() {
            OrderTable orderTable = createOrderTable();
            when(orderTableDao.findById(any())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정에 속해있는 주문 테이블은 변경할 수 없다.")
        @Test
        void changeEmptyExceptionIfInTableGroup() {
            orderTable.setTableGroupId(1L);
            when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조리, 식사 주문 상태를 가진 주문이 있는 주문 테이블은 변경할 수 없다.")
        @Test
        void changeEmptyExceptionIfHasStatus() {
            when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
            when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                    orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족하는 경우 변경할 수 있다.")
        @Test
        void changeEmpty() {
            orderTable.setEmpty(true);
            when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
            when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                    orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
            when(orderTableDao.save(any())).thenReturn(orderTable);

            assertDoesNotThrow(this::subject);
        }
    }

    @DisplayName("주문 테이블의 방문한 손님 수 변경은")
    @Nested
    class ChangeNumber {

        private final Long orderTableId = 1L;
        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            orderTable = createOrderTable(orderTableId);
        }

        void subject() {
            tableService.changeNumberOfGuests(orderTableId, orderTable);
        }

        @DisplayName("0 미만으로 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsExceptionIfLessThanZero() {
            orderTable.setNumberOfGuests(-1);

            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 주문 테이블일 경우 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsExceptionIfNotExist() {
            orderTable = createOrderTable();
            when(orderTableDao.findById(any())).thenReturn(Optional.empty());

            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블일 경우 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsExceptionIfEmpty() {
            orderTable.setEmpty(true);
            when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족하는 경우 변경할 수 있다.")
        @Test
        void changeNumberOfGuests() {
            orderTable.setNumberOfGuests(10);
            when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
            when(orderTableDao.save(any())).thenReturn(orderTable);

            assertDoesNotThrow(this::subject);
        }
    }
}
