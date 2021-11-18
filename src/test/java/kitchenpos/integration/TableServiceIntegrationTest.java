package kitchenpos.integration;

import kitchenpos.table.application.TableService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.ui.dto.TableRequest;
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
@DisplayName("Table 서비스 통합 테스트")
public class TableServiceIntegrationTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    private OrderTable orderTable;
    private long orderTableId;

    @BeforeEach
    void setUp() {
        TableGroup tableGroup = CREATE_TABLE_GROUP(1L);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        orderTable = orderTableRepository.save(CREATE_ORDER_TABLE(1L, savedTableGroup, 10, false));

        orderTableId = orderTable.getId();
    }

    @DisplayName("OrderTable을 빈 상태로 변경한다.- 성공, 모든 주문이 완료 상태")
    @Test
    void changeEmpty() {
        // given
        List<Order> orders = Arrays.asList(
                CREATE_ORDER(1L, orderTable, OrderStatus.COMPLETION.name()),
                CREATE_ORDER(2L, orderTable, OrderStatus.COMPLETION.name()),
                CREATE_ORDER(3L, orderTable, OrderStatus.COMPLETION.name())
        );
        orderRepository.saveAll(orders);

        TableRequest tableRequest = TableRequest.of(0, true);

        // when - then
        assertThatCode(() -> tableService.changeEmpty(orderTable.getId(), tableRequest))
                .doesNotThrowAnyException();
    }

    @DisplayName("OrderTable을 빈 상태로 변경한다.- 실패, COOKING 상태의 주문이 포함됨.")
    @Test
    void changeEmptyFailedWhenContainingCOOKING() {
        // given
        List<Order> orders = Arrays.asList(
                CREATE_ORDER(1L, orderTable, OrderStatus.COMPLETION.name()),
                CREATE_ORDER(2L, orderTable, OrderStatus.COOKING.name()),
                CREATE_ORDER(3L, orderTable, OrderStatus.COOKING.name())
        );
        orderRepository.saveAll(orders);

        TableRequest tableRequest = TableRequest.of(0, true);

        // when - then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), tableRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아직 조리 혹은 식사 중인 주문이 존재합니다.");
    }

    @DisplayName("OrderTable을 빈 상태로 변경한다.- 실패, MEAL 상태의 주문이 포함됨.")
    @Test
    void changeEmptyFailedWhenContainingMEAL() {
        // given
        List<Order> orders = Arrays.asList(
                CREATE_ORDER(1L, orderTable, OrderStatus.COMPLETION.name()),
                CREATE_ORDER(2L, orderTable, OrderStatus.MEAL.name()),
                CREATE_ORDER(3L, orderTable, OrderStatus.MEAL.name())
        );
        orderRepository.saveAll(orders);

        TableRequest tableRequest = TableRequest.of(0, true);

        // when - then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), tableRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아직 조리 혹은 식사 중인 주문이 존재합니다.");
    }
}
