package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTestWithProfiles
class TableServiceTest {
    private static final OrderTable CHANGE_EMPTY_TRUE = new OrderTable(true);

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderDao orderDao;

    @Test
    @DisplayName("주문 테이블 정상 생성")
    void create() {
        OrderTable orderTable = new OrderTable(true);

        OrderTable saved = tableService.create(orderTable);
        assertNotNull(saved.getId());
    }

    @Test
    @DisplayName("주문 테이블 목록 정상 조회")
    void list() {
        tableService.create(new OrderTable(true));
        tableService.create(new OrderTable(false));
        tableService.create(new OrderTable(false));

        assertThat(tableService.list()).hasSize(3);
    }

    @Test
    @DisplayName("주문 테이블 정상 Empty 상태 수정")
    void changeEmpty() {
        OrderTable orderTable = tableService.create(new OrderTable(false));

        OrderTable changed = tableService.changeEmpty(orderTable.getId(), CHANGE_EMPTY_TRUE);
        assertThat(changed.isEmpty()).isEqualTo(CHANGE_EMPTY_TRUE.isEmpty());
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
        OrderTable orderTable1 = tableService.create(new OrderTable(true));
        OrderTable orderTable2 = tableService.create(new OrderTable(true));

        tableGroupService.create(new TableGroup(Arrays.asList(orderTable1, orderTable2)));
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), CHANGE_EMPTY_TRUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "주문 테이블 Empty 상태 수정 실패 :: 주문 상태 {0}")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void changeEmptyForTableWithNotAllowedOrderStatus(OrderStatus orderStatus) {
        OrderTable orderTable = tableService.create(new OrderTable(false));
        orderDao.save(new Order(orderTable.getId(), orderStatus.name(), LocalDateTime.now(), Collections.emptyList()));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), CHANGE_EMPTY_TRUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 손님 수 정상 수정")
    void changeNumberOfGuests() {
        OrderTable orderTable = tableService.create(new OrderTable(false));
        OrderTable request = new OrderTable(3);

        OrderTable changed = tableService.changeNumberOfGuests(orderTable.getId(), request);
        assertThat(changed.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @Test
    @DisplayName("주문 테이블 손님 수 수정 실패 :: 음의 손님 수")
    void changeNumberOfGuestsWithNegativeValue() {
        OrderTable orderTable = tableService.create(new OrderTable(false));
        OrderTable negativeNumberRequest = new OrderTable(-3);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), negativeNumberRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 손님 수 수정 실패 :: 존재하지 않는 테이블 ID")
    void changeNumberOfGuestsNotExistingTableId() {
        Long notExistingTableId = Long.MAX_VALUE;
        OrderTable request = new OrderTable(3);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistingTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 손님 수 수정 실패 :: 빈 테이블 상태")
    void changeNumberOfGuestsWithEmptyTableStatus() {
        OrderTable orderTable = tableService.create(new OrderTable(true));
        OrderTable request = new OrderTable(3);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        orderTableRepository.deleteAllInBatch();
        tableGroupRepository.deleteAllInBatch();
    }
}
