package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.application.ordertable.TableGroupService;
import kitchenpos.application.ordertable.dto.TableGroupCreateRequest;
import kitchenpos.application.ordertable.dto.TableGroupResponse;
import kitchenpos.common.ServiceTest;
import kitchenpos.common.fixtures.MenuFixtures;
import kitchenpos.common.fixtures.OrderTableFixtures;
import kitchenpos.common.fixtures.TableGroupFixtures;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orertable.OrderTable;
import kitchenpos.domain.orertable.OrderTableRepository;
import kitchenpos.domain.orertable.TableGroup;
import kitchenpos.domain.orertable.TableGroupRepository;
import kitchenpos.domain.orertable.exception.TableGroupException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
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
            final TableGroupCreateRequest request = TableGroupFixtures.TABLE_GROUP1_CREATE_REQUEST();

            final OrderTable orderTable1 = OrderTableFixtures.ORDER_TABLE1();
            final OrderTable orderTable2 = OrderTableFixtures.ORDER_TABLE1();
            orderTableRepository.save(orderTable1);
            orderTableRepository.save(orderTable2);

            // when
            final TableGroupResponse response = tableGroupService.create(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                softly.assertThat(response.getCreatedDate()).isNotNull();
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
            final OrderTable orderTable1 = OrderTableFixtures.ORDER_TABLE1();
            final OrderTable orderTable2 = OrderTableFixtures.ORDER_TABLE1();

            TableGroup tableGroup = TableGroup.create();
            TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

            orderTable1.updateTableGroup(savedTableGroup);
            orderTable2.updateTableGroup(savedTableGroup);

            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
            OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());
            OrderTable orderTable = orderTableRepository.findById(savedOrderTable1.getId()).get();

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(orderTable.getTableGroup()).isNull();
                softly.assertThat(orderTable.isEmpty()).isFalse();
                softly.assertThat(orderTable.getTableGroup()).isNull();
                softly.assertThat(orderTable.isEmpty()).isFalse();
            });
        }

        @Test
        @DisplayName("주문 테이블이 존재하고, 주문 상태가 조리 or 식사이면 예외가 발생한다.")
        void throws_existsOrderTableAndOrderStatusIsMealOrCooking() {
            // given
            final OrderTable orderTable1 = new OrderTable(OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS, false);
            final OrderTable orderTable2 = new OrderTable(OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS, false);
            final int orderLineItemSize = 1;

            final TableGroup tableGroup = TableGroup.create();
            final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

            orderTable1.updateTableGroup(savedTableGroup);
            orderTable2.updateTableGroup(savedTableGroup);

            final OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);

            final OrderLineItem orderLineItem = new OrderLineItem(MenuFixtures.MENU1_NAME, MenuFixtures.MENU1_PRICE, 1L);
            final Order order = Order.from(savedOrderTable1.getId(), orderLineItemSize, orderLineItemSize, List.of(orderLineItem));
            order.changeStatus(OrderStatus.MEAL);
            orderRepository.save(order);

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(TableGroupException.CannotUngroupStateByOrderStatusException.class)
                    .hasMessage("[ERROR] 주문 테이블의 주문 상태가 조리중이거나 식사중일 때 테이블 그룹을 해제할 수 없습니다.");
        }
    }
}
