package kitchenpos.application;

import kitchenpos.EntityFactory;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.dto.OrderUpdateRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.product.ui.dto.TableGroupCreateRequest;
import kitchenpos.product.ui.dto.TableGroupOrderTableRequest;
import kitchenpos.product.ui.dto.TableGroupResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private EntityFactory entityFactory;

    @Nested
    @DisplayName("단체 지정 생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("단체 지정을 생성할 수 있다")
        void create() {
            //given
            final OrderTable orderTable1 = entityFactory.saveOrderTable();
            final OrderTable orderTable2 = entityFactory.saveOrderTable();

            final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(new TableGroupOrderTableRequest(orderTable1.getId()),
                            new TableGroupOrderTableRequest(orderTable2.getId())));

            //when
            final TableGroupResponse tableGroup = tableGroupService.create(request);

            //then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(tableGroup.getId()).isNotNull();
                softAssertions.assertThat(tableGroup.getCreatedDate()).isNotNull();
                softAssertions.assertThat(tableGroup.getOrderTables()).hasSize(2);
            });
        }

        @Test
        @DisplayName("단체 지정을 생성할 때 주문 테이블이 1개 이하면 예외가 발생한다")
        void create_fail() {
            //given
            final OrderTable orderTable = entityFactory.saveOrderTable();

            final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(new TableGroupOrderTableRequest(orderTable.getId())));

            //when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("그룹 지정은 주문 테이블이 최소 2개여야 합니다.");
        }

        @Test
        @DisplayName("단체 지정을 생성할 때 요청 주문 테이블의 개수와 실제 주문 테이블의 개수가 다르면 예외가 발생한다")
        void create_fail2() {
            //given
            final OrderTable orderTable1 = entityFactory.saveOrderTable();
            final OrderTable orderTable2 = new OrderTable(0L, null, 4, false);

            final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(new TableGroupOrderTableRequest(orderTable1.getId()),
                            new TableGroupOrderTableRequest(orderTable2.getId())));

            //when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 주문 테이블입니다.");
        }

        @Test
        @DisplayName("단체 지정을 생성할 때 주문 테이블이 빈 테이블이 아니라면 예외가 발생한다")
        void create_fail3() {
            //given
            final OrderTable orderTable1 = entityFactory.saveOrderTable();
            final OrderTable orderTable2 = entityFactory.saveOrderTableWithNotEmpty();

            final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(new TableGroupOrderTableRequest(orderTable1.getId()),
                            new TableGroupOrderTableRequest(orderTable2.getId())));

            //when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 빈 테이블이 아니거나 이미 그룹 지정된 테이블입니다.");
        }

        @Test
        @DisplayName("단체 지정을 생성할 때 주문 테이블이 단체 지정 돼있으면 예외가 발생한다")
        void create_fail4() {
            //given
            final OrderTable orderTable1 = entityFactory.saveOrderTable();
            final OrderTable orderTable2 = entityFactory.saveOrderTable();
            final OrderTable orderTable3 = entityFactory.saveOrderTable();
            entityFactory.saveTableGroup(orderTable1, orderTable2);


            final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(new TableGroupOrderTableRequest(orderTable1.getId()),
                            new TableGroupOrderTableRequest(orderTable3.getId())));

            //when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 빈 테이블이 아니거나 이미 그룹 지정된 테이블입니다.");
        }
    }

    @Test
    @DisplayName("단체 지정을 삭제할 수 있다")
    void ungroup() {
        //given
        final OrderTable orderTable1 = entityFactory.saveOrderTable();
        final OrderTable orderTable2 = entityFactory.saveOrderTable();
        final TableGroup tableGroup = entityFactory.saveTableGroup(orderTable1, orderTable2);

        final List<OrderTable> beforeUngroup = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        //when
        tableGroupService.ungroup(tableGroup.getId());

        final List<OrderTable> afterUngroup = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(beforeUngroup).hasSize(2);
            softAssertions.assertThat(afterUngroup).isEmpty();
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("단체 지정을 삭제할 때 해당 주문 테이블 중 COOKING 또는 MEAL 상태가 존재하면 예외가 발생한다")
    void ungroup_fail(final OrderStatus status) {
        //given
        final OrderTable orderTable1 = entityFactory.saveOrderTableWithNotEmpty();
        final OrderTable orderTable2 = entityFactory.saveOrderTableWithNotEmpty();
        final TableGroup tableGroup = entityFactory.saveTableGroup(orderTable1, orderTable2);

        final Order order = entityFactory.saveOrder(orderTable1);
        final OrderUpdateRequest requestToChangeStatus = new OrderUpdateRequest(status);
        orderService.changeOrderStatus(order.getId(), requestToChangeStatus);

        //when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 중이거나 식사 중인 테이블입니다.");
    }
}
