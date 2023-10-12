package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kitchenpos.application.exception.TableServiceException.EmptyTableException;
import kitchenpos.application.exception.TableServiceException.ExistsNotCompletionOrderException;
import kitchenpos.application.exception.TableServiceException.ExistsTableGroupException;
import kitchenpos.application.exception.TableServiceException.InvalidNumberOfGuestsException;
import kitchenpos.application.exception.TableServiceException.NotExistsOrderTableException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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

    private OrderTable orderTable = new OrderTable();

    @Test
    @DisplayName("주문 테이블을 생성할 수 있다. 이때 id와 tableGroupId은 null로 초기화된다.")
    void create_success() {
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        when(orderTableDao.save(orderTable)).thenReturn(orderTable); // 동일한 객체 전달해 메소드 내에서 값이 변경됐는지 확인

        OrderTable actual = tableService.create(orderTable);

        assertAll(
                () -> assertThat(actual.getId()).isNull(),
                () -> assertThat(actual.getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("현재 저장된 주문 테이블을 확인할 수 있다.")
    void list_success() {
        tableService.list();

        verify(orderTableDao, times(1)).findAll();
    }

    @ParameterizedTest
    @CsvSource(value = {"true,true", "true,false", "false,true", "false,false"})
    @DisplayName("현재 주문 테이블을 빈 테이블이나 빈 테이블이 아닌 상태로 변경할 수 있다.")
    void changeEmpty_success(boolean beforeEmpty, boolean changedEmpty) {
        orderTable.setEmpty(beforeEmpty);
        orderTable.setTableGroupId(null);
        long orderTableId = 1L;
        OrderTable changeEmptyOrderTable = new OrderTable();
        changeEmptyOrderTable.setEmpty(changedEmpty);

        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.ofNullable(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).thenReturn(false);

        tableService.changeEmpty(orderTableId, changeEmptyOrderTable);

        boolean actualEmpty = orderTable.isEmpty();

        assertThat(actualEmpty).isEqualTo(changedEmpty);
    }

    @Test
    @DisplayName("주문 테이블이 db에 저장되어있지 않으면 예외가 발생한다.")
    void changeEmpty_fail_no_order_table() {
        OrderTable changeEmptyOrderTable = new OrderTable();

        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty()); // db에 저장 x

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeEmptyOrderTable))
                .isInstanceOf(NotExistsOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블이 어떤 테이블 그룹에 포함되어 있으면 예외가 발생한다.")
    void changeEmpty_fail_exists_tableGroup() {
        orderTable.setTableGroupId(1L); // 테이블 그룹에 포함
        OrderTable changeEmptyOrderTable = new OrderTable();

        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeEmptyOrderTable))
                .isInstanceOf(ExistsTableGroupException.class);
    }

    @Test
    @DisplayName("주문 테이블에 매핑된 주문이 COOKING 상태이거나 MEAL 상태라면 예외가 발생한다.")
    void changeEmpty_fail_not_completion() {
        orderTable.setTableGroupId(null);
        OrderTable changeEmptyOrderTable = new OrderTable();

        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).thenReturn(
                true); // cooking 상태이거나 meal 상태

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeEmptyOrderTable))
                .isInstanceOf(ExistsNotCompletionOrderException.class);
    }

    @Test
    @DisplayName("방문한 손님 수를 바꿀 수 있다.")
    void changeNumberOfGuests_success() {
        orderTable.setEmpty(false);
        OrderTable changeNumberOrderTable = new OrderTable();
        int changeNumberOfGuests = 10;
        changeNumberOrderTable.setNumberOfGuests(changeNumberOfGuests);

        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));

        int beforeNumberOfGuests = orderTable.getNumberOfGuests();

        tableService.changeNumberOfGuests(orderTable.getId(), changeNumberOrderTable);

        int afterNumberOfGuests = orderTable.getNumberOfGuests();

        assertAll(
                () -> assertThat(beforeNumberOfGuests).isZero(),
                () -> assertThat(afterNumberOfGuests).isEqualTo(changeNumberOfGuests)
        );
    }

    @Test
    @DisplayName("방문한 손님 수가 0명 미만인 경우 예외가 발생한다.")
    void changeNumberOfGuests_fail_number_of_guest_less_than_zero() {
        OrderTable changeNumberOrderTable = new OrderTable();
        changeNumberOrderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeNumberOrderTable))
                .isInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @Test
    @DisplayName("주문 테이블이 db에 저장되어있지 않으면 예외가 발생한다")
    void changeNumberOfGuests_fail_no_order_table() {
        OrderTable changeNumberOrderTable = new OrderTable();
        changeNumberOrderTable.setNumberOfGuests(10);

        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeNumberOrderTable))
                .isInstanceOf(NotExistsOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
    void changeNumberOfGuests_fail_empty_table() {
        orderTable.setEmpty(true);
        OrderTable changeNumberOrderTable = new OrderTable();
        changeNumberOrderTable.setNumberOfGuests(10);

        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeNumberOrderTable))
                .isInstanceOf(EmptyTableException.class);
    }
}
