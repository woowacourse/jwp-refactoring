package kitchenpos.application;

import static kitchenpos.Fixture.테이블집합;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;

    @Test
    void 테이블그룹을_생성한다() {
        TableGroup tableGroup = 테이블집합(빈테이블을_생성(), 빈테이블을_생성());

        TableGroup actual = tableGroupService.create(tableGroup);
        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_없는_경우_예외를_발생시킨다() {
        TableGroup tableGroup = new TableGroup(null);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_2개보다_적은_경우_예외를_발생시킨다() {
        TableGroup tableGroup = 테이블집합(빈테이블을_생성());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_저장된_정보와_다를_경우_예외를_발생시킨다() {
        TableGroup tableGroup = 테이블집합(new OrderTable(-1L, null, 0, true), 빈테이블을_생성());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_비어있지_않은_경우_예외를_발생시킨다() {
        OrderTable orderTable = 빈테이블을_생성();
        orderTable.setEmpty(false);
        tableService.changeEmpty(orderTable.getId(), orderTable);
        TableGroup tableGroup = 테이블집합(orderTable, 빈테이블을_생성());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_해제한다() {
        TableGroup savedGroup = 테이블집합_생성();

        assertDoesNotThrow(() -> tableGroupService.ungroup(savedGroup.getId()));
    }

    @Test
    void 테이블그룹을_해제할때_조리중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable orderTable = 빈테이블을_생성();
        TableGroup tableGroup = 테이블집합(orderTable, 빈테이블을_생성());
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        주문_생성(orderTable.getId());

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_해제할때_식사중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable orderTable = 빈테이블을_생성();
        TableGroup tableGroup = 테이블집합(orderTable, 빈테이블을_생성());
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        Order savedOrder = 주문_생성(orderTable.getId());
        savedOrder.setOrderStatus("MEAL");
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    OrderTable 빈테이블을_생성() {
        return 테이블_생성(true);
    }
}
