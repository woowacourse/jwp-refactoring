package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.ui.request.ChangeTableEmptyRequest;
import kitchenpos.table.ui.request.ChangeTableGuestRequest;
import kitchenpos.table.ui.request.CreateTableRequest;
import kitchenpos.table.ui.response.TableResponse;
import kitchenpos.table.application.TableService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.fixture.OrderTableFixture.*;
import static kitchenpos.fixture.TableGroupFixture.GROUP1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@DisplayName("TableService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableValidator tableValidator;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블을 생성할 수 있다. - 아무 그룹에도 속해있지 않아야한다.")
    void create() {
        // given
        CreateTableRequest request = new CreateTableRequest(0, true);
        OrderTable expected = new OrderTable(1L, null, 0, true);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(expected);

        // when
        TableResponse actual = tableService.create(request);

        // then
        assertNotNull(actual);
        assertNull(actual.getTableGroupId());
    }

    @Test
    @DisplayName("전체 테이블을 조회할 수 있다.")
    void list() {
        // given
        OrderTable table1 = new OrderTable(1L, GROUP1, 5, true);
        OrderTable table2 = new OrderTable(2L, null, 0, true);
        List<OrderTable> expected = Arrays.asList(table1, table2);
        given(orderTableRepository.findAll()).willReturn(expected);

        // when
        List<TableResponse> actual = tableService.list();

        // then
        assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("테이블이 비어있는지를 나타내는 상태를 수정할 수 있다.")
    void changeEmpty() {
        // given
        ChangeTableEmptyRequest request = new ChangeTableEmptyRequest(true);
        OrderTable table = new OrderTable(4L, null, 2, false);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(table));

        // when
        TableResponse actual = tableService.changeEmpty(table.getId(), request);

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("빈 상태를 수정하려면 테이블은 존재해야 한다.")
    void changeEmptyWrongNotExist() {
        // given
        ChangeTableEmptyRequest request = new ChangeTableEmptyRequest(false);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeEmpty(1L, request));
        assertEquals("주문 테이블이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("빈 상태를 수정하려면 테이블은 그룹에 속해있지 않아야한다.")
    void changeEmptyWrongNotInGroup() {
        // given
        ChangeTableEmptyRequest request = new ChangeTableEmptyRequest(false);
        OrderTable table = 그룹1_손님2_테이블;
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(table));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeEmpty(table.getTableGroup().getId(), request));
        assertEquals("주문 테이블이 그룹에 속해있습니다. 그룹을 해제해주세요.", exception.getMessage());
    }

    @Test
    @DisplayName("빈 상태를 수정하려면 테이블이 조리중(COOKING)이나 식사중(MEAL)이 아니어야한다.")
    void changeEmptyWrongCookingOrMeal() {
        // given
        OrderTable table = new OrderTable(
                5L,
                null,
                2,
                false
        );
        ChangeTableEmptyRequest request = new ChangeTableEmptyRequest(false);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(table));
        doThrow(new IllegalArgumentException("주문 상태가 조리중이나 식사중입니다.")).when(tableValidator).validateOrder(any());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeEmpty(table.getId(), request));
        assertEquals("주문 상태가 조리중이나 식사중입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        OrderTable table = 단일_손님2_테이블;
        ChangeTableGuestRequest request = new ChangeTableGuestRequest(5);
        given(orderTableRepository.findById(table.getId())).willReturn(Optional.of(table));

        // when
        TableResponse actual = tableService.changeNumberOfGuests(table.getId(), request);

        // then
        assertEquals(5, actual.getNumberOfGuests());
    }

    @Test
    @DisplayName("손님의 수를 변경하려면 변경하려는 손님의 수는 0 이상이어야 한다.")
    void changeNumberOfGuestsWrongNumber() {
        // given
        OrderTable table = 단일_손님2_테이블;
        ChangeTableGuestRequest request = new ChangeTableGuestRequest(-1);
        given(orderTableRepository.findById(table.getId())).willReturn(Optional.of(table));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(table.getId(), request));
        assertEquals("변경하려는 손님 수는 0이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("손님의 수를 변경하려면 테이블은 존재해야 한다.")
    void changeNumberOfGuestsWrongTableNotExist() {
        // given
        ChangeTableGuestRequest request = new ChangeTableGuestRequest(5);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(1L, request));
        assertEquals("주문 테이블이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("손님의 수를 변경하려면 테이블은 비어있지 않아야한다.")
    void changeNumberOfGuestsWrongTableEmpty() {
        // given
        OrderTable table = 단일_손님0_테이블1;
        ChangeTableGuestRequest request = new ChangeTableGuestRequest(5);
        given(orderTableRepository.findById(table.getId())).willReturn(Optional.of(table));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(table.getId(), request));
        assertEquals("비어있는 테이블의 손님 수를 변경할 수 없습니다.", exception.getMessage());
    }
}
