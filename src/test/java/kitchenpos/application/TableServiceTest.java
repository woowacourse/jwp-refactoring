package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.TestFixture;

@ExtendWith(MockitoExtension.class)
class TableServiceTest extends TestFixture {

    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("테이블 생성 성공 테스트")
    @Test
    void createTest() {
        given(orderTableDao.save(any())).willReturn(ORDER_TABLE_1);

        OrderTable persistOrderTable = tableService.create(ORDER_TABLE_1);

        assertThat(persistOrderTable).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1);
    }

    @DisplayName("테이블 조회 성공 테스트")
    @Test
    void listTest() {
        given(orderTableDao.findAll()).willReturn(ORDER_TABLES);

        List<OrderTable> persistOrderTables = tableService.list();

        assertAll(
            () -> assertThat(persistOrderTables).hasSize(ORDER_TABLES.size()),
            () -> assertThat(persistOrderTables.get(0)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1),
            () -> assertThat(persistOrderTables.get(1)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_2)
        );
    }

    @DisplayName("테이블 empty 변경 예외 테스트: 테이블이 존재하지 않을 때")
    @Test
    void changeEmptyFailByNotExistOrderTable() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(ORDER_TABLE_ID_1, ORDER_TABLE_1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 empty 변경 예외 테스트: 테이블이 그룹이 이미 지정되어 있을 때")
    @Test
    void changeEmptyFailByAlreadyIncluded() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(ORDER_TABLE_1));

        assertThatThrownBy(() -> tableService.changeEmpty(ORDER_TABLE_ID_1, ORDER_TABLE_1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 empty 변경 예외 테스트: 아직 식사 중 또는 요리 중일 때")
    @Test
    void changeEmptyFailByNotCompleted() {
        OrderTable notCompletedOrderTable = new OrderTable();
        notCompletedOrderTable.setId(ORDER_TABLE_ID_1);
        notCompletedOrderTable.setNumberOfGuests(ORDER_TABLE_NUMBER_OF_GUESTS_1);
        notCompletedOrderTable.setEmpty(ORDER_TABLE_EMPTY_1);

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(notCompletedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(ORDER_TABLE_ID_1, ORDER_TABLE_1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 empty 변경 성 테스트")
    @Test
    void changeEmptyTest() {
        OrderTable notGroupedOrderTable = new OrderTable();
        notGroupedOrderTable.setId(ORDER_TABLE_ID_1);
        notGroupedOrderTable.setNumberOfGuests(ORDER_TABLE_NUMBER_OF_GUESTS_1);
        notGroupedOrderTable.setEmpty(ORDER_TABLE_EMPTY_1);

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(notGroupedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(ORDER_TABLE_1);

        OrderTable persistedOrderTable = tableService.changeEmpty(ORDER_TABLE_ID_1, ORDER_TABLE_1);
        assertThat(persistedOrderTable).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1);
    }

    @DisplayName("테이블 고객수 변경 예외 테스트: 음수로 변경 시도할 때")
    @Test
    void changeNumberOfGuestsTestFailByNegativeNumberOfGuests() {
        OrderTable negativeGuestsTable = new OrderTable();
        negativeGuestsTable.setId(ORDER_TABLE_ID_1);
        negativeGuestsTable.setTableGroupId(TABLE_GROUP_ID);
        negativeGuestsTable.setNumberOfGuests(-1);
        negativeGuestsTable.setEmpty(ORDER_TABLE_EMPTY_1);

        assertThatThrownBy(()-> tableService.changeEmpty(ORDER_TABLE_ID_1, negativeGuestsTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 고객수 변경 예외 테스트: 테이블이 존재하지 않을 때")
    @Test
    void changeNumberOfGuestsTestFailByNotExistTable() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(()-> tableService.changeEmpty(ORDER_TABLE_ID_1, ORDER_TABLE_1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 고객수 변경 예외 테스트: 테이블이 비어있을 때")
    @Test
    void changeNumberOfGuestsTestFailByEmptyTable() {
        OrderTable emptyTable = new OrderTable();
        emptyTable.setId(ORDER_TABLE_ID_1);
        emptyTable.setTableGroupId(TABLE_GROUP_ID);
        emptyTable.setNumberOfGuests(ORDER_TABLE_NUMBER_OF_GUESTS_1);
        emptyTable.setEmpty(true);

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(emptyTable));

        assertThatThrownBy(()-> tableService.changeEmpty(ORDER_TABLE_ID_1, emptyTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 고객수 변경 성공 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(ORDER_TABLE_1));
        given(orderTableDao.save(any())).willReturn(ORDER_TABLE_1);

        OrderTable persistedOrderTable = tableService.changeNumberOfGuests(ORDER_TABLE_ID_1, ORDER_TABLE_1);

        assertThat(persistedOrderTable).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1);
    }
}