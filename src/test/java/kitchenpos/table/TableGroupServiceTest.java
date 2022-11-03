package kitchenpos.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.exception.CanNotGroupException;
import kitchenpos.exception.NotEnoughForGroupingException;
import kitchenpos.exception.OrderNotCompletionException;
import kitchenpos.exception.OrderTableSizeException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.support.ServiceTest;
import kitchenpos.support.fixtures.MenuFixtures;
import kitchenpos.support.fixtures.MenuGroupFixtures;
import kitchenpos.support.fixtures.OrderFixtures;
import kitchenpos.support.fixtures.OrderLineItemFixtures;
import kitchenpos.support.fixtures.OrderTableFixtures;
import kitchenpos.support.fixtures.TableGroupFixtures;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupCreateRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("classpath:truncate.sql")
@SpringBootTest
class TableGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블 단체를 지정한다")
    void create() {
        // given
        final OrderTable orderTable1 = OrderTableFixtures.createEmptyTable(null);
        final OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        final OrderTableRequest orderTableRequest1 = new OrderTableRequest(savedOrderTable1.getId());

        final OrderTable orderTable2 = OrderTableFixtures.createEmptyTable(null);
        final OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);
        final OrderTableRequest orderTableRequest2 = new OrderTableRequest(savedOrderTable2.getId());

        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                Arrays.asList(orderTableRequest1, orderTableRequest2));

        // when
        final TableGroupResponse saved = tableGroupService.create(tableGroupCreateRequest);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getCreationDate()).isBeforeOrEqualTo(LocalDateTime.now()),
                () -> assertThat(saved.getOrderTables()).extracting("id")
                        .hasSize(2)
                        .contains(savedOrderTable1.getId(), savedOrderTable2.getId()),
                () -> assertThat(saved.getOrderTables()).extracting("empty")
                        .containsExactly(false, false)
        );
    }

    @Test
    @DisplayName("주문 테이블이 비어있을 때 단체를 지정하려고 하면 예외가 발생한다")
    void createWithEmptyOrderTable() {
        // given
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isExactlyInstanceOf(NotEnoughForGroupingException.class);
    }

    @Test
    @DisplayName("주문 테이블이 1개 이하일 때 단체를 지정하려고 하면 예외가 발생한다")
    void createWithFewOrderTable() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createEmptyTable(null);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final OrderTableRequest orderTableRequest = new OrderTableRequest(savedOrderTable.getId());

        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                Collections.singletonList(orderTableRequest));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isExactlyInstanceOf(NotEnoughForGroupingException.class);
    }

    @Test
    @DisplayName("존재하지 않은 주문 테이블에 대해서 단체를 지정하려고 하면 예외가 발생한다")
    void createWithNotExistOrderTables() {
        // given
        final OrderTableRequest orderTableRequest1 = new OrderTableRequest(-1L);
        final OrderTableRequest orderTableRequest2 = new OrderTableRequest(-2L);

        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                Arrays.asList(orderTableRequest1, orderTableRequest2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isExactlyInstanceOf(OrderTableSizeException.class);
    }

    @Test
    @DisplayName("주문을 등록할 수 없는 주문 테이블에 대해서 단체를 지정하려고 하면 예외가 발생한다")
    void createWithNotEmptyOrderTables() {
        // given
        final OrderTable orderTable1 = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable NotEmptyOrderTable = orderTableRepository.save(orderTable1);
        final OrderTableRequest NotEmptyOrderTableRequest = new OrderTableRequest(NotEmptyOrderTable.getId());

        final OrderTable orderTable2 = OrderTableFixtures.createEmptyTable(null);
        final OrderTable emptyOrderTable = orderTableRepository.save(orderTable2);
        final OrderTableRequest emptyOrderTableRequest = new OrderTableRequest(emptyOrderTable.getId());

        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                Arrays.asList(NotEmptyOrderTableRequest, emptyOrderTableRequest));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isExactlyInstanceOf(CanNotGroupException.class);
    }

    @Test
    @DisplayName("이미 단체로 지정된 주문 테이블에 대해서 단체를 지정하려고 하면 예외가 발생한다")
    void createWithAlreadyGroupedOrderTables() {
        // given
        final TableGroup tableGroup = TableGroupFixtures.create();
        final TableGroup alreadyGroupedTable = tableGroupRepository.save(tableGroup);

        final OrderTable orderTable1 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        final OrderTable alreadyGroupedOrderTable1 = orderTableRepository.save(orderTable1);
        final OrderTableRequest alreadyGroupedOrderTableRequest = new OrderTableRequest(
                alreadyGroupedOrderTable1.getId());

        final OrderTable orderTable2 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        orderTableRepository.save(orderTable2);

        final OrderTable orderTable3 = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable orderTable = orderTableRepository.save(orderTable3);
        final OrderTableRequest orderTableRequest = new OrderTableRequest(orderTable.getId());

        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                Arrays.asList(orderTableRequest, alreadyGroupedOrderTableRequest));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isExactlyInstanceOf(CanNotGroupException.class);
    }

    @Test
    @DisplayName("테이블 단체를 해제한다")
    void ungroup() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final TableGroup tableGroup = TableGroupFixtures.create();
        final TableGroup alreadyGroupedTable = tableGroupRepository.save(tableGroup);

        final OrderTable orderTable1 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        final OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);

        final OrderTable orderTable2 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        final OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = OrderFixtures.COMPLETION_ORDER.createWithOrderTableIdAndOrderLineItems(
                savedOrderTable1.getId(), orderLineItem);
        orderRepository.save(order);

        // when
        tableGroupService.ungroup(alreadyGroupedTable.getId());

        // then
        final List<OrderTable> ungroupedOrderTable = orderTableRepository.findAllByIdIn(
                Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));

        assertAll(
                () -> assertThat(ungroupedOrderTable).isNotEmpty(),
                () -> assertThat(ungroupedOrderTable).extracting("tableGroupId")
                        .containsExactly(null, null),
                () -> assertThat(ungroupedOrderTable).extracting("empty")
                        .containsExactly(false, false)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING_ORDER", "MEAL_ORDER"})
    @DisplayName("주문의 상태가 COOKING, MEAL인 경우 테이블 단체를 해제하면 예외가 발생한다")
    void ungroupExceptionNotCompletionOrder(final OrderFixtures orderFixtures) {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final TableGroup tableGroup = TableGroupFixtures.create();
        final TableGroup alreadyGroupedTable = tableGroupRepository.save(tableGroup);

        final OrderTable orderTable1 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        final OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);

        final OrderTable orderTable2 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        orderTableRepository.save(orderTable2);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = orderFixtures.createWithOrderTableIdAndOrderLineItems(savedOrderTable1.getId(),
                orderLineItem);
        orderRepository.save(order);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(alreadyGroupedTable.getId()))
                .isExactlyInstanceOf(OrderNotCompletionException.class);
    }
}
