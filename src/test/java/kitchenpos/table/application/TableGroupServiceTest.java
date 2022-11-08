package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dto.request.OrderTableIdRequest;
import kitchenpos.dto.request.TableGroupsCreateRequest;
import kitchenpos.exception.InvalidOrderTableToGroupException;
import kitchenpos.exception.NotCompletedOrderTableException;
import kitchenpos.exception.NotEnoughOrderTablesSizeException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.OrderPrice;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.support.application.ServiceTestEnvironment;
import kitchenpos.support.fixture.MenuFixture;
import kitchenpos.support.fixture.MenuGroupFixture;
import kitchenpos.support.fixture.OrderFixture;
import kitchenpos.support.fixture.OrderLineItemFixture;
import kitchenpos.support.fixture.OrderTableFixture;
import kitchenpos.support.fixture.ProductFixture;
import kitchenpos.support.fixture.TableGroupFixture;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTestEnvironment {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 그룹을 등록할 수 있다.")
    void create() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);
        final OrderTable savedTable3 = serviceDependencies.save(orderTable3);

        TableGroupsCreateRequest tableGroupsCreateRequest = new TableGroupsCreateRequest(Arrays.asList(
                new OrderTableIdRequest(savedTable1.getId()),
                new OrderTableIdRequest(savedTable2.getId()),
                new OrderTableIdRequest(savedTable3.getId())
        ));

        // when
        final TableGroup actual = tableGroupService.create(tableGroupsCreateRequest);

        // then
        assertThat(actual.getOrderTables())
                .usingElementComparatorOnFields("id")
                .containsExactly(savedTable1, savedTable2, savedTable3);
    }

    @Test
    @DisplayName("2개 미만의 테이블을 테이블 그룹으로 등록할 수 없다.")
    void create_ExceptionOrderTablesLowerThanTwo() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);

        TableGroupsCreateRequest tableGroupsCreateRequest = new TableGroupsCreateRequest(Collections.singletonList(
                new OrderTableIdRequest(savedTable1.getId())
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupsCreateRequest))
                .isExactlyInstanceOf(NotEnoughOrderTablesSizeException.class);
    }

    @Test
    @DisplayName("등록되지 않은 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionNotSavedOrderTable() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);

        TableGroupsCreateRequest tableGroupsCreateRequest = new TableGroupsCreateRequest(Arrays.asList(
                new OrderTableIdRequest(savedTable1.getId()),
                new OrderTableIdRequest(savedTable2.getId()),
                new OrderTableIdRequest(-1L)
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupsCreateRequest))
                .isExactlyInstanceOf(InvalidOrderTableToGroupException.class);
    }

    @Test
    @DisplayName("비어있지 않는 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionEmptyOrderTable() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(false, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);
        final OrderTable savedTable3 = serviceDependencies.save(orderTable3);

        TableGroupsCreateRequest tableGroupsCreateRequest = new TableGroupsCreateRequest(Arrays.asList(
                new OrderTableIdRequest(savedTable1.getId()),
                new OrderTableIdRequest(savedTable2.getId()),
                new OrderTableIdRequest(savedTable3.getId())
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupsCreateRequest))
                .isExactlyInstanceOf(InvalidOrderTableToGroupException.class);
    }

    @Test
    @DisplayName("중복된 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionDuplicatedOrderTable() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);

        TableGroupsCreateRequest tableGroupsCreateRequest = new TableGroupsCreateRequest(Arrays.asList(
                new OrderTableIdRequest(savedTable1.getId()),
                new OrderTableIdRequest(savedTable2.getId()),
                new OrderTableIdRequest(savedTable2.getId())
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupsCreateRequest))
                .isExactlyInstanceOf(InvalidOrderTableToGroupException.class);
    }

    @Test
    @DisplayName("이미 테이블 그룹에 속한 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionAlreadyCreatedTableGroup() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);
        final OrderTable savedTable3 = serviceDependencies.save(orderTable3);

        final TableGroup tableGroup1 = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);
        final TableGroup savedTableGroup1 = serviceDependencies.save(tableGroup1);
        savedTableGroup1.addOrderTable(savedTable1);
        serviceDependencies.save(savedTable1);

        TableGroupsCreateRequest tableGroupsCreateRequest = new TableGroupsCreateRequest(Arrays.asList(
                new OrderTableIdRequest(savedTable1.getId()),
                new OrderTableIdRequest(savedTable2.getId()),
                new OrderTableIdRequest(savedTable3.getId())
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupsCreateRequest))
                .isExactlyInstanceOf(InvalidOrderTableToGroupException.class);
    }

    @Test
    @DisplayName("특정 테이블 그룹을 해제할 수 있다.")
    void ungroup() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);
        final OrderTable savedTable3 = serviceDependencies.save(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);
        final TableGroup savedTableGroup = serviceDependencies.save(tableGroup);

        // when, then
        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("해제하려는 그룹에 속한 주문 테이블이 조리나 식사 상태면 안된다.")
    void ungroup_exceptionOrderTableCookingOrMeal(final OrderStatus orderStatus) {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);
        final OrderTable savedTable3 = serviceDependencies.save(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);
        final TableGroup savedTableGroup = serviceDependencies.save(tableGroup);

        final Menu savedMenu = saveValidMenu();
        final OrderLineItem orderLineItem = OrderLineItemFixture.create(savedMenu.getName(), new OrderPrice(savedMenu.getPrice()));
        final Order order = OrderFixture.create(savedTable1.getId(), orderStatus, orderLineItem);
        serviceDependencies.save(order);

        savedTableGroup.addOrderTable(savedTable1);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(NotCompletedOrderTableException.class);
    }

    private Menu saveValidMenu() {
        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = serviceDependencies.save(product1);
        final Product savedProduct2 = serviceDependencies.save(product2);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = serviceDependencies.save(menuGroup1);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup1.getId(), 2000L,
                Arrays.asList(savedProduct1.getId(), savedProduct2.getId()),
                Arrays.asList(1000L, 1000L));
        return serviceDependencies.save(menu);
    }
}
