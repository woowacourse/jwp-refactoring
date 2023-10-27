package kitchenpos.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import(TableGroupService.class)
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTableRequest 주문테이블1_요청;
    private OrderTableRequest 주문테이블2_요청;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = OrderTable.create(0, true);
        orderTable2 = OrderTable.create(0, true);

        orderTable1 = orderTableRepository.save(orderTable1);
        orderTable2 = orderTableRepository.save(orderTable2);

        주문테이블1_요청 = new OrderTableRequest(orderTable1.getId());
        주문테이블2_요청 = new OrderTableRequest(orderTable2.getId());
    }

    @DisplayName("테이블 그룹을 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(주문테이블1_요청, 주문테이블2_요청));

        // when
        TableGroup actual = tableGroupService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(tableGroupRepository.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getOrderTables()).extracting("empty")
                    .containsExactly(false, false);
            softly.assertThat(actual.getOrderTables()).extracting("tableGroup")
                    .containsExactly(actual, actual);
        });
    }

    @DisplayName("테이블 그룹 등록 시, 주문 테이블이 비어있을 경우 예외가 발생한다.")
    @Test
    void create_FailWithEmptyOrderLineItems() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시, 주문 테이블이 2개 미만일 경우 예외가 발생한다.")
    @Test
    void create_FailWithInvalidSizeOfOrderLineItems() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(주문테이블1_요청));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시, 주문 테이블이 중복될 경우 예외가 발생한다.")
    @Test
    void create_FailWithDuplicatedOrderTable() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(주문테이블1_요청, 주문테이블2_요청, 주문테이블2_요청));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 등록 시, 주문 테이블이 비어있지 않은 경우 예외가 발생한다.")
    @Test
    void create_FailWithNotEmptyOrderTable() {
        // given
        OrderTable orderTable3 = OrderTable.create(0, false);
        orderTable3 = orderTableRepository.save(orderTable3);

        OrderTableRequest 비어있지_않은_주문테이블 = new OrderTableRequest(orderTable3.getId());
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(주문테이블1_요청, 주문테이블2_요청, 비어있지_않은_주문테이블));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 등록 시, 주문 테이블이 이미 테이블 그룹에 속해있을 경우 예외가 발생한다.")
    @Test
    void create_FailWithOrderTableAlreadyHasTableGroup() {
        // given
        tableGroupService.create(new TableGroupCreateRequest(List.of(주문테이블1_요청, 주문테이블2_요청)));
        OrderTableRequest 이미_테이블그룹에_속한_주문테이블 = new OrderTableRequest(주문테이블1_요청.getId());

        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(이미_테이블그룹에_속한_주문테이블, 주문테이블2_요청));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 정상적으로 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(주문테이블1_요청, 주문테이블2_요청));

        TableGroup 테이블그룹 = tableGroupService.create(request);

        tableGroupService.ungroup(테이블그룹.getId());

        // when & then
        assertSoftly(softly -> {
            softly.assertThat(테이블그룹.getOrderTables()).extracting("empty")
                    .containsExactly(false, false);
            softly.assertThat(테이블그룹.getOrderTables()).extracting("tableGroup")
                    .containsExactly(null, null);
        });

        tableGroupService.ungroup(테이블그룹.getId());

        TableGroup 그룹해제한_테이블그룹 = tableGroupRepository.findById(테이블그룹.getId()).orElseThrow(IllegalArgumentException::new);
        List<OrderTable> 그룹해제한_주문테이블 = orderTableRepository.findAllByTableGroupId(그룹해제한_테이블그룹.getId());

        assertThat(그룹해제한_주문테이블).hasSize(0);
    }

    @DisplayName("테이블 그룹 해제 시, 주문 테이블의 주문 상태가 COOKING, MEAL인 경우 예외가 발생한다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void ungroup_FailWithInvalidOrderStatus(OrderStatus invalidOrderStatus) {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(주문테이블1_요청, 주문테이블2_요청));

        TableGroup 테이블그룹 = tableGroupService.create(request);

        Order order = Order.create(orderTable1);
        order.changeOrderStatus(invalidOrderStatus);

        orderRepository.save(order);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
