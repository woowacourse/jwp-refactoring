package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.domain.ordertable.TableGroup;
import kitchenpos.domain.ordertable.TableGroupRepository;
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 테이블을_단체로_지정할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroupRequest request = new TableGroupRequest(
                List.of(new OrderTableIdRequest(orderTable1.getId()), new OrderTableIdRequest(orderTable2.getId())));

        TableGroup actual = tableGroupService.create(request);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getCreatedDate()).isNotNull();
            assertThat(actual.getOrderTables()).hasSize(2)
                    .extracting("tableGroup")
                    .isNotNull();
        });
    }

    @Test
    void 단체로_지정할_테이블이_한_개_이하인_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));

        TableGroupRequest request = new TableGroupRequest(List.of(new OrderTableIdRequest(orderTable1.getId())));

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체로_지정할_테이블_중_빈_테이블이_존재하는_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroupRequest request = new TableGroupRequest(
                List.of(new OrderTableIdRequest(orderTable1.getId()), new OrderTableIdRequest(orderTable2.getId())));

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체로_지정할_테이블_중_이미_단체로_지정된_테이블이_존재하는_경우_지정할_수_없다() {
        OrderTable alreadyGroupedOrderTable1 = new OrderTable(null, 1, true);
        OrderTable alreadyGroupedOrderTable2 = new OrderTable(null, 2, true);

        tableGroupRepository.save(new TableGroup(List.of(alreadyGroupedOrderTable1, alreadyGroupedOrderTable2)));

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 1, true));

        TableGroupRequest request = new TableGroupRequest(
                List.of(new OrderTableIdRequest(alreadyGroupedOrderTable1.getId()),
                        new OrderTableIdRequest(orderTable.getId())));

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_단체_지정을_취소할_수_있다() {
        OrderTable orderTable1 = new OrderTable(null, 1, true);
        OrderTable orderTable2 = new OrderTable(null, 2, true);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        tableGroupService.ungroup(tableGroup.getId());

        OrderTable foundOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
        OrderTable foundOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();

        assertAll(() -> {
            assertThat(foundOrderTable1.getTableGroup()).isNull();
            assertThat(foundOrderTable1.isEmpty()).isFalse();
            assertThat(foundOrderTable2.getTableGroup()).isNull();
            assertThat(foundOrderTable2.isEmpty()).isFalse();
        });
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = {"COMPLETION"})
    void 단체_지정을_취소할_테이블들의_주문이_모두_완료_상태가_아닌_경우_취소할_수_없다(OrderStatus orderStatus) {
        OrderTable orderTable1 = new OrderTable(null, 1, true);
        OrderTable orderTable2 = new OrderTable(null, 2, true);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        orderRepository.save(new Order(orderTable1, orderStatus, new ArrayList<>()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
