package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDaoTest extends DaoTest {

    @Autowired
    OrderDao orderDao;

    @DisplayName("Order 저장 - 성공")
    @Test
    void save_Success() {
        //given && when
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        // then
        assertThat(saveOrder.getId()).isNotNull();
    }

    @DisplayName("Order ID로 Order 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnOrder() {
        //given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        // when
        final Order foundOrder = orderDao.findById(saveOrder.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 order가 없습니다."));

        // then
        assertThat(foundOrder.getId()).isEqualTo(saveOrder.getId());
    }

    @DisplayName("Order ID로 Order 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        // when
        final Optional<Order> foundOrder = orderDao.findById(saveOrder.getId() + 1);

        // then
        assertThat(foundOrder.isPresent()).isFalse();
    }

    @DisplayName("전체 Order 조회 - 성공")
    @Test
    void findAll_Success() {
        //given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder1 = saveOrder(OrderStatus.COOKING, saveOrderTable, null);
        final Order saveOrder2 = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        // when
        final List<Order> orders = orderDao.findAll();

        // then
        assertThat(orders).hasSize(2);
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - True, 모두 매치")
    @Test
    void existsByOrderTableIdAndOrderStatusIn_MatchedOrderTableIdAndOrderStatus_ReturnTrue() {
        //given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        // when
        final boolean existsOrder
                = orderDao.existsByOrderTableIdAndOrderStatusIn(saveOrderTable.getId(), List.of(OrderStatus.COOKING.name()));

        // then
        assertThat(existsOrder).isTrue();
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - False, OrderStatus만 매치")
    @Test
    void existsByOrderTableIdAndOrderStatusIn_MatchedOnlyOrderStatus_ReturnFalse() {
        //given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        // when
        final boolean existsOrder
                = orderDao.existsByOrderTableIdAndOrderStatusIn(saveOrderTable.getId() + 1, List.of(OrderStatus.COOKING.name()));

        //then
        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - False, OrderTableId만 매치")
    @Test
    void existsByOrderTableIdAndOrderStatusIn_MatchedOnlyOrderTableId_ReturnFalse() {
        //given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        // when
        final boolean existsOrder
                = orderDao.existsByOrderTableIdAndOrderStatusIn(saveOrderTable.getId(), List.of(OrderStatus.COMPLETION.name()));

        //then
        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - False, 매치되는 것 없음")
    @Test
    void existsByOrderTableIdAndOrderStatusIn_MatchedNothing_ReturnFalse() {
        //given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);
        final Order saveOrder = saveOrder(OrderStatus.COOKING, saveOrderTable, null);

        // when
        final boolean existsOrder
                = orderDao.existsByOrderTableIdAndOrderStatusIn(saveOrderTable.getId() + 1, List.of(OrderStatus.COMPLETION.name()));

        //then
        assertThat(existsOrder).isFalse();
    }
}
