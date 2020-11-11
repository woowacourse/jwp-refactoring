package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.TableGroupRequest;
import kitchenpos.dto.order.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupServiceTest extends ServiceTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    private TableGroupService tableGroupService;

    static Stream<TableGroupRequest> invalidTableGroupRequestWithOrderTable() {
        TableGroupRequest tableGroupRequestWithEmpty = new TableGroupRequest(Collections.emptyList());
        TableGroupRequest tableGroupRequestWithSingleOrderTable = new TableGroupRequest(
                Collections.singletonList(new OrderTableRequest(1L)));

        return Stream.of(
                tableGroupRequestWithEmpty, tableGroupRequestWithSingleOrderTable
        );
    }

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
    }

    @DisplayName("새로운 단체 지정")
    @Test
    void createTest() {
        OrderTable firstOrderTable = saveOrderTable(orderTableRepository, 1, true);
        OrderTable secondOrderTable = saveOrderTable(orderTableRepository, 2, true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new OrderTableRequest(firstOrderTable.getId()),
                        new OrderTableRequest(secondOrderTable.getId()))
        );

        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        assertAll(
                () -> assertThat(tableGroupResponse.getId()).isNotNull(),
                () -> assertThat(tableGroupResponse.getCreatedDate()).isBefore(LocalDateTime.now()),
                () -> assertThat(tableGroupResponse.getOrderTables()).hasSize(2)
        );
    }

    @DisplayName("새로운 단체 지정 시 주문 테이블의 개수가 잘못 되었을 때 예외 반환")
    @ParameterizedTest
    @MethodSource("invalidTableGroupRequestWithOrderTable")
    void createTableGroupWithInvalidOrderTableTest(TableGroupRequest tableGroupRequest) {
        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 단체 지정 시 존재하지 않는 주문 테이블 입력 시 예외 반환")
    @Test
    void createTableGroupWithInvalidOrderTableTest() {
        OrderTableRequest firstInvalidOrderTableRequest = new OrderTableRequest(0, false);
        OrderTableRequest secondInvalidOrderTableRequest = new OrderTableRequest(0, false);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(firstInvalidOrderTableRequest, secondInvalidOrderTableRequest)
        );

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroupTest() {
        TableGroup savedTableGroup = saveTableGroup(tableGroupRepository);
        OrderTable firstOrderTable = saveOrderTable(orderTableRepository, 1, true, savedTableGroup.getId());
        OrderTable secondOrderTable = saveOrderTable(orderTableRepository, 2, true, savedTableGroup.getId());

        tableGroupService.ungroup(savedTableGroup.getId());
        List<OrderTable> ungroupedOrderTables = orderTableRepository.findAllByIdIn(
                Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())
        );

        assertAll(
                () -> assertThat(ungroupedOrderTables.get(0).getTableGroup()).isNull(),
                () -> assertThat(ungroupedOrderTables.get(0).getNumberOfGuestsCount()).isEqualTo(1),
                () -> assertThat(ungroupedOrderTables.get(0).isEmptyTable()).isFalse(),
                () -> assertThat(ungroupedOrderTables.get(1).getTableGroup()).isNull(),
                () -> assertThat(ungroupedOrderTables.get(1).getNumberOfGuestsCount()).isEqualTo(2),
                () -> assertThat(ungroupedOrderTables.get(1).isEmptyTable()).isFalse()
        );
    }

    @DisplayName("단체 테이블 중 결제 완료 되지 않은 주문이 남아있을 때 단체 지정 해제 시 예외 반환")
    @Test
    void ungroupWithInvalidOrderTableTest() {
        TableGroup savedTableGroup = saveTableGroup(tableGroupRepository);
        OrderTable unPairedOrderTable = saveOrderTable(orderTableRepository, 1, true, savedTableGroup.getId());
        saveOrderTable(orderTableRepository, 2, true, savedTableGroup.getId());
        saveOrder(orderRepository, unPairedOrderTable.getId(), OrderStatus.MEAL, LocalDateTime.now());

        assertThatThrownBy(() -> {
            tableGroupService.ungroup(savedTableGroup.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        tableGroupRepository.deleteAll();
    }
}