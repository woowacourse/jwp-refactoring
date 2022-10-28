package kitchenpos.dao;

import static kitchenpos.support.MenuFixture.MENU_PRICE_10000;
import static kitchenpos.support.MenuGroupFixture.MENU_GROUP_1;
import static kitchenpos.support.OrderFixture.ORDER_COOKING_1;
import static kitchenpos.support.OrderLineItemFixture.ORDER_LINE_ITEM_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_NOT_EMPTY_1;
import static kitchenpos.support.TableGroupFixture.TABLE_GROUP_NOW;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderLineItemDaoTest extends JdbcDaoTest {

    @Test
    void 주문항목을_저장할_수_있다() {
        // given
        final Menu menu = 메뉴_저장();
        final Order order = 주문_저장();

        final OrderLineItem orderLineItem = new OrderLineItem(order, menu, 10);

        // when
        final OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        // then
        assertThat(savedOrderLineItem.getSeq()).isEqualTo(1L);
    }

    @Test
    void 주문항목을_조회할_수_있다() {
        // given
        final Menu menu = 메뉴_저장();
        final Order order = 주문_저장();
        final OrderLineItem savedOrderLineItem = 주문항목을_저장한다(ORDER_LINE_ITEM_1.생성(order, menu));

        // when
        final OrderLineItem orderLineItem = orderLineItemDao.findById(savedOrderLineItem.getSeq())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(orderLineItem.getQuantity()).isEqualTo(1);
    }

    @Test
    void 모든_주문항목을_조회한다() {
        // given
        final Menu menu = 메뉴_저장();
        final Order order = 주문_저장();
        final OrderLineItem savedOrderLineItem = 주문항목을_저장한다(ORDER_LINE_ITEM_1.생성(order, menu));

        // when
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();

        // then
        assertThat(orderLineItems).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(savedOrderLineItem);
    }

    @Test
    void 주문아이디로_주문항목을_조회한다() {
        // given
        final Menu menu = 메뉴_저장();
        final Order order = 주문_저장();
        final OrderLineItem savedOrderLineItem = 주문항목을_저장한다(ORDER_LINE_ITEM_1.생성(order, menu));

        // when
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());

        // then
        assertThat(orderLineItems).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(savedOrderLineItem);
    }

    private Menu 메뉴_저장() {
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        return 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId));
    }

    private Order 주문_저장() {
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId)).getId();
        return 주문을_저장한다(ORDER_COOKING_1.주문항목_없이_생성(orderTableId));
    }
}
