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
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
        List<OrderTableRequest> orderTables = 저장된_기본_주문_테이블들_id_요청();
        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTables);

        // when
        TableGroupResponse actual = tableGroupService.create(request);

        // then
        OrderTableResponse response1 = new OrderTableResponse(orderTables.get(0).getId(), actual.getId(), 0, false);
        OrderTableResponse response2 = new OrderTableResponse(orderTables.get(1).getId(), actual.getId(), 0, false);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getCreatedDate()).isCloseTo(LocalDateTime.now(),
                        new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS)),
                () -> assertThat(actual.getOrderTables())
                        .usingRecursiveComparison()
                        .isEqualTo(Arrays.asList(response1, response2))
        );
    }

    @Test
    void 주문_테이블_개수가_2개_미만이면_테이블_그룹으로_생성할_수_없다() {
        // given
        List<OrderTableRequest> invalidOrderTable = Collections.singletonList(new OrderTableRequest(1L));

        TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(invalidOrderTable);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록되지_않은_테이블은_테이블_그룹으로_생성할_수_없다() {
        // given
        List<OrderTableRequest> invalidOrderTables = 저장된_기본_주문_테이블들_id_요청();
        invalidOrderTables.add(new OrderTableRequest(-1L));

        TableGroupCreateRequest request = new TableGroupCreateRequest(invalidOrderTables);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_상태가_아니면_테이블_그룹으로_생성할_수_없다() {
        // given
        final List<OrderTableRequest> invalidOrderTables = 저장된_기본_주문_테이블들_id_요청();
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, null, 0, false));
        invalidOrderTables.add(new OrderTableRequest(orderTable.getId()));

        TableGroupCreateRequest request = new TableGroupCreateRequest(invalidOrderTables);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_다른_테이블_그룹에_등록되어있다면_테이블_그룹으로_생성할_수_없다() {
        // given
        List<OrderTableRequest> orderTables = 저장된_기본_주문_테이블들_id_요청();
        TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(orderTables);
        tableGroupService.create(tableGroup);

        // when
        TableGroupCreateRequest tableGroup2 = new TableGroupCreateRequest(orderTables);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_삭제한다() {
        // given
        List<OrderTableRequest> orderTables = Arrays.asList(
                생성된_orderTable_createRequest(OrderStatus.COMPLETION),
                생성된_orderTable_createRequest(OrderStatus.COMPLETION)
        );

        TableGroupResponse savedTableGroup = tableGroupService.create(new TableGroupCreateRequest(orderTables));

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        assertAll(
                () -> assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId())).isEmpty(),
                () -> assertThat(orderTableDao.findAllByIdIn(
                        orderTables.stream()
                                .map(OrderTableRequest::getId)
                                .collect(Collectors.toList())
                )).isEqualTo(Arrays.asList(new OrderTable(orderTables.get(0).getId(), null, 0, false),
                        new OrderTable(orderTables.get(1).getId(), null, 0, false)))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void 테이블_그룹에_속한_테이블이_조리중이거나_식사중인_상태라면_테이블_그룹을_삭제할_수_없다(String orderStatus) {
        // given
        List<OrderTableRequest> orderTables = Arrays.asList(
                생성된_orderTable_createRequest(OrderStatus.valueOf(orderStatus)),
                생성된_orderTable_createRequest(OrderStatus.COMPLETION)
        );

        TableGroupResponse savedTableGroup = tableGroupService.create(new TableGroupCreateRequest(orderTables));

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderTableRequest> 저장된_기본_주문_테이블들_id_요청() {
        OrderTable orderTable = new OrderTable(null, null, 0, true);
        OrderTable orderTable1 = orderTableDao.save(orderTable);
        OrderTable orderTable2 = orderTableDao.save(orderTable);

        return new ArrayList<>(Arrays.asList(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId())
        ));
    }

    private OrderTableRequest 생성된_orderTable_createRequest(OrderStatus orderStatus) {
        OrderTable orderTable = new OrderTable(null, null, 0, true);
        OrderTable emptyOrderTable = orderTableDao.save(orderTable);

        orderDao.save(new Order(emptyOrderTable.getId(), orderStatus, LocalDateTime.now()));
        return new OrderTableRequest(emptyOrderTable.getId());
    }
}
