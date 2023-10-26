package kitchenpos.integration;

import kitchenpos.menu.Menu;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.table.TableService;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.TableGroupService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableIntegrationTest extends IntegrationTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Nested
    class 테이블을_비울_때 {

        @Test
        void 그룹이_되어_있으면_비울_수_없다() {
            OrderTable orderTable1 = tableService.create(new OrderTable(1, true));
            OrderTable orderTable2 = tableService.create(new OrderTable(2, true));

            tableGroupService.create(List.of(orderTable1.getId(), orderTable2.getId()));

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), true))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("그룹된 테이블을 비울 수 없습니다.");
        }

        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @ParameterizedTest
        void 주문_상태가_완료가_아니면_비울_수_없다(OrderStatus orderStatus) {
            Menu menu = fixtureFactory.메뉴_생성(fixtureFactory.메뉴_그룹_생성().getId(), fixtureFactory.제품_생성());
            OrderTable orderTable = tableService.create(new OrderTable(1, false));
            Order order = new Order(List.of(new OrderLineItem(menu.getId(), 1)), orderTable.getId());
            order.changeOrderStatus(orderStatus);
            orderRepository.save(order);

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), true))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("조리중 또는 식사중인 테이블은 비울 수 없습니다.");
        }

        @Test
        void 존재하지_않는_테이블을_비울_수_없다() {
            assertThatThrownBy(() -> tableService.changeEmpty(-1L, true))
                    .hasMessageContaining("주문 테이블이 존재하지 않습니다.");
        }
    }

    @Nested
    class 테이블을_그룹_생성 {

        @Test
        void 정상_생성_테스트() {
            OrderTable orderTable = tableService.create(new OrderTable(1, true));
            OrderTable orderTable2 = tableService.create(new OrderTable(2, true));

            TableGroup tableGroup = tableGroupService.create(List.of(orderTable.getId(), orderTable2.getId()));

            List<OrderTable> results = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

            assertThat(results).hasSize(2);
        }

        @Test
        void 두개_미만의_테이블로_그룹할_수_없다() {
            OrderTable orderTable = tableService.create(new OrderTable(1, true));

            assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable.getId())))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테이블 그룹은 2개 이상의 테이블로 구성되어야 합니다.");
        }

        @Test
        void 비어있지_않은_테이블은_그룹할_수_없다() {
            OrderTable orderTable = tableService.create(new OrderTable(1, false));
            OrderTable orderTable2 = tableService.create(new OrderTable(2, true));

            assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable.getId(), orderTable2.getId())))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("비어있지 않은 테이블은 그룹으로 지정할 수 없습니다.");
        }

        @Test
        void 이미_그룹으로_지정된_테이블은_그룹할_수_없다() {
            OrderTable orderTable = tableService.create(new OrderTable(1, true));
            OrderTable orderTable2 = tableService.create(new OrderTable(2, true));

            tableGroupService.create(List.of(orderTable.getId(), orderTable2.getId()));

            assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable.getId(), orderTable2.getId())))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블을_비어있지_않은_상태로_만든다() {
            OrderTable orderTable = tableService.create(new OrderTable(1, true));
            OrderTable orderTable2 = tableService.create(new OrderTable(2, true));

            TableGroup tableGroup = tableGroupService.create(List.of(orderTable.getId(), orderTable2.getId()));

            List<OrderTable> results = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

            assertAll(
                    () -> assertThat(results).isNotEmpty(),
                    () -> assertThat(results).allMatch(OrderTable::isNotEmpty)
            );
        }
    }

    @Nested
    class 테이블_그룹_해제 {

        @Test
        void 정상_해제_테스트() {
            OrderTable orderTable = tableService.create(new OrderTable(1, true));
            OrderTable orderTable2 = tableService.create(new OrderTable(2, true));

            TableGroup tableGroup = tableGroupService.create(List.of(orderTable.getId(), orderTable2.getId()));

            tableGroupService.ungroup(tableGroup.getId());

            List<OrderTable> results = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

            assertThat(results).isEmpty();
        }

        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @ParameterizedTest
        void 주문이_완료가_아니면_그룹_해제할_수_없다(OrderStatus orderStatus) {
            Menu menu = fixtureFactory.메뉴_생성(fixtureFactory.메뉴_그룹_생성().getId(), fixtureFactory.제품_생성());
            OrderTable orderTable = tableService.create(new OrderTable(1, true));
            OrderTable orderTable2 = tableService.create(new OrderTable(2, true));
            TableGroup tableGroup = tableGroupService.create(List.of(orderTable.getId(), orderTable2.getId()));
            Order order = new Order(List.of(new OrderLineItem(menu.getId(), 1)), orderTable.getId());
            order.changeOrderStatus(orderStatus);
            orderRepository.save(order);

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("조리중 또는 식사중인 테이블은 그룹을 해제할 수 없습니다.");
        }
    }
}
