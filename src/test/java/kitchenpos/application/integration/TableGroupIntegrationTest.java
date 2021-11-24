package kitchenpos.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.application.common.factory.MenuFactory;
import kitchenpos.application.common.factory.MenuGroupFactory;
import kitchenpos.application.common.factory.MenuProductFactory;
import kitchenpos.application.common.factory.OrderFactory;
import kitchenpos.application.common.factory.OrderLineItemFactory;
import kitchenpos.application.common.factory.OrderTableFactory;
import kitchenpos.application.common.factory.ProductFactory;
import kitchenpos.application.common.factory.TableGroupFactory;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TableGroupIntegrationTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @DisplayName("생성 - 테이블 그룹을 생성(등록)할 수 있다.")
    @Test
    void create_success() {
        // given
        OrderTable savedOrderTable1 = tableService.create(OrderTableFactory.create(0, true));
        OrderTable savedOrderTable2 = tableService.create(OrderTableFactory.create(0, true));

        // when
        TableGroup savedTableGroup = tableGroupService.create(
            TableGroupFactory.create(LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2))
        );

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getCreatedDate()).isBefore(LocalDateTime.now());
        assertThat(savedTableGroup.getOrderTables())
            .hasSize(2)
            .containsExactlyInAnyOrder(savedOrderTable1, savedOrderTable2)
            .allMatch(orderTable -> orderTable.getTableGroupId().equals(savedTableGroup.getId()));
    }

    @DisplayName("생성 - 테이블 그룹에 속할 테이블 수는 적어도 2개 이상이어야 한다.")
    @Test
    void create_tableLessThanTwo_fail() {
        // given
        OrderTable savedOrderTable1 = tableService.create(OrderTableFactory.create(0, true));

        // when, then
        TableGroup tableGroup = TableGroupFactory
            .create(LocalDateTime.now(), Arrays.asList(savedOrderTable1));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 테이블 그룹에 속할 테이블 수는 적어도 2개 이상이어야 한다.");
    }

    @DisplayName("생성 - 테이블 그룹에 속할 테이블은 모두 등록된 테이블 이어야 한다.")
    @Test
    void create_nonExistsTable_fail() {
        // given
        OrderTable savedOrderTable = tableService.create(OrderTableFactory.create(0, true));
        OrderTable nonSavedOrderTable = OrderTableFactory.create(0, true);

        // when, then
        TableGroup tableGroup = TableGroupFactory
            .create(LocalDateTime.now(), Arrays.asList(savedOrderTable, nonSavedOrderTable));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 테이블 그룹에 속할 테이블은 모두 등록된 테이블이어야 한다.");
    }

    @DisplayName("생성 - 테이블 그룹에 속할 테이블들은 모두 비어있어야 한다.")
    @Test
    void create_nonEmptyTable_fail() {
        // given
        OrderTable savedOrderTable1 = tableService.create(OrderTableFactory.create(5, false));
        OrderTable savedOrderTable2 = tableService.create(OrderTableFactory.create(0, true));

        // when
        TableGroup tableGroup = TableGroupFactory
            .create(LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 테이블 그룹에 속할 테이블들은 모두 비어있어야 한다.");
    }

    @DisplayName("생성 - 테이블 그룹에 속할 테이블들은 모두 이미 다른 그룹에 속해있으면 안된다.")
    @Test
    void create_alreadyHasGroup_fail() {
        // given
        OrderTable savedOrderTable1 = tableService.create(OrderTableFactory.create(0, true));
        OrderTable savedOrderTable2 = tableService.create(OrderTableFactory.create(0, true));
        OrderTable savedOrderTable3 = tableService.create(OrderTableFactory.create(0, true));

        TableGroup savedTableGroup = tableGroupService.create(
            TableGroupFactory.create(LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2))
        );

        // when
        TableGroup tableGroup = TableGroupFactory
            .create(LocalDateTime.now(), Arrays.asList(savedOrderTable2, savedOrderTable3));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 해제 - 테이블 그룹을 해제할 수 있다.")
    @Test
    void ungroup_success() {
        // given
        OrderTable savedOrderTable1 = tableService.create(OrderTableFactory.create(0, true));
        OrderTable savedOrderTable2 = tableService.create(OrderTableFactory.create(0, true));

        TableGroup savedTableGroup = tableGroupService.create(
            TableGroupFactory.create(LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2))
        );

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables)
            .hasSize(2)
            .allMatch(orderTable -> !orderTable.isEmpty())
            .allMatch(orderTable -> orderTable.getTableGroupId() == null);
    }

    @DisplayName("그룹 해제 - 테이블 그룹에 속한 테이블의 주문 상태중 하나라도 COOKING이거나 MEAL이면 해제할 수 없다.")
    @Test
    void ungroup_orderStatus_fail() {
        // given
        Product savedProduct = productService.create(
            ProductFactory.create("후라이드", BigDecimal.valueOf(16_000))
        );
        MenuProduct menuProduct = MenuProductFactory.create(savedProduct, 4);

        MenuGroup savedMenuGroup = menuGroupService.create(MenuGroupFactory.create("두마리메뉴"));

        Menu savedMenu = menuService.create(MenuFactory.create(
            "후라이드치킨",
            BigDecimal.valueOf(16_000),
            savedMenuGroup,
            Arrays.asList(menuProduct))
        );
        OrderLineItem orderLineItem1 = OrderLineItemFactory.create(savedMenu, 5);

        OrderTable savedOrderTable1 = tableService.create(OrderTableFactory.create(0, true));
        OrderTable savedOrderTable2 = tableService.create(OrderTableFactory.create(0, true));

        TableGroup savedTableGroup = tableGroupService.create(
            TableGroupFactory.create(LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2))
        );

        // when
        Order savedOrder = orderService.create(
            OrderFactory.create(savedOrderTable1, Arrays.asList(orderLineItem1))
        );
        savedOrder.setOrderStatus(OrderStatus.COOKING);
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
