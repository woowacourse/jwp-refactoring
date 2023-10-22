package kitchenpos.dao;

import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemDaoTest extends DaoTest {

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @DisplayName("OrderLineItem 저장 - 성공")
    @Test
    void save_Success() {
        // given && when
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        final MenuGroup saveMenuGroup = saveMenuGroup("메뉴 그룹");
        final Menu saveMenu = saveMenu("메뉴", 10, saveMenuGroup, null);
        final OrderLineItem saveOrderLineItem = saveOrderLineItem(saveOrder.getId(), saveMenu.getId(), 3);

        // then
        assertThat(saveOrderLineItem.getId()).isNotNull();
    }

    @DisplayName("OrderLineItem ID로 OrderLineItem 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnOrderLineItem() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        final MenuGroup saveMenuGroup = saveMenuGroup("메뉴 그룹");
        final Menu saveMenu = saveMenu("메뉴", 10, saveMenuGroup, null);
        final OrderLineItem saveOrderLineItem = saveOrderLineItem(saveOrder.getId(), saveMenu.getId(), 3);

        // when
        final OrderLineItem foundOrderLineItem = orderLineItemDao.findById(saveOrderLineItem.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 orderLineItem이 없습니다."));

        // then
        assertThat(foundOrderLineItem.getId()).isEqualTo(saveOrderLineItem.getId());
    }

    @DisplayName("OrderLineItem ID로 OrderLineItem 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        final MenuGroup saveMenuGroup = saveMenuGroup("메뉴 그룹");
        final Menu saveMenu = saveMenu("메뉴", 10, saveMenuGroup, null);
        final OrderLineItem saveOrderLineItem = saveOrderLineItem(saveOrder.getId(), saveMenu.getId(), 3);

        // when
        final Optional<OrderLineItem> foundOrderLineItem = orderLineItemDao.findById(saveOrderLineItem.getId() + 1);

        assertThat(foundOrderLineItem.isPresent()).isFalse();
    }

    @DisplayName("전체 OrderLineItem 조회 - 성공")
    @Test
    void findAll_Success() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        final MenuGroup saveMenuGroup = saveMenuGroup("메뉴 그룹");
        final Menu saveMenu = saveMenu("메뉴", 10, saveMenuGroup, null);
        final OrderLineItem saveOrderLineItem1 = saveOrderLineItem(saveOrder.getId(), saveMenu.getId(), 3);
        final OrderLineItem saveOrderLineItem2 = saveOrderLineItem(saveOrder.getId(), saveMenu.getId(), 3);
        final OrderLineItem saveOrderLineItem3 = saveOrderLineItem(saveOrder.getId(), saveMenu.getId(), 3);

        // then
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();

        // then
        assertThat(orderLineItems).hasSize(3);
    }

    @DisplayName("Order ID로 OrderLineItems 조회 - 조회됨, Order ID에 매치되는 경우")
    @Test
    void findAllByOrderId_MatchedOrderId_ReturnOrderLineItems() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        final MenuGroup saveMenuGroup = saveMenuGroup("메뉴 그룹");
        final Menu saveMenu = saveMenu("메뉴", 10, saveMenuGroup, null);
        final OrderLineItem saveOrderLineItem1 = saveOrderLineItem(saveOrder.getId(), saveMenu.getId(), 3);
        final OrderLineItem saveOrderLineItem2 = saveOrderLineItem(saveOrder.getId(), saveMenu.getId(), 3);

        // when
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(saveOrder.getId());

        // then
        assertThat(orderLineItems).hasSize(2);
    }

    @DisplayName("Order ID로 OrderLineItems 조회 - 조회되지 않음, Order ID에 매치되지 않는 경우")
    @Test
    void findAllByOrderId_NotMatchedOrderId_ReturnEmpty() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        final MenuGroup saveMenuGroup = saveMenuGroup("메뉴 그룹");
        final Menu saveMenu = saveMenu("메뉴", 10, saveMenuGroup, null);
        final OrderLineItem saveOrderLineItem1 = saveOrderLineItem(saveOrder.getId(), saveMenu.getId(), 3);
        final OrderLineItem saveOrderLineItem2 = saveOrderLineItem(saveOrder.getId(), saveMenu.getId(), 3);

        // when
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(saveOrder.getId()+1);

        // then
        assertThat(orderLineItems).isEmpty();
    }
}
