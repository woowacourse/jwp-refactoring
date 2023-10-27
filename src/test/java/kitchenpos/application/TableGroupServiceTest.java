package kitchenpos.application;

import fixture.OrderBuilder;
import fixture.OrderTableBuilder;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import kitchenpos.tablegroup.domain.validator.TableGroupValidator;
import kitchenpos.tablegroup.ui.request.OrderTableIdRequest;
import kitchenpos.tablegroup.ui.request.TableGroupRequest;
import kitchenpos.tablegroup.ui.response.TableGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    TableGroupValidator tableGroupValidator;

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
        assertThatThrownBy(() -> tableGroupService.ungroup(2L)).isInstanceOf(IllegalArgumentException.class);
    }
}
