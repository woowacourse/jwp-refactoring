package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

@ServiceTest
class TableServiceTest {

    @Mock
    private OrderDao mockOrderDao;

    @Mock
    private OrderTableDao mockOrderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = createOrderTable(1L, null, false);
        when(mockOrderTableDao.save(any())).then(AdditionalAnswers.returnsFirstArg());
    }

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        tableService.create(orderTable);
        verify(mockOrderTableDao).save(orderTable);
    }

    @DisplayName("테이블 목록을 반환한다.")
    @Test
    void list() {
        when(mockOrderTableDao.findAll()).thenReturn(Collections.singletonList(orderTable));
        List<OrderTable> list = tableService.list();
        assertAll(
                () -> assertThat(list).hasSize(1),
                () -> assertThat(list).contains(orderTable)
        );
    }

    @DisplayName("테이블의 empty 상태를 변경한다.")
    @Test
    void changeEmpty() {
        when(mockOrderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(mockOrderDao.existsByOrderTableIdAndOrderStatusIn(
                any(), Mockito.eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
        ).thenReturn(false);

        OrderTable updateOrderTable = createOrderTable(true);
        tableService.changeEmpty(orderTable.getId(), updateOrderTable);

        ArgumentCaptor<OrderTable> argument = ArgumentCaptor.forClass(OrderTable.class);
        verify(mockOrderTableDao).save(argument.capture());
        assertThat(argument.getValue().isEmpty()).isEqualTo(updateOrderTable.isEmpty());
    }

    @DisplayName("조리중이거나, 식사 중 상태의 테이블의 empty 상태를 변경할 수 없다.")
    @Test
    void changeEmptyWithInvalidOrderStatus() {
        when(mockOrderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(mockOrderDao.existsByOrderTableIdAndOrderStatusIn(
                any(), Mockito.eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
        ).thenReturn(true);
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), createOrderTable()));
    }

    @DisplayName("그룹 지정이 되어있는 테이블의 empty 상태를 변경할 수 없다.")
    @Test
    void changeEmptyWithGroupedTable() {
        orderTable.setTableGroupId(1L);
        when(mockOrderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(mockOrderDao.existsByOrderTableIdAndOrderStatusIn(
                any(), Mockito.eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
        ).thenReturn(false);
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), createOrderTable()));
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        when(mockOrderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        OrderTable updateOrderTable = createOrderTable();
        tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable);

        ArgumentCaptor<OrderTable> argument = ArgumentCaptor.forClass(OrderTable.class);
        verify(mockOrderTableDao).save(argument.capture());
        assertThat(argument.getValue().getNumberOfGuests()).isEqualTo(updateOrderTable.getNumberOfGuests());
    }

    @DisplayName("테이블의 손님 수를 음수로 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsWithInvalid() {
        when(mockOrderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), createOrderTable(-1)));
    }

    @DisplayName("비어있는 테이블의 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestWithEmptyTable() {
        orderTable.setEmpty(true);
        when(mockOrderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), createOrderTable()));
    }
}
