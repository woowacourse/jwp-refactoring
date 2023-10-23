package kitchenpos.application;

import static kitchenpos.application.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.table.TableEmptyChangeRequest;
import kitchenpos.application.dto.table.TableGuestChangeRequest;
import kitchenpos.application.dto.table.TableRequest;
import kitchenpos.application.dto.table.TableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("새 주문 테이블을 저장한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = createOrderTable(1L, 3, false);
        final TableRequest tableRequest = new TableRequest(3, false);

        given(orderTableDao.save(any(OrderTable.class)))
            .willReturn(orderTable);

        // when
        final TableResponse savedOrderTable = tableService.create(tableRequest);

        // then
        assertThat(savedOrderTable).usingRecursiveComparison()
            .isEqualTo(TableResponse.from(orderTable));
    }

    @DisplayName("모든 주문 테이블을 조회한다.")
    @Test
    void list() {
        // given
        final OrderTable orderTable1 = createOrderTable(1L, 3);
        final OrderTable orderTable2 = createOrderTable(2L, 3);

        given(orderTableDao.findAll())
            .willReturn(List.of(orderTable1, orderTable2));

        // when
        final List<TableResponse> list = tableService.list();

        // then
        assertThat(list).usingRecursiveComparison()
            .isEqualTo(List.of(TableResponse.from(orderTable1), TableResponse.from(orderTable2)));
    }

    @DisplayName("주문 테이블을 비어있도록 변경한다.")
    @Test
    void changeEmpty_success() {
        // given
        final OrderTable orderTableToChange = createOrderTable(1L, 3, false);
        final TableEmptyChangeRequest request = new TableEmptyChangeRequest(true);

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(orderTableToChange));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
            .willReturn(false);

        final OrderTable expectedOrderTable = createOrderTable(1L, 3, true);

        given(orderTableDao.save(any(OrderTable.class)))
            .willReturn(expectedOrderTable);

        // when
        final TableResponse result = tableService.changeEmpty(orderTableToChange.getId(), request);

        // then
        assertThat(result).usingRecursiveComparison()
            .isEqualTo(TableResponse.from(expectedOrderTable));
    }

    @DisplayName("존재하지 않는 주문 테이블을 변경 요청하면 예외가 발생한다.")
    @Test
    void changeEmpty_notExistTableId_fail() {
        // given
        final TableEmptyChangeRequest request = new TableEmptyChangeRequest(true);

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 그룹에 속해있다면 예외가 발생한다.")
    @Test
    void changeEmpty_tableInGroup_fail() {
        // given
        final OrderTable savedOrderTable = createOrderTable(1L, 1L, 3);
        final Long tableId = savedOrderTable.getId();
        final TableEmptyChangeRequest request = new TableEmptyChangeRequest(true);

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(tableId, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태가 COMPLETION 이 아니라면 예외가 발생한다.")
    @Test
    void changeEmpty_tableNotComplete_fail() {
        // given
        final OrderTable savedOrderTable = createOrderTable(1L, 3);
        final TableEmptyChangeRequest request = new TableEmptyChangeRequest(true);

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
            .willReturn(true);

        // when, then
        final Long tableId = savedOrderTable.getId();
        assertThatThrownBy(() -> tableService.changeEmpty(tableId, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests_success() {
        // given
        final OrderTable prevOrderTable = createOrderTable(1L, 2);
        final TableGuestChangeRequest request = new TableGuestChangeRequest(3);
        final OrderTable expected = createOrderTable(1L, 3);

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(prevOrderTable));
        given(orderTableDao.save(prevOrderTable))
            .willReturn(expected);

        // when
        final TableResponse result = tableService.changeNumberOfGuests(prevOrderTable.getId(),
            request);

        // then
        assertThat(result).usingRecursiveComparison()
            .isEqualTo(TableResponse.from(expected));
    }

    @DisplayName("주문 테이블의 변경하려는 손님 수가 0보다 작으면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_wrongGuest_fail() {
        // given
        final TableGuestChangeRequest request = new TableGuestChangeRequest(-1);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경하려는 주문 테이블이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_notExistTable_fail() {
        // given
        final TableGuestChangeRequest request = new TableGuestChangeRequest(3);

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경하려는 주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_empty_fail() {
        // given
        final OrderTable prevOrderTable = createOrderTable(1L, 3, true);
        final Long orderTableId = prevOrderTable.getId();
        final TableGuestChangeRequest request = new TableGuestChangeRequest(3);

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(prevOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
