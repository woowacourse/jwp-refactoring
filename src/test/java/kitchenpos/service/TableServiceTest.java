package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableModifyRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class TableServiceTest {

    private final TableService tableService;
    private final TableGroupService tableGroupService;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    TableGroup tableGroup;
    List<OrderTable> orderTables;

    public TableServiceTest(TableService tableService, TableGroupService tableGroupService,
                            OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.tableService = tableService;
        this.tableGroupService = tableGroupService;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @BeforeEach
    void setUp() {
        orderTables = new ArrayList<>();
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(100, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(100, true));
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        tableGroup = tableGroupService.create(new TableGroupRequest(List.of(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId())
        )));
    }

    @DisplayName("존재하지 않는 주문 테이블의 경우 빈 테이블로 바꿀 수 없다.")
    @Test
    void changeEmptyWithNotSavedOrderTable() {
        OrderTableModifyRequest request = new OrderTableModifyRequest(
                100, true
        );

        assertThatThrownBy(() -> tableService.changeEmpty(-1L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 경우, 빈 테이블로 바꿀 수 없다.")
    @Test
    void changeEmptyWithGroupTable() {
        OrderTable orderTable = orderTables.get(0);
        OrderTableModifyRequest request = new OrderTableModifyRequest(
                orderTable.getNumberOfGuests(), orderTable.isEmpty()
        );

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리중이거나 식사중인 경우 빈 테이블로 바꿀 수 없다.")
    @Test
    void changeEmptyWithNotCookingOrMealState() {
        OrderTable orderTable = orderTables.get(0);
        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), Collections.emptyList());
        Order savedOrder = orderRepository.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableModifyRequest(100, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료된 경우 EMPTY로 상태가 변경된다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(100, true));
        Order order = new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.now(), Collections.emptyList());
        orderRepository.save(order);

        OrderTable emptyOrderTable = tableService.changeEmpty(orderTable.getId(), new OrderTableModifyRequest(100, true));

        assertThat(emptyOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("인원수가 음수인 경우 인원수 변경 시 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWithNegative() {
        Long orderTableId = orderTables.get(0).getId();
        OrderTableModifyRequest request = new OrderTableModifyRequest(-1, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블인 경우 인원수 변경 시 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWithEmptyTable() {
        OrderTable orderTable = new OrderTable(10, true);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        Long orderTableId = savedOrderTable.getId();
        OrderTableModifyRequest request = new OrderTableModifyRequest(100, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인원수가 성공적으로 변경된다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(10, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        OrderTableModifyRequest request = new OrderTableModifyRequest(100, true);
        Long orderTableId = savedOrderTable.getId();
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTableId, request);

        assertThat(changedOrderTable.getNumberOfGuests())
                .isEqualTo(100);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable savedOrderTable = tableService.create(new OrderTableCreateRequest(10, false));

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("모든 주문 테이블을 조회할 수 있다.")
    @Test
    void list() {
        assertThat(tableService.list()).hasSize(2);
    }
}
