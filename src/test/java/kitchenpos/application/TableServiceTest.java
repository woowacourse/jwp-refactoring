package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.Table;
import kitchenpos.exception.*;
import kitchenpos.fixture.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest extends TestFixture {

    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private TableDao tableDao;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, tableDao);
    }

    @DisplayName("테이블 생성 성공 테스트")
    @Test
    void createTest() {
        given(tableDao.save(any())).willReturn(TABLE_1);

        Table persistTable = tableService.create();

        assertThat(persistTable).usingRecursiveComparison().isEqualTo(TABLE_1);
    }

    @DisplayName("테이블 조회 성공 테스트")
    @Test
    void listTest() {
        given(tableDao.findAll()).willReturn(TABLES);

        List<Table> persistTables = tableService.list();

        assertAll(
            () -> assertThat(persistTables).hasSize(TABLES.size()),
            () -> assertThat(persistTables.get(0)).usingRecursiveComparison().isEqualTo(TABLE_1),
            () -> assertThat(persistTables.get(1)).usingRecursiveComparison().isEqualTo(TABLE_2)
        );
    }

    @DisplayName("테이블 empty 변경 예외 테스트: 테이블이 존재하지 않을 때")
    @Test
    void changeEmptyFailByNotExistTable() {
        given(tableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(TABLE_ID_1, TABLE_EMPTY_1))
            .isInstanceOf(TableNotExistenceException.class);
    }

    @DisplayName("테이블 empty 변경 예외 테스트: 테이블이 그룹이 이미 지정되어 있을 때")
    @Test
    void changeEmptyFailByAlreadyIncluded() {
        given(tableDao.findById(anyLong())).willReturn(Optional.of(TABLE_1));

        assertThatThrownBy(() -> tableService.changeEmpty(TABLE_ID_1, TABLE_EMPTY_1))
            .isInstanceOf(TableGroupExistenceException.class);
    }

    @DisplayName("테이블 empty 변경 예외 테스트: 아직 식사 중 또는 요리 중일 때")
    @Test
    void changeEmptyFailByNotCompleted() {
        Table notCompletedTable = new Table(TABLE_ID_1, null, TABLE_NUMBER_OF_GUESTS_1, TABLE_EMPTY_1);

        given(tableDao.findById(anyLong())).willReturn(Optional.of(notCompletedTable));
        given(orderDao.findByTableIds(anyList())).willReturn(Arrays.asList(ORDER_1));

        assertThatThrownBy(() -> tableService.changeEmpty(TABLE_ID_1, TABLE_EMPTY_1))
            .isInstanceOf(TableCannotChangeEmptyException.class);
    }

    @DisplayName("테이블 empty 변경 성공 테스트")
    @Test
    void changeEmptyTest() {
        Table notGroupedTable = new Table(TABLE_ID_1, null, TABLE_NUMBER_OF_GUESTS_1, TABLE_EMPTY_1);

        given(tableDao.findById(anyLong())).willReturn(Optional.of(notGroupedTable));
        given(tableDao.save(any())).willReturn(TABLE_1);

        Table persistedTable = tableService.changeEmpty(TABLE_ID_1, TABLE_EMPTY_1);
        assertThat(persistedTable).usingRecursiveComparison().isEqualTo(TABLE_1);
    }

    @DisplayName("테이블 고객수 변경 예외 테스트: 음수로 변경 시도할 때")
    @Test
    void changeNumberOfGuestsTestFailByNegativeNumberOfGuests() {
        given(tableDao.findById(anyLong())).willReturn(Optional.of(TABLE_1));

        assertThatThrownBy(()-> tableService.changeNumberOfGuests(TABLE_ID_1, -1))
            .isInstanceOf(NotEnoughGuestsException.class);
    }

    @DisplayName("테이블 고객수 변경 예외 테스트: 테이블이 존재하지 않을 때")
    @Test
    void changeNumberOfGuestsTestFailByNotExistTable() {
        given(tableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(()-> tableService.changeNumberOfGuests(TABLE_ID_1, TABLE_NUMBER_OF_GUESTS_1))
            .isInstanceOf(TableNotExistenceException.class);
    }

    @DisplayName("테이블 고객수 변경 예외 테스트: 테이블이 비어있을 때")
    @Test
    void changeNumberOfGuestsTestFailByEmptyTable() {
        Table emptyTable = new Table(TABLE_ID_1, TABLE_GROUP_ID, TABLE_NUMBER_OF_GUESTS_1, true);

        given(tableDao.findById(anyLong())).willReturn(Optional.of(emptyTable));

        assertThatThrownBy(()-> tableService.changeNumberOfGuests(TABLE_ID_1, TABLE_NUMBER_OF_GUESTS_1))
            .isInstanceOf(TableEmptyException.class);
    }

    @DisplayName("테이블 고객수 변경 성공 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        given(tableDao.findById(anyLong())).willReturn(Optional.of(TABLE_1));
        given(tableDao.save(any())).willReturn(TABLE_1);

        Table persistedTable = tableService.changeNumberOfGuests(TABLE_ID_1, TABLE_NUMBER_OF_GUESTS_1);

        assertThat(persistedTable).usingRecursiveComparison().isEqualTo(TABLE_1);
    }
}