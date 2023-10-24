package kitchenpos.application;

import fixture.OrderBuilder;
import fixture.OrderTableBuilder;
import fixture.TableGroupBuilder;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.request.OrderTableIdRequest;
import kitchenpos.ui.request.TableGroupRequest;
import kitchenpos.ui.response.TableGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void 테이블_그룹을_생성한다() {
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(
                        new OrderTableIdRequest(1L),
                        new OrderTableIdRequest(2L)
                )
        );
        final TableGroupResponse created = tableGroupService.create(tableGroupRequest);

        assertThat(created.getId()).isNotNull();
    }

    @Test
    void 주문_테이블이_2개_이하면_예외를_발생한다() {
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(
                        new OrderTableIdRequest(1L)
                )
        );

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹의_입력_테이블들이_비어_있으면_예외를_발생한다() {
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of());

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블아이디들로_주문테이블을_조회해서_개수가_맞지_않으면_예외를_발생한다() {
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(
                        new OrderTableIdRequest(1L),
                        new OrderTableIdRequest(1000L)
                )
        );

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블들이_비어있지_않으면_예외를_발생한다() {
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(
                        new OrderTableIdRequest(1L),
                        new OrderTableIdRequest(10L)
                )
        );

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹의_주문테이블들을_빈_테이블로_변경한다() {
        tableGroupService.ungroup(1L);

        final List<OrderTable> allByTableGroupId = orderTableRepository.findAllByTableGroupId(1L);
        assertSoftly(softAssertions -> {
            for (OrderTable orderTable : allByTableGroupId) {
                softAssertions.assertThat(orderTable.isEmpty()).isTrue();
            }
        });
    }

    @Test
    void 테이블_그룹에_조리중이나_식사중인_테이블이_있다면_언그룹을_못한다() {
        TableGroup tableGroup = TableGroupBuilder.init()
                .build();
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        OrderTable orderTable1 = OrderTableBuilder.init()
                .empty(false)
                .build();
        OrderTable orderTable2 = OrderTableBuilder.init()
                .empty(false)
                .build();
        savedTableGroup.addOrderTable(orderTable1);
        savedTableGroup.addOrderTable(orderTable2);
        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);
        Order order1 = OrderBuilder.init()
                .orderTable(savedOrderTable1)
                .orderStatus(OrderStatus.MEAL)
                .build();
        Order order2 = OrderBuilder.init()
                .orderTable(savedOrderTable2)
                .orderStatus(OrderStatus.COMPLETION)
                .build();
        orderRepository.save(order1);
        orderRepository.save(order2);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId())).isInstanceOf(IllegalArgumentException.class);
    }
}
