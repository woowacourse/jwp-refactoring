package kitchenpos.integration;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.service.OrderCreateService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.support.FixtureFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableIntegrationTest extends IntegrationTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderCreateService orderCreateService;

    @Autowired
    private FixtureFactory fixtureFactory;

    @Nested
    class 테이블을_비울_때 {

        @Test
        void 그룹이_되어_있으면_비울_수_없다() {
            OrderTable orderTable1 = tableService.create(new OrderTable(1, true, false));
            OrderTable orderTable2 = tableService.create(new OrderTable(2, true, false));

            tableGroupService.create(List.of(orderTable1.getId(), orderTable2.getId()));

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), true))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("그룹된 테이블을 비울 수 없습니다.");
        }

        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @ParameterizedTest
        void 주문_상태가_완료가_아니면_비울_수_없다(OrderStatus orderStatus) {
            Menu menu = fixtureFactory.메뉴_생성(fixtureFactory.메뉴_그룹_생성().getId(), fixtureFactory.제품_생성());
            OrderTable orderTable = tableService.create(new OrderTable(1, false, false));
            Order order = new Order(List.of(new OrderLineItem(menu.getId(), 1)));
            orderCreateService.create(orderTable.getId(), order);
            order.changeOrderStatus(orderStatus);

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), true))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("조리중 또는 식사중인 테이블은 비울 수 없습니다.");
        }

        @Test
        void 존재하지_않는_테이블을_비울_수_없다() {
            assertThatThrownBy(() -> tableService.changeEmpty(-1L, true))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 테이블이 존재하지 않습니다.");
        }
    }
}
