package kitchenpos.table.application;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.support.DataCleaner;
import kitchenpos.order.application.dto.OrderTableResponse;
import kitchenpos.table.application.dto.OrderTableIdRequest;
import kitchenpos.table.application.dto.TableGroupRequest;
import kitchenpos.table.application.dto.TableGroupResponse;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private DataCleaner dataCleaner;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        dataCleaner.clear();
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create_tableGroup() {
        // given
        final OrderTable orderTable1 = new OrderTable(5, true);
        final OrderTable orderTable2 = new OrderTable(4, true);
        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        final TableGroupRequest request = new TableGroupRequest(List.of(
            new OrderTableIdRequest(orderTable1.getId()),
            new OrderTableIdRequest(orderTable2.getId())));

        // when
        final TableGroupResponse result = tableGroupService.create(request);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        final List<OrderTableResponse> orderTableResponse = result.getOrderTableResponses();
        assertSoftly(softly -> {
            softly.assertThat(orderTableResponse).hasSize(2);
            softly.assertThat(orderTableResponse.get(0).getTableGroupId()).isEqualTo(result.getId());
            softly.assertThat(orderTableResponse.get(1).getTableGroupId()).isEqualTo(result.getId());
        });
    }

    @DisplayName("주문 테이블 그룹의 테이블 개수가 1개 혹은 0개이면 주문 테이블 그룹을 생성할 수 없다.")
    @MethodSource("getOrderTable")
    @ParameterizedTest
    void create_tableGroup_fail_with_table_cont_0_or_1(final List<OrderTableIdRequest> orderTableIds) {
        // given
        orderTableRepository.save(new OrderTable(4));
        final TableGroupRequest wrongRequest = new TableGroupRequest(orderTableIds);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(wrongRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블 그룹할 주문 테이블은 2개 이상이어야 합니다.");
    }

    private static Stream<Arguments> getOrderTable() {
        return Stream.of(
            Arguments.of(Collections.emptyList()),
            Arguments.of(List.of(new OrderTableIdRequest(1L)))
        );
    }

    @DisplayName("주문 테이블 그룹내의 테이블 개수와 DB에 저장된 주문 테이블의 개수가 다르면 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_tableGroup_fail_with_different_count_of_orderTable() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(5));
        final TableGroupRequest wrongRequest = new TableGroupRequest(List.of(
            new OrderTableIdRequest(2L),
            new OrderTableIdRequest(5L)));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(wrongRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("실제 존재하지 않은 주문 테이블이 포함되어 있습니다.");
    }

    @DisplayName("비어있지 않는 주문 테이블이있다면 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_tableGroup_fail_with_not_empty_table() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(5));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(4));
        final TableGroupRequest wrongRequest = new TableGroupRequest(List.of(
            new OrderTableIdRequest(orderTable1.getId()),
            new OrderTableIdRequest(orderTable2.getId())));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(wrongRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블이 비어있지 않거나 이미 다른 그룹에 포함된 주문 테이블은 새로운 테이블 그룹에 속할 수 없습니다.");
    }

    @DisplayName("이미 다른 테이블 그룹에 포함된 주문 테이블이있다면 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_tableGroup_fail_with_already_grouped_table() {
        // given
        final TableGroup otherTableGroup = tableGroupRepository.save(TableGroup.forSave());
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(5));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(4));
        orderTable1.joinTableGroup(otherTableGroup);

        final TableGroupRequest wrongRequest = new TableGroupRequest(List.of(
            new OrderTableIdRequest(orderTable1.getId()),
            new OrderTableIdRequest(orderTable2.getId())));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(wrongRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블이 비어있지 않거나 이미 다른 그룹에 포함된 주문 테이블은 새로운 테이블 그룹에 속할 수 없습니다.");
    }

    @Transactional
    @DisplayName("주문 테이블 그룹에서 주문 테이블을 분리할 수 있다.")
    @Test
    void ungroup() {
        // given
        final TableGroup tableGroup = TableGroup.forSave();
        tableGroupRepository.save(tableGroup);
        final OrderTable orderTable1 = new OrderTable(5, true);
        final OrderTable orderTable2 = new OrderTable(4, true);
        orderTable1.joinTableGroup(tableGroup);
        orderTable2.joinTableGroup(tableGroup);
        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        final Order order1 = new Order(orderTable1.getId());
        final Order order2 = new Order(orderTable2.getId());
        order1.changeStatus(OrderStatus.COMPLETION.name());
        order2.changeStatus(OrderStatus.COMPLETION.name());
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTable1.getTableGroup()).isNull();
            softly.assertThat(orderTable1.isEmpty()).isTrue();
            softly.assertThat(orderTable2.getTableGroup()).isNull();
            softly.assertThat(orderTable2.isEmpty()).isTrue();
        });
    }

    @DisplayName("주문 정보의 상태가 COOKING 혹은 MEAL이면 주문 테이블 그룹에서 주문 테이블을 분리할 수 없다.")
    @Test
    void ungroup_fail_with_table_order_status_COOKING_and_MEAL() {
        // given
        final TableGroup tableGroup = TableGroup.forSave();
        tableGroupRepository.save(tableGroup);
        final OrderTable orderTable1 = new OrderTable(5);
        final OrderTable orderTable2 = new OrderTable(4);
        orderTable1.joinTableGroup(tableGroup);
        orderTable2.joinTableGroup(tableGroup);
        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);
        orderRepository.save(new Order(orderTable1.getId()));
        orderRepository.save(new Order(orderTable2.getId()));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("현재 요리중이거나 식사 중인 경우 그룹해제를 할 수 없습니다.");
    }
}
