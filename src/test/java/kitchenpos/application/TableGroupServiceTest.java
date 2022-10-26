package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 테이블_그룹을_생성한다() {
        // given
        List<OrderTable> orderTables = 저장된_기본_주문_테이블들();
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when
        TableGroup actual = tableGroupService.create(tableGroup);

        // then
        OrderTable expectedOrderTable1 = new OrderTable();
        expectedOrderTable1.setTableGroupId(actual.getId());
        OrderTable expectedOrderTable2 = new OrderTable();
        expectedOrderTable2.setTableGroupId(actual.getId());

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getCreatedDate()).isCloseTo(LocalDateTime.now(),
                        new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS)),
                () -> assertThat(actual.getOrderTables()).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(Arrays.asList(expectedOrderTable1, expectedOrderTable2))
        );
    }

    @Test
    void 주문_테이블_개수가_2개_미만이면_테이블_그룹으로_생성할_수_없다() {
        // given
        List<OrderTable> invalidOrderTable = Collections.singletonList(new OrderTable());

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(invalidOrderTable);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록되지_않은_테이블은_테이블_그룹으로_생성할_수_없다() {
        // given
        List<OrderTable> invalidOrderTables = 저장된_기본_주문_테이블들();
        invalidOrderTables.add(new OrderTable());

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(invalidOrderTables);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_상태가_아니면_테이블_그룹으로_생성할_수_없다() {
        // given
        List<OrderTable> invalidOrderTables = 저장된_기본_주문_테이블들();
        OrderTable invalidOrderTable = new OrderTable();
        invalidOrderTables.add(orderTableDao.save(invalidOrderTable));

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(invalidOrderTables);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_다른_테이블_그룹에_등록되어있다면_테이블_그룹으로_생성할_수_없다() {
        // given
        List<OrderTable> orderTables = 저장된_기본_주문_테이블들();
        TableGroup tableGroup1 = new TableGroup();
        tableGroup1.setOrderTables(orderTables);
        tableGroupService.create(tableGroup1);

        // when
        TableGroup tableGroup2 = new TableGroup();
        tableGroup2.setOrderTables(orderTables);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_삭제한다() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                getOrderedOrderTable(OrderStatus.COMPLETION.name()),
                getOrderedOrderTable(OrderStatus.COMPLETION.name())
        );

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        orderTables.forEach(orderTable -> {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
        });

        assertAll(
                () -> assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId())).isEmpty(),
                () -> assertThat(orderTableDao.findAllByIdIn(
                        orderTables.stream()
                                .map(OrderTable::getId)
                                .collect(Collectors.toList()))
                ).usingRecursiveComparison().isEqualTo(orderTables)
        );
    }

    @Test
    void 테이블_그룹에_속한_테이블이_조리중인_상태라면_삭제할_수_없다() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                getOrderedOrderTable(OrderStatus.COOKING.name()),
                getOrderedOrderTable(OrderStatus.COMPLETION.name())
        );

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹에_속한_테이블이_식사중인_상태라면_삭제할_수_없다() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                getOrderedOrderTable(OrderStatus.MEAL.name()),
                getOrderedOrderTable(OrderStatus.COMPLETION.name())
        );

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderTable> 저장된_기본_주문_테이블들() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable orderTable1 = orderTableDao.save(orderTable);
        OrderTable orderTable2 = orderTableDao.save(orderTable);

        return new ArrayList<>(Arrays.asList(orderTable1, orderTable2));
    }

    private OrderTable getOrderedOrderTable(String orderStatus) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable emptyOrderTable = orderTableDao.save(orderTable);

        Order order = new Order();
        order.setOrderTableId(emptyOrderTable.getId());
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);
        return emptyOrderTable;
    }
}
