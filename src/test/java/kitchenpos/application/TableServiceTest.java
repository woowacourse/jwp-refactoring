package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @MockBean
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        databaseCleanUp.clear();
    }

    @DisplayName("테이블을 저장한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTableRequest = createOrderTableRequest(4, false);

        // when
        final OrderTable savedOrderTable = tableService.create(orderTableRequest);

        // then
        assertThat(savedOrderTable).usingRecursiveComparison()
                .ignoringFields("id", "tableGroupId")
                .isEqualTo(orderTableRequest);
    }

    @DisplayName("테이블을 전체 조회한다.")
    @Test
    void findAll() {
        // given
        orderTableDao.save(createOrderTable(4, false));

        // when, then
        assertThat(tableService.findAll()).hasSize(1);
    }

    @DisplayName("테이블의 사용 여부를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable orderTableRequest = createOrderTableRequest(4, false);
        final OrderTable savedOrderTable = tableService.create(orderTableRequest);

        // when
        final OrderTable orderTableForUpdateRequest = createChangeEmptyRequest(true);
        final OrderTable updatedOrderTable = tableService
                .changeEmpty(savedOrderTable.getId(), orderTableForUpdateRequest);

        // then
        assertAll(
                () -> assertThat(updatedOrderTable.isEmpty()).isTrue(),
                () -> assertThat(updatedOrderTable).usingRecursiveComparison()
                        .ignoringFields("empty")
                        .isEqualTo(savedOrderTable)
        );
    }

    @DisplayName("테이블이 존재하지 않으면 사용 여부를 변경할 시 예외를 반환한다.")
    @Test
    void changeEmpty_throwException_ifTableNotExist() {
        // given
        final Long invalidTableId = 999L;

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidTableId, createChangeEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블입니다.");
    }

    @DisplayName("테이블이 단체 지정되어 있는데 사용여부를 변경하면 예외를 반환한다.")
    @Test
    void changeEmpty_throwException_ifTableGrouping() {
        // given
        final TableGroup savedTableGroup = tableGroupDao.save(createTableGroup(LocalDateTime.now()));
        final OrderTable orderTable = createOrderTable(savedTableGroup.getId(), 4, false);
        final OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setTableGroupId(1L);
        orderTableDao.save(savedOrderTable);

        // when, then
        assertThatThrownBy(
                () -> tableService.changeEmpty(savedOrderTable.getId(), createChangeEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블입니다.");
    }

    @DisplayName("테이블에 주문이 들어가거나 식사 상태인데 사용여부를 변경하면 예외를 반환한다.")
    @Test
    void changeEmpty_throwException_ifOrderAlreadyOngoing() {
        // given
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(true);
        final OrderTable orderTableRequest = createOrderTableRequest(4, false);
        final OrderTable savedOrderTable = tableService.create(orderTableRequest);

        // when, then
        assertThatThrownBy(
                () -> tableService.changeEmpty(savedOrderTable.getId(), createChangeEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 주문이 들어간 테이블입니다.");
    }

    @DisplayName("방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTableRequest = createOrderTableRequest(4, false);
        final OrderTable savedOrderTable = tableService.create(orderTableRequest);

        // when
        final OrderTable orderTableForUpdate = createChangeNumberOfGuestsRequest(5);
        final OrderTable updatedOrderTable = tableService
                .changeNumberOfGuests(savedOrderTable.getId(), orderTableForUpdate);

        // then
        assertAll(
                () -> assertThat(updatedOrderTable.getNumberOfGuests())
                        .isEqualTo(orderTableForUpdate.getNumberOfGuests()),
                () -> assertThat(updatedOrderTable).usingRecursiveComparison()
                        .ignoringFields("numberOfGuests")
                        .isEqualTo(savedOrderTable)
        );
    }

    @DisplayName("방문한 손님 수를 음수로 변경하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_throwException_ifNumberOfGuestsIsNegative() {
        // given
        final OrderTable orderTableRequest = createOrderTableRequest(4, false);
        final OrderTable savedOrderTable = tableService.create(orderTableRequest);

        // when, then
        final OrderTable orderTableForUpdate = createChangeNumberOfGuestsRequest(-1);
        assertThatThrownBy(() -> tableService
                .changeNumberOfGuests(savedOrderTable.getId(), orderTableForUpdate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 인원은 음수가 될 수 없습니다.");
    }

    @DisplayName("테이블이 존재하지 않을 경우 방문한 손님 수 변경하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_throwException_ifTableNotExist() {
        // given
        final Long invalidTableId = 999L;

        // when, then
        final OrderTable orderTableForUpdate = createChangeNumberOfGuestsRequest(5);
        assertThatThrownBy(() -> tableService
                .changeNumberOfGuests(invalidTableId, orderTableForUpdate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 존재하지 않습니다.");
    }

    @DisplayName("사용 중이지 않은 테이블의 방문한 손님 수를 변경하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_throwException_ifTableEmpty() {
        // given
        final OrderTable orderTable = createOrderTableRequest(0, true);
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // when, then
        final OrderTable orderTableForUpdate = createChangeNumberOfGuestsRequest(5);
        assertThatThrownBy(() -> tableService
                .changeNumberOfGuests(savedOrderTable.getId(), orderTableForUpdate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용 중이지 않은 테이블의 방문한 손님 수를 변경할 수 없습니다.");
    }

    private OrderTable createOrderTableRequest(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTableRequest = new OrderTable();
        orderTableRequest.setNumberOfGuests(numberOfGuests);
        orderTableRequest.setEmpty(empty);
        return orderTableRequest;
    }

    private OrderTable createChangeEmptyRequest(final boolean empty) {
        return createOrderTableRequest(0, empty);
    }

    private OrderTable createChangeNumberOfGuestsRequest(final int numberOfGuests) {
        return createOrderTableRequest(numberOfGuests, false);
    }
}
