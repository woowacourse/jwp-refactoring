package kitchenpos.integration;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.utils.Fixture.DomainFactory.CREATE_ORDER;
import static kitchenpos.utils.Fixture.DomainFactory.CREATE_ORDER_TABLE;
import static kitchenpos.utils.Fixture.DomainFactory.CREATE_TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
@DisplayName("OrderTable 서비스 통합 테스트")
public class OrderTableServiceIntegrationTest {
    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    private OrderTable firstOrderTable, secondOrderTable, thirdOrderTable;
    private long tableGroupId;

    @BeforeEach
    void setUp() {
        TableGroup tableGroup = CREATE_TABLE_GROUP(1L);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        tableGroupId = savedTableGroup.getId();

        List<OrderTable> orderTables = Arrays.asList(
                CREATE_ORDER_TABLE(1L, savedTableGroup, 10, false),
                CREATE_ORDER_TABLE(2L, savedTableGroup, 20, false),
                CREATE_ORDER_TABLE(3L, savedTableGroup, 30, false)
        );
        orderTableRepository.saveAll(orderTables);

        firstOrderTable = orderTables.get(0);
        secondOrderTable = orderTables.get(1);
        thirdOrderTable = orderTables.get(2);
    }

    @DisplayName("OrderTable 그룹을 해제한다. - 성공, 모든 주문이 완료 상태")
    @Test
    void ungroup() {
        // given
        List<Order> orders = Arrays.asList(
                CREATE_ORDER(1L, firstOrderTable, OrderStatus.COMPLETION.name()),
                CREATE_ORDER(2L, secondOrderTable, OrderStatus.COMPLETION.name()),
                CREATE_ORDER(3L, thirdOrderTable, OrderStatus.COMPLETION.name())
        );
        orderRepository.saveAll(orders);

        // when - then
        assertThatCode(() -> orderTableService.ungroup(tableGroupId))
                .doesNotThrowAnyException();
    }

    @DisplayName("OrderTable 그룹을 해제한다. - 실패, COOKING 상태의 주문이 포함됨.")
    @Test
    void ungroupFailedWhenContainingCOOKING() {
        // given
        List<Order> orders = Arrays.asList(
                CREATE_ORDER(1L, firstOrderTable, OrderStatus.COMPLETION.name()),
                CREATE_ORDER(2L, secondOrderTable, OrderStatus.COOKING.name()),
                CREATE_ORDER(3L, thirdOrderTable, OrderStatus.COOKING.name())
        );
        orderRepository.saveAll(orders);

        // when - then
        assertThatThrownBy(() -> orderTableService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아직 조리 혹은 식사 중인 주문이 존재합니다.");
    }

    @DisplayName("OrderTable 그룹을 해제한다. - 실패, MEAL 상태의 주문이 포함됨.")
    @Test
    void ungroupFailedWhenContainingMEAL() {
        // given
        List<Order> orders = Arrays.asList(
                CREATE_ORDER(1L, firstOrderTable, OrderStatus.COMPLETION.name()),
                CREATE_ORDER(2L, secondOrderTable, OrderStatus.MEAL.name()),
                CREATE_ORDER(3L, thirdOrderTable, OrderStatus.COMPLETION.name())
        );
        orderRepository.saveAll(orders);

        // when - then
        assertThatThrownBy(() -> orderTableService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아직 조리 혹은 식사 중인 주문이 존재합니다.");
    }
}
