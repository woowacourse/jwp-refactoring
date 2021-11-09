package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.service.TableGroupRequest;
import kitchenpos.tablegroup.service.TableGroupRequest.OrderTableId;
import kitchenpos.tablegroup.service.TableGroupService;
import kitchenpos.table.service.TableRequest;
import kitchenpos.table.service.TableResponse;
import kitchenpos.table.service.TableService;
import kitchenpos.table.service.TableValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTestWithProfiles
class TableServiceTest {
    private static final TableRequest CHANGE_EMPTY_TRUE = new TableRequest(null, true);
    private static final TableRequest CHANGE_GUESTS_TO_THREE = new TableRequest(3, null);
    private static final TableRequest TABLE_REQUEST_FOUR_NOT_EMPTY = new TableRequest(4, false);
    private static final TableRequest TABLE_REQUEST_FOUR_EMPTY = new TableRequest(4, true);

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @MockBean
    private TableValidator tableValidator;

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 테이블 정상 생성")
    void create() {
        TableResponse saved = tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);
        assertNotNull(saved.getId());
    }

    @Test
    @DisplayName("주문 테이블 목록 정상 조회")
    void list() {
        tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);
        tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);
        tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);

        assertThat(tableService.list()).hasSize(3);
    }

    @Test
    @DisplayName("주문 테이블 정상 Empty 상태 수정")
    void changeEmpty() {
        TableResponse orderTable = tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);

        TableResponse changed = tableService.changeEmpty(orderTable.getId(), CHANGE_EMPTY_TRUE);
        assertThat(changed.isEmpty()).isEqualTo(CHANGE_EMPTY_TRUE.getEmpty());
    }

    @Test
    @DisplayName("주문 테이블 Empty 상태 수정 실패 :: 존재하지 않는 OrderTable")
    void changeEmptyNotExistingOrderTable() {
        Long notExistingOrderTableId = Long.MAX_VALUE;
        assertThatThrownBy(() -> tableService.changeEmpty(notExistingOrderTableId, CHANGE_EMPTY_TRUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 Empty 상태 수정 실패 :: 단체 지정된 테이블")
    void changeEmptyForTableWithTableGroupId() {
        TableResponse orderTable1 = tableService.create(TABLE_REQUEST_FOUR_EMPTY);
        TableResponse orderTable2 = tableService.create(TABLE_REQUEST_FOUR_EMPTY);

        tableGroupService.create(new TableGroupRequest(Arrays.asList(
                new OrderTableId(orderTable1.getId()),
                new OrderTableId(orderTable2.getId())
        )));
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), CHANGE_EMPTY_TRUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 Empty 상태 수정 실패 :: validate 실패")
    void changeEmptyForTableWithNotAllowedOrderStatus() {
        TableResponse orderTable = tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);

        doThrow(new IllegalArgumentException()).when(tableValidator).validateUpdateEmpty(any());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), CHANGE_EMPTY_TRUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 손님 수 정상 수정")
    void changeNumberOfGuests() {
        TableResponse orderTable = tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);

        TableResponse changed = tableService.changeNumberOfGuests(orderTable.getId(), CHANGE_GUESTS_TO_THREE);
        assertThat(changed.getNumberOfGuests()).isEqualTo(CHANGE_GUESTS_TO_THREE.getNumberOfGuests());
    }

    @Test
    @DisplayName("주문 테이블 손님 수 수정 실패 :: 음의 손님 수")
    void changeNumberOfGuestsWithNegativeValue() {
        TableResponse orderTable = tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);
        TableRequest negativeNumberRequest = new TableRequest(-3, null);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), negativeNumberRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 손님 수 수정 실패 :: 존재하지 않는 테이블 ID")
    void changeNumberOfGuestsNotExistingTableId() {
        Long notExistingTableId = Long.MAX_VALUE;

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistingTableId, CHANGE_GUESTS_TO_THREE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 손님 수 수정 실패 :: 빈 테이블 상태")
    void changeNumberOfGuestsWithEmptyTableStatus() {
        TableResponse orderTable = tableService.create(new TableRequest(3, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), CHANGE_GUESTS_TO_THREE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAllInBatch();
        orderTableRepository.deleteAllInBatch();
        tableGroupRepository.deleteAllInBatch();
    }
}
