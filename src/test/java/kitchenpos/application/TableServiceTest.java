package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create() {
        //given
        OrderTable orderTable = new OrderTable.OrderTableBuilder()
                                    .setNumberOfGuests(4)
                                    .build();

        given(orderTableDao.save(orderTable))
                .willReturn(orderTable);
        //when
        OrderTable actual = tableService.create(orderTable);
        //then
        assertThat(actual).isEqualTo(orderTable);

        verify(orderTableDao, times(1)).save(orderTable);
    }

    @DisplayName("테이블 목록을 가져올 수 있다.")
    @Test
    void list() {
        //given
        OrderTable orderTable1 = new OrderTable.OrderTableBuilder()
                                    .setNumberOfGuests(4)
                                    .build();
        OrderTable orderTable2 = new OrderTable.OrderTableBuilder()
                                    .setNumberOfGuests(2)
                                    .build();

        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        given(orderTableDao.findAll())
                .willReturn(orderTables);
        //when
        List<OrderTable> actual = tableService.list();
        //then
        assertThat(actual).isEqualTo(orderTables);

        verify(orderTableDao, times(1)).findAll();
    }

    @DisplayName("테이블 상태를 빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() {
        //given
        Long orderTableId = 1L;
        OrderTable savedTable = new OrderTable.OrderTableBuilder()
                                    .setId(1L)
                                    .build();
        OrderTable changeTable = new OrderTable.OrderTableBuilder()
                                    .setEmpty(true)
                                    .build();

        given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(savedTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);
        given(orderTableDao.save(savedTable))
                .willReturn(savedTable);
        //when
        OrderTable actual = tableService.changeEmpty(orderTableId, changeTable);
        //then
        assertThat(actual).isEqualTo(savedTable);

        verify(orderTableDao, times(1)).findById(orderTableId);
        verify(orderDao, times(1)).existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
        verify(orderTableDao, times(1)).save(savedTable);
    }

    @DisplayName("테이블 상태 변경 실패 - 그룹 테이블 ID가 null이 아닌 경우")
    @Test
    void changeEmptyFailInvalidGroupTableID() {
        //given
        Long orderTableId = 1L;

        OrderTable savedTable = new OrderTable.OrderTableBuilder()
                                    .setId(1L)
                                    .setTableGroupId(1L)
                                    .build();
        OrderTable changeTable = new OrderTable.OrderTableBuilder()
                                    .setEmpty(true)
                                    .build();

        given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(savedTable));
        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, changeTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹 테이블 ID가 null이 아닙니다.");
    }

    @DisplayName("테이블 상태 변경 실패 - 주문 상태가 요리 또는 식사 상태인 경우")
    @Test
    void changeEmptyFailInvalidOrderStatus() {
        //given
        Long orderTableId = 1L;
        OrderTable savedTable = new OrderTable.OrderTableBuilder()
                                    .setId(1L)
                                    .build();
        OrderTable changeTable = new OrderTable.OrderTableBuilder()
                                    .setEmpty(true)
                                    .build();

        given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(savedTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);
        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, changeTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 요리 또는 식사 상태입니다.");
    }

    @DisplayName("테이블의 인원 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        //given
        Long orderTableId = 1L;
        OrderTable changeOrderTable = new OrderTable.OrderTableBuilder()
                                        .setNumberOfGuests(2)
                                        .build();
        OrderTable expectedOrderTable = new OrderTable();

        given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(expectedOrderTable));
        given(orderTableDao.save(expectedOrderTable))
                .willReturn(expectedOrderTable);
        //when
        OrderTable actual = tableService.changeNumberOfGuests(orderTableId, changeOrderTable);
        //then
        assertThat(actual).isEqualTo(expectedOrderTable);

        verify(orderTableDao, times(1)).findById(orderTableId);
        verify(orderTableDao, times(1)).save(expectedOrderTable);
    }

    @DisplayName("테이블의 인원 수 변경 실패 - 손님의 수가 음수일 경우")
    @Test
    void changeNumberOfGuestsFailInvalidGuestNumber() {
        //given
        Long orderTableId = 1L;
        OrderTable changeOrderTable = new OrderTable.OrderTableBuilder()
                                        .setNumberOfGuests(-2)
                                        .build();
        //when
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 수는 음수일 수 없습니다.");
    }

    @DisplayName("테이블의 인원 수 변경 실패 - 변경할 테이블 번호가 없을 경우")
    @Test
    void changeNumberOfGuestsFailInvalidTableNumber() {
        //given
        Long orderTableId = 10L;
        OrderTable changeOrderTable = new OrderTable.OrderTableBuilder()
                                        .setNumberOfGuests(2)
                                        .build();

        given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
    }

    @DisplayName("테이블의 인원 수 변경 실패 - 변경할 테이블이 빈 테이블일 경우")
    @Test
    void changeNumberOfGuestsFailEmptyTable() {
        //given
        Long orderTableId = 1L;
        OrderTable changeOrderTable = new OrderTable.OrderTableBuilder()
                                            .setNumberOfGuests(2)
                                            .build();
        OrderTable expectedOrderTable = new OrderTable.OrderTableBuilder()
                                            .setEmpty(true)
                                            .build();

        given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(expectedOrderTable));
        //when
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈테이블은 변경할 수 없습니다.");
    }
}