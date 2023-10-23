package kitchenpos.application;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static kitchenpos.common.fixtures.TableGroupFixtures.TABLE_GROUP1;
import static kitchenpos.common.fixtures.TableGroupFixtures.TABLE_GROUP1_CREATE_REQUEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.TableGroupException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

    @Nested
    @DisplayName("테이블 그룹 생성 시")
    class CreateTableGroup {

        @Test
        @DisplayName("생성에 성공한다.")
        void success() {
            // given
            final TableGroupCreateRequest request = TABLE_GROUP1_CREATE_REQUEST();

            final OrderTable orderTable1 = ORDER_TABLE1();
            final OrderTable orderTable2 = ORDER_TABLE1();
            orderTableRepository.save(orderTable1);
            orderTableRepository.save(orderTable2);

            // when
            final TableGroupResponse response = tableGroupService.create(request);
            final List<OrderTableResponse> orderTableResponses = response.getOrderTables().getOrderTables();

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                softly.assertThat(response.getCreatedDate()).isNotNull();
                softly.assertThat(orderTableResponses.get(0).getTableGroup()).isNotNull();
                softly.assertThat(orderTableResponses.get(1).getTableGroup()).isNotNull();
            });
        }
    }

    @Nested
    @DisplayName("테이블 그룹 해제 시")
    class UnGroup {

        @Test
        @DisplayName("해제에 성공한다.")
        void success() {
            // given
            final OrderTable orderTable1 = ORDER_TABLE1();
            final OrderTable orderTable2 = ORDER_TABLE1();

            TableGroup tableGroup = TABLE_GROUP1();
            TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

            orderTable1.confirmTableGroup(savedTableGroup);
            orderTable2.confirmTableGroup(savedTableGroup);

            orderTableRepository.save(orderTable1);
            orderTableRepository.save(orderTable2);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());
            final TableGroup upGrouppedTableGroup = tableGroupRepository.findById(savedTableGroup.getId()).get();
            final List<OrderTable> orderTables = upGrouppedTableGroup.getOrderTables();

            // then
            assertSoftly(softly -> {
                softly.assertThat(orderTables.get(0).getTableGroup()).isNull();
                softly.assertThat(orderTables.get(0).isEmpty()).isFalse();
                softly.assertThat(orderTables.get(1).getTableGroup()).isNull();
                softly.assertThat(orderTables.get(1).isEmpty()).isFalse();
            });
        }

        @Test
        @DisplayName("주문 테이블이 존재하고, 주문 상태가 조리 or 식사이면 예외가 발생한다.")
        void throws_existsOrderTableAndOrderStatusIsMealOrCooking() {
            // given
            final OrderTable orderTable1 = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false);
            final OrderTable orderTable2 = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false);

            final TableGroup tableGroup = TABLE_GROUP1();
            final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

            orderTable1.confirmTableGroup(savedTableGroup);
            orderTable2.confirmTableGroup(savedTableGroup);

            final OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);

            final Order order = Order.from(savedOrderTable1);
            order.changeStatus(OrderStatus.MEAL);
            orderRepository.save(order);

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(TableGroupException.CannotUngroupStateByOrderStatusException.class)
                    .hasMessage("[ERROR] 주문 테이블의 주문 상태가 조리중이거나 식사중일 때 테이블 그룹을 해제할 수 없습니다.");
        }
    }
}
