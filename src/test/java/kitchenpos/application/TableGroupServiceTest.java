package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.application.TableGroupResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;

    @Test
    void 테이블그룹을_생성한다() {
        TableGroup tableGroupRequest = TableGroup.of(List.of(테이블_생성(true), 테이블_생성(true)));

        TableGroupResponse actual = tableGroupService.create(tableGroupRequest);
        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_없는_경우_예외를_발생시킨다() {
        assertThatThrownBy(
                () -> tableGroupService.create(
                        TableGroup.of(List.of(new OrderTable(-1L, 1L, 1, true)))))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_저장된_정보와_다를_경우_예외를_발생시킨다() {
        TableGroup tableGroupRequest = TableGroup.of(
                List.of(new OrderTable(-1L, null, 0, true), 테이블_생성(true)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_해제한다() {
        TableGroup savedGroup = 테이블집합_생성();

        assertDoesNotThrow(() -> tableGroupService.ungroup(savedGroup.getId()));
    }

    @Test
    void 테이블그룹을_해제할때_조리중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable orderTable = 테이블_생성(true);
        TableGroup tableGroupRequest = TableGroup.of(List.of(orderTable, 테이블_생성(true)));
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);
        주문_생성(orderTable.getId());

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_해제할때_식사중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable orderTable = 테이블_생성(true);
        TableGroup tableGroupRequest = TableGroup.of(List.of(orderTable, 테이블_생성(true)));
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);
        Order savedOrder = 주문_생성(orderTable.getId());
        orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.MEAL);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
