package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.ChangeTableEmptyRequest;
import kitchenpos.dto.request.ChangeTableNumberOfGuestsRequest;
import kitchenpos.dto.request.TableRequest;
import kitchenpos.dto.response.TableResponse;
import kitchenpos.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @MockBean
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        databaseCleanUp.clear();
    }

    @DisplayName("테이블을 저장한다.")
    @Test
    void create() {
        // given
        final TableRequest orderTableRequest = createOrderTableRequest(4, false);

        // when
        final TableResponse savedOrderTable = tableService.create(orderTableRequest);

        // then
        assertThat(savedOrderTable).usingRecursiveComparison()
                .ignoringFields("id", "tableGroupId")
                .isEqualTo(orderTableRequest);
    }

    @DisplayName("테이블을 전체 조회한다.")
    @Test
    void findAll() {
        // given
        orderTableRepository.save(createOrderTable(4, false));

        // when, then
        assertThat(tableService.findAll()).hasSize(1);
    }

    @DisplayName("테이블의 사용 여부를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));

        // when
        final ChangeTableEmptyRequest orderTableForUpdateRequest = createChangeEmptyRequest(true);
        final TableResponse updatedOrderTable = tableService
                .changeEmpty(orderTable.getId(), orderTableForUpdateRequest);

        // then
        assertAll(
                () -> assertThat(updatedOrderTable.isEmpty()).isTrue(),
                () -> assertThat(updatedOrderTable).usingRecursiveComparison()
                        .ignoringFields("empty")
                        .isEqualTo(orderTable)
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
        final TableGroup tableGroup = tableGroupRepository.save(createTableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 4, false));

        // when, then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), createChangeEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블입니다.");
    }

    @DisplayName("테이블에 주문이 들어가거나 식사 상태인데 사용여부를 변경하면 예외를 반환한다.")
    @Test
    void changeEmpty_throwException_ifOrderAlreadyOngoing() {
        // given
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(true);
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));

        // when, then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), createChangeEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 주문이 들어간 테이블입니다.");
    }

    @DisplayName("방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));

        // when
        final ChangeTableNumberOfGuestsRequest orderTableForUpdate = createChangeNumberOfGuestsRequest(5);
        final TableResponse updatedOrderTable = tableService
                .changeNumberOfGuests(orderTable.getId(), orderTableForUpdate);

        // then
        assertAll(
                () -> assertThat(updatedOrderTable.getNumberOfGuests())
                        .isEqualTo(orderTableForUpdate.getNumberOfGuests()),
                () -> assertThat(updatedOrderTable).usingRecursiveComparison()
                        .ignoringFields("numberOfGuests")
                        .isEqualTo(orderTable)
        );
    }

    @DisplayName("방문한 손님 수를 음수로 변경하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_throwException_ifNumberOfGuestsIsNegative() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));

        // when, then
        final ChangeTableNumberOfGuestsRequest orderTableForUpdate = createChangeNumberOfGuestsRequest(-1);
        assertThatThrownBy(() -> tableService
                .changeNumberOfGuests(orderTable.getId(), orderTableForUpdate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 인원은 음수가 될 수 없습니다.");
    }

    @DisplayName("테이블이 존재하지 않을 경우 방문한 손님 수 변경하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_throwException_ifTableNotExist() {
        // given
        final Long invalidTableId = 999L;

        // when, then
        final ChangeTableNumberOfGuestsRequest orderTableForUpdate = createChangeNumberOfGuestsRequest(5);
        assertThatThrownBy(() -> tableService
                .changeNumberOfGuests(invalidTableId, orderTableForUpdate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 존재하지 않습니다.");
    }

    @DisplayName("사용 중이지 않은 테이블의 방문한 손님 수를 변경하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_throwException_ifTableEmpty() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));

        // when, then
        final ChangeTableNumberOfGuestsRequest orderTableForUpdate = createChangeNumberOfGuestsRequest(5);
        assertThatThrownBy(() -> tableService
                .changeNumberOfGuests(orderTable.getId(), orderTableForUpdate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용 중이지 않은 테이블의 방문한 손님 수를 변경할 수 없습니다.");
    }

    private TableRequest createOrderTableRequest(final int numberOfGuests, final boolean empty) {
        return new TableRequest(numberOfGuests, empty);
    }

    private ChangeTableEmptyRequest createChangeEmptyRequest(final boolean empty) {
        return new ChangeTableEmptyRequest(empty);
    }

    private ChangeTableNumberOfGuestsRequest createChangeNumberOfGuestsRequest(final int numberOfGuests) {
        return new ChangeTableNumberOfGuestsRequest(numberOfGuests);
    }
}
