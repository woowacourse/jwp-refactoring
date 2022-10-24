package kitchenpos.application;

import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE1;
import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.OrderTableFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("OrderTable을 생성한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = ORDER_TABLE1.createWithIdNull();

        // when
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("OrderTable들을 조회한다.")
    @Test
    void list() {
        // given
        final List<OrderTable> expected = OrderTableFixtures.createAll();

        // when
        final List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    @DisplayName("orderTableId가 존재하지 않은 경우 empty 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_notExistOrderTableId_throwsException() {
        // given
        final OrderTable orderTable = ORDER_TABLE1.create();

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(9L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable이 단체 지정이 된 경우 예외가 발생한다.")
    @Test
    void changeEmpty_tableGroupIdNonNull_throwsException() {
        // given
        final OrderTable orderTable = ORDER_TABLE1.create();
        orderTable.setTableGroupId(1L);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 포함된 주문이 요리중이거나 식사 중인 경우 예외가 발생한다.")
    @Test
    void changeEmpty_orderStatusCookingOrMeal_throwsException() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 3);
        final Order order = new Order(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(orderLineItem));
        final Order savedOrder = orderDao.save(order);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, ORDER_TABLE_NOT_EMPTY.create()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable의 empty 상태를 정상적으로 변경한다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable orderTable = ORDER_TABLE_NOT_EMPTY.create();

        // when
        final OrderTable changedOrderTable = tableService.changeEmpty(1L, orderTable);

        // then
        assertAll(
                () -> assertThat(changedOrderTable.getId()).isNotNull(),
                () -> assertThat(changedOrderTable.isEmpty()).isFalse()
        );
    }
}
