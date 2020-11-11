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

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.fixture.OrderTableFixture;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("테이블을 정상적으로 생성한다.")
    @Test
    void create() {
        OrderTableRequest createDto = OrderTableFixture.createRequest();
        OrderTable withId = OrderTableFixture.createEmptyWithId(OrderTableFixture.ID1);

        when(orderTableDao.save(any(OrderTable.class))).thenReturn(withId);
        OrderTable saved = tableService.create(createDto);

        assertThat(saved).isEqualToComparingFieldByField(withId);
    }

    @DisplayName("모든 테이블을 조회한다.")
    @Test
    void list() {
        OrderTable table1 = OrderTableFixture.createEmptyWithId(1L);
        OrderTable table2 = OrderTableFixture.createEmptyWithId(2L);

        List<OrderTable> tables = Lists.newArrayList(table1, table2);
        when(orderTableDao.findAll()).thenReturn(tables);

        List<OrderTableResponse> actual = tableService.list();
        assertThat(actual).usingRecursiveComparison().isEqualTo(tables);
    }

    @DisplayName("Empty상태를 변경한다.")
    @Test
    void changeEmpty() {
        OrderTable emptyTable = OrderTableFixture.createEmptyWithId(1L);
        OrderTableRequest changeEmptyRequest = OrderTableFixture.createRequestEmptyOf(true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(emptyTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(emptyTable);
        OrderTableResponse response = tableService.changeEmpty(emptyTable.getId(),
            changeEmptyRequest);

        assertThat(response.isEmpty()).isTrue();
    }

    @DisplayName("Order Table이 존재하지 않는 경우 Empty 변경 요청 시 예외를 반환한다.")
    @Test
    void changeEmptyNotFound() {
        OrderTable orderTable = OrderTableFixture.createEmptyWithId(1L);
        OrderTableRequest request = OrderTableFixture.createRequest();
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order Table이 Table Group에 속해있다면 예외를 반환한다.")
    @Test
    void changeEmptyGroupIdNull() {
        OrderTable orderTable = OrderTableFixture.createGroupTableWithId(1L);
        OrderTableRequest request = OrderTableFixture.createRequest();
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("식사중이거나, 조리중인 경우에 빈 테이블로 만들 수 없다.")
    @Test
    void changeEmptyAlreadyDoingSomething() {
        OrderTable mealOrCookingTable = OrderTableFixture.createNotEmptyWithId(1L);
        OrderTableRequest request = OrderTableFixture.createRequest();
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(mealOrCookingTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        assertThatThrownBy(
            () -> tableService.changeEmpty(mealOrCookingTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님의 수를 수정한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable oneGuestTable = OrderTableFixture.createNotEmptyWithId(1L);
        OrderTable tenGuestTable = OrderTableFixture.createNumOf(1L, 10);
        OrderTableRequest tenGuestRequest = OrderTableFixture.createRequestNumOf(10);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(oneGuestTable));
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(tenGuestTable);
        OrderTableResponse response = tableService.changeNumberOfGuests(
            tenGuestTable.getId(), tenGuestRequest);

        assertThat(response).usingRecursiveComparison()
            .isEqualTo(OrderTableResponse.of(tenGuestTable));
    }

    @DisplayName("손님의 수가 음수인 경우 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsNegativeGuestNumber() {
        OrderTableRequest negativeRequest = OrderTableFixture.createRequestNumOf(-10);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(1L, negativeRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("해당하는 OrderTable이 없으면 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsNoOrderTable() {
        OrderTableRequest request = OrderTableFixture.createRequest();
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 비어있는 테이블에, 손님의 수를 추가할 수 없다.")
    @Test
    void changeNumberOfGuestsEmptyNumber() {
        OrderTable emptyTable = OrderTableFixture.createEmptyWithId(1L);
        OrderTableRequest request = OrderTableFixture.createRequest();
        when(orderTableDao.findById(emptyTable.getId())).thenReturn(Optional.of(emptyTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
