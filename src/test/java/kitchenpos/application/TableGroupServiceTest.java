package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.application.request.TableGroupCreateRequest;
import kitchenpos.application.response.TableGroupResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderTable orderTableA;
    private OrderTable orderTableB;

    @BeforeEach
    void setUp() {
        orderTableA = orderTableRepository.save(OrderTableFixture.createOrderTable(null, true, 4));
        orderTableB = orderTableRepository.save(OrderTableFixture.createOrderTable(null, true, 2));
    }

    @Nested
    class 테이블을_그룹화_할_때 {

        @Test
        void 정상적으로_그룹화한다() {
            TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                    List.of(orderTableA.getId(), orderTableB.getId()));

            TableGroupResponse response = tableGroupService.create(tableGroupCreateRequest);

            assertThat(response.getId()).isPositive();
        }

        @Test
        void 주문_테이블_수가_2보다_작으면_예외가_발생한다() {
            TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(orderTableA.getId()));

            assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 그룹화를 위한 테이블이 2개 이상 필요합니다.");
        }

        @Test
        void 주문_테이블을_찾을_수_없으면_예외가_발생한다() {
            TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(-1L, -2L));

            assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 그룹화를 위한 테이블이 2개 이상 필요합니다.");
        }

        @Test
        void 주문_테이블_상태가_EMPTY가_아니면_예외가_발생한다() {
            OrderTable notEmptyOrderTableA = orderTableRepository.save(OrderTableFixture.createOrderTable(null, false, 4));
            OrderTable notEmptyOrderTableB = orderTableRepository.save(OrderTableFixture.createOrderTable(null, false, 2));
            TableGroupCreateRequest tableGroupCreateRequest =
                    new TableGroupCreateRequest(List.of(notEmptyOrderTableA.getId(), notEmptyOrderTableB.getId()));

            assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않은 테이블은 그룹화할 수 없습니다.");
        }
    }

    @Nested
    class 그룹화를_해지할_떄 {

        @Test
        void 정상적으로_해지한다() {
            Order orderA = Order.builder().orderTable(orderTableA).orderStatus(OrderStatus.COMPLETION).build();
            Order orderB = Order.builder().orderTable(orderTableB).orderStatus(OrderStatus.COMPLETION).build();
            orderRepository.save(orderA);
            orderRepository.save(orderB);
            TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                    List.of(orderTableA.getId(), orderTableB.getId()));
            TableGroupResponse response = tableGroupService.create(tableGroupCreateRequest);

            assertDoesNotThrow(() -> tableGroupService.ungroup(response.getId()));
        }

        @Test
        void 주문_테이블_주문_상태가_COMPLETE가_아니면_예외가_발생한다() {
            Order order = Order.builder().orderTable(orderTableA).orderStatus(OrderStatus.MEAL).build();
            orderRepository.save(order);
            TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                    List.of(orderTableA.getId(), orderTableB.getId()));
            TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupCreateRequest);

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("요리중이거나 식사중인 테이블이 존재합니다.");
        }
    }
}
