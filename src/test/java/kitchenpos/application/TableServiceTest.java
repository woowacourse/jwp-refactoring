package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.AlreadyEmptyTableException;
import kitchenpos.exception.AlreadyInTableGroupException;
import kitchenpos.exception.NegativeNumberOfGuestsException;
import kitchenpos.exception.OrderNotCompleteException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @DisplayName("테이블을 정상적으로 생성한다.")
    @Test
    void create() {
        OrderTableRequest createDto = OrderTableFixture.createRequest();
        OrderTable withId = OrderTableFixture.createEmptyWithId(OrderTableFixture.ID1);

        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(withId);

        OrderTableResponse saved = tableService.create(createDto);

        assertThat(saved).isEqualToComparingFieldByField(withId);
    }

    @DisplayName("모든 테이블을 조회한다.")
    @Test
    void list() {
        OrderTable table1 = OrderTableFixture.createEmptyWithId(1L);
        OrderTable table2 = OrderTableFixture.createEmptyWithId(2L);

        List<OrderTable> tables = Lists.newArrayList(table1, table2);
        when(orderTableRepository.findAll()).thenReturn(tables);

        List<OrderTableResponse> actual = tableService.list();
        assertThat(actual).usingRecursiveComparison().isEqualTo(tables);
    }

    @DisplayName("Empty상태를 변경한다.")
    @Test
    void changeEmpty() {
        OrderTable emptyTable = OrderTableFixture.createEmptyWithId(1L);
        OrderTableChangeEmptyRequest changeEmptyRequest = OrderTableFixture.createRequestEmptyOf(
            true);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(emptyTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(
            false);
        OrderTableResponse response = tableService.changeEmpty(emptyTable.getId(),
            changeEmptyRequest);

        assertThat(response.isEmpty()).isTrue();
    }

    @DisplayName("Order Table이 존재하지 않는 경우 Empty 변경 요청 시 예외를 반환한다.")
    @Test
    void changeEmptyNotFound() {
        OrderTable orderTable = OrderTableFixture.createEmptyWithId(1L);
        OrderTableChangeEmptyRequest request = OrderTableFixture.createRequestEmptyOf(true);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
            .isInstanceOf(OrderTableNotFoundException.class)
            .hasMessage(String.format("%d table is not exist!", orderTable.getId()));
    }

    @DisplayName("Order Table이 Table Group에 속해있다면 예외를 반환한다.")
    @Test
    void changeEmptyGroupIdNull() {
        OrderTable orderTable = OrderTableFixture.createGroupTableWithId(1L, 1L);
        OrderTableChangeEmptyRequest request = OrderTableFixture.createRequestEmptyOf(true);
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
            .isInstanceOf(AlreadyInTableGroupException.class)
            .hasMessage(String.format("%d table is already in table group %d",
                orderTable.getId(), orderTable.getTableGroupId()));
    }

    @DisplayName("식사중이거나, 조리중인 경우에 빈 테이블로 만들 수 없다.")
    @Test
    void changeEmptyAlreadyDoingSomething() {
        OrderTable mealOrCookingTable = OrderTableFixture.createNotEmptyWithId(1L);
        OrderTableChangeEmptyRequest request = OrderTableFixture.createRequestEmptyOf(true);
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(mealOrCookingTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(
            true);

        assertThatThrownBy(
            () -> tableService.changeEmpty(mealOrCookingTable.getId(), request))
            .isInstanceOf(OrderNotCompleteException.class)
            .hasMessage(
                String.format("Order of %d table is not completion yet. First, complete order.",
                    mealOrCookingTable.getId()));
    }

    @DisplayName("손님의 수를 수정한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable oneGuestTable = OrderTableFixture.createNotEmptyWithId(1L);
        OrderTable tenGuestTable = OrderTableFixture.createNumOf(1L, 10);
        OrderTableChangeNumberOfGuestsRequest request = OrderTableFixture.createRequestNumOf(
            10);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(oneGuestTable));
        OrderTableResponse response = tableService.changeNumberOfGuests(
            tenGuestTable.getId(), request);

        assertThat(response).usingRecursiveComparison()
            .isEqualTo(OrderTableResponse.of(tenGuestTable));
    }

    @DisplayName("손님의 수가 음수인 경우 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsNegativeGuestNumber() {
        OrderTableChangeNumberOfGuestsRequest negativeRequest = OrderTableFixture.createRequestNumOf(
            -10);
        OrderTable oneGuestTable = OrderTableFixture.createNotEmptyWithId(1L);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(oneGuestTable));

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(1L, negativeRequest))
            .isInstanceOf(NegativeNumberOfGuestsException.class)
            .hasMessage(String.format("%d Table cannot change number of guests to negative integer",
                oneGuestTable.getId()));
    }

    @DisplayName("해당하는 OrderTable이 없으면 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsNoOrderTable() {
        OrderTableChangeNumberOfGuestsRequest request = OrderTableFixture.createRequestNumOf(
            10);
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
            .isInstanceOf(OrderTableNotFoundException.class)
            .hasMessage(String.format("%d table is not exist!", 1L));
    }

    @DisplayName("이미 비어있는 테이블에, 손님의 수를 추가할 수 없다.")
    @Test
    void changeNumberOfGuestsEmptyNumber() {
        OrderTableChangeNumberOfGuestsRequest request = OrderTableFixture.createRequestNumOf(
            10);
        OrderTable emptyTable = OrderTableFixture.createEmptyWithId(1L);
        when(orderTableRepository.findById(emptyTable.getId())).thenReturn(Optional.of(emptyTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyTable.getId(), request))
            .isInstanceOf(AlreadyEmptyTableException.class)
            .hasMessage(
                String.format("%d Table can't change number of guests because it is already empty",
                    emptyTable.getId()));
    }
}
