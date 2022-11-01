package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.application.dto.request.OrderTableIdRequest;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 테이블을_생성할_수_있다() {
        OrderTableRequest request = new OrderTableRequest(5, false);

        OrderTableResponse actual = tableService.create(request);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getNumberOfGuests()).isEqualTo(5);
            assertThat(actual.isEmpty()).isFalse();
        });
    }

    @Test
    void 전체_테이블을_조회할_수_있다() {
        OrderTable orderTable1 = new OrderTable(null, 3, false);
        OrderTable orderTable2 = new OrderTable(null, 5, false);

        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        List<OrderTableResponse> actual = tableService.list();

        assertThat(actual).hasSize(2);
    }

    @Test
    void 기존_테이블의_빈_테이블_여부를_변경할_수_있다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));
        OrderTableRequest request = new OrderTableRequest(0, true);

        OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), request);

        assertThat(actual.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = {"COMPLETION"})
    void 기존_테이블의_주문_상태가_완료_상태가_아니면_빈_테이블로_변경할_수_없다(OrderStatus orderStatus) {
        OrderLineItem orderLineItem = new OrderLineItem("메뉴", new BigDecimal(10000), 2);

        Long orderTableId = orderTableRepository.save(new OrderTable(null, 5, false)).getId();
        orderRepository.save(new Order(orderTableId, orderStatus, List.of(orderLineItem)));

        OrderTableRequest request = new OrderTableRequest(0, true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 기존_테이블의_손님_수를_변경할_수_있다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, false));
        OrderTableRequest request = new OrderTableRequest(5, false);

        OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(), request);

        assertThat(actual.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 손님_수가_음수인_경우_손님_수를_변경할_수_없다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, false));
        OrderTableRequest request = new OrderTableRequest(-1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_테이블은_손님_수를_변경할_수_없다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, true));
        OrderTableRequest request = new OrderTableRequest(5, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블을_단체로_지정할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroupRequest request = new TableGroupRequest(
                List.of(new OrderTableIdRequest(orderTable1.getId()), new OrderTableIdRequest(orderTable2.getId())));

        TableGroupResponse actual = tableService.group(request);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getCreatedDate()).isNotNull();
            assertThat(actual.getOrderTables()).hasSize(2)
                    .extracting("tableGroupId")
                    .isNotNull();
        });
    }

    @Test
    void 단체로_지정할_테이블이_한_개_이하인_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));

        TableGroupRequest request = new TableGroupRequest(List.of(new OrderTableIdRequest(orderTable1.getId())));

        assertThatThrownBy(() -> tableService.group(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체로_지정할_테이블이_모두_비어있지_않는_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroupRequest request = new TableGroupRequest(
                List.of(new OrderTableIdRequest(orderTable1.getId()), new OrderTableIdRequest(orderTable2.getId())));

        assertThatThrownBy(() -> tableService.group(request)).isInstanceOf(IllegalArgumentException.class);
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

        assertThatThrownBy(() -> tableService.group(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_단체_지정을_취소할_수_있다() {
        OrderTable orderTable1 = new OrderTable(null, 1, true);
        OrderTable orderTable2 = new OrderTable(null, 2, true);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        tableService.ungroup(tableGroup.getId());

        OrderTable foundOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
        OrderTable foundOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();

        assertAll(() -> {
            assertThat(foundOrderTable1.isGrouped()).isFalse();
            assertThat(foundOrderTable1.isEmpty()).isFalse();
            assertThat(foundOrderTable2.isGrouped()).isFalse();
            assertThat(foundOrderTable2.isEmpty()).isFalse();
        });
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = {"COMPLETION"})
    void 단체_지정을_취소할_테이블들의_주문이_모두_완료_상태가_아닌_경우_취소할_수_없다(OrderStatus orderStatus) {
        OrderLineItem orderLineItem = new OrderLineItem("메뉴", new BigDecimal(10000), 2);

        OrderTable orderTable1 = new OrderTable(null, 1, true);
        OrderTable orderTable2 = new OrderTable(null, 2, true);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        orderRepository.save(new Order(orderTable1.getId(), orderStatus, List.of(orderLineItem)));

        assertThatThrownBy(() -> tableService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
