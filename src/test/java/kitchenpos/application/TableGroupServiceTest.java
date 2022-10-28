package kitchenpos.application;

import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE1;
import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE2;
import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("단체 지정을 할 때 주문 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void create_ifOrderTableSizeLessThanTwo_throwsException() {
        // given
        final List<Long> orderTableIds = Arrays.asList(ORDER_TABLE1.create().getId());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 할 때 주문 테이블과 저장된 주문 테이블의 갯수가 다르면 예외가 발생한다.")
    @Test
    void create_ifContainsNotExistOrderTable_throwsException() {
        // given
        final List<Long> orderTableIds = Arrays.asList(ORDER_TABLE1.create().getId(),
                ORDER_TABLE2.createWithIdNull().getId());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable이 빈 테이블이 아니라면 예외가 발생한다.")
    @Test
    void create_ifOrderTableNotEmpty_throwsException() {
        // given
        final List<Long> orderTableIds = Arrays.asList(ORDER_TABLE1.create().getId(),
                ORDER_TABLE_NOT_EMPTY.create().getId());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable이 정상적으로 단체 지정이 된다.")
    @Test
    void create() {
        // given
        final List<Long> orderTableIds = Arrays.asList(ORDER_TABLE1.create().getId(), ORDER_TABLE2.create().getId());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(2)
        );
    }

    @DisplayName("주문 상태가 요리중이거나 식사중이면 예외가 발생한다.")
    @Test
    void ungroup_ifOrderStatusCookingOrMeal_throwsException() {
        // given
        final List<Long> orderTableIds = Arrays.asList(ORDER_TABLE1.create().getId(), ORDER_TABLE2.create().getId());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
        final TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

        final OrderLineItem orderLineItem = new OrderLineItem(1L, 3);
        final Order order = new Order(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(orderLineItem));
        final Order savedOrder = orderDao.save(order);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        final List<Long> orderTableIds = Arrays.asList(ORDER_TABLE1.create().getId(), ORDER_TABLE2.create().getId());
        final TableGroupRequest tableGroup = new TableGroupRequest(orderTableIds);
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when, then
        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .doesNotThrowAnyException();
    }
}
