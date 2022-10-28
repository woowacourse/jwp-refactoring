package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import kitchenpos.application.TableGroupService;
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
public class TableGroupServiceTest {

    private final TableGroupService tableGroupService;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    OrderTable orderTable1;
    OrderTable orderTable2;

    public TableGroupServiceTest(TableGroupService tableGroupService,
                                 OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.tableGroupService = tableGroupService;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @BeforeEach
    void setUp() {
        orderTable1 = orderTableRepository.save(new OrderTable(100, true));
        orderTable2 = orderTableRepository.save(new OrderTable(100, true));
    }

    @DisplayName("단체 지정 시 주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    public void createWithEmptyOrderTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(-1L);
        TableGroupRequest request = new TableGroupRequest(List.of(orderTableRequest));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블의 개수가 1개인 경우 예외가 발생한다.")
    @Test
    public void createWithOneElementOrderTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(orderTable1.getId());
        TableGroupRequest request = new TableGroupRequest(List.of(orderTableRequest));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 비어있지 않다면 예외가 발생한다.")
    @Test
    public void createWithNotEmptyOrderTable() {

        OrderTable orderTable = orderTableRepository.save(new OrderTable(100, false));
        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(orderTable.getId()),
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId())
        );
        TableGroupRequest request = new TableGroupRequest(orderTableRequests);

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 단체가 지정된 경우 ID가 발급된다.")
    @Test
    public void create() {
        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId())
        );

        TableGroupRequest request = new TableGroupRequest(orderTableRequests);

        assertThat(tableGroupService.create(request).getId()).isNotNull();
    }

    @DisplayName("그룹을 해제하는 경우를 테스트한다.")
    @Test
    public void ungroup() {
        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId())
        );
        TableGroup savedTableGroup = tableGroupService.create(new TableGroupRequest(orderTableRequests));
        tableGroupService.ungroup(savedTableGroup.getId());

        OrderTable orderTable = orderTableRepository.findById(orderTable1.getId()).get();
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @DisplayName("그룹을 해제하는 경우, 조리중이거나 식사 상태인 경우 예외가 발생한다.")
    @Test
    public void ungroupWithCookingAndMealState() {
        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId())
        );
        TableGroup savedTableGroup = tableGroupService.create(new TableGroupRequest(orderTableRequests));

        Order order = new Order(
                orderTable1,
                OrderStatus.COOKING,
                LocalDateTime.now(),
                Collections.emptyList()
        );
        Order savedOrder = orderRepository.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
