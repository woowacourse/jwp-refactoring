package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.IntegrationTest;
import kitchenpos.common.fixture.TMenu;
import kitchenpos.common.fixture.TMenuGroup;
import kitchenpos.common.fixture.TMenuProduct;
import kitchenpos.common.fixture.TOrder;
import kitchenpos.common.fixture.TOrderLineItem;
import kitchenpos.common.fixture.TOrderTable;
import kitchenpos.common.fixture.TProduct;
import kitchenpos.common.fixture.TTableGroup;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends IntegrationTest {

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = TOrderTable.builder()
            .numberOfGuests(0)
            .empty(true)
            .build();

        OrderTable orderTable2 = TOrderTable.builder()
            .numberOfGuests(0)
            .empty(true)
            .build();

        this.orderTable1 = tableService.create(orderTable1);
        this.orderTable2 = tableService.create(orderTable2);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        TableGroup tableGroup = tableGroupService.create(TTableGroup.builder()
            .orderTables(Arrays.asList(
                orderTable1,
                orderTable2
            )).build());

        assertThat(tableGroup.getOrderTables())
            .extracting("empty")
            .containsExactlyInAnyOrder(false, false);
    }

    @DisplayName("테이블 그룹이 2보다 작으면 예외.")
    @Test
    void create_tableNumberMustOverThen2() {
        assertThatThrownBy(() -> tableGroupService.create(TTableGroup.builder()
            .orderTables(Arrays.asList(
                orderTable1
            )).build())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹의 테이블은 중복을 허용하지 않는다.")
    @Test
    void create_duplicate() {
        tableGroupService.create(TTableGroup.builder()
            .orderTables(Arrays.asList(
                orderTable1,
                orderTable2
            )).build());

        assertThatThrownBy(() -> tableGroupService.create(TTableGroup.builder()
            .orderTables(Arrays.asList(
                orderTable1,
                orderTable2
            )).build())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 주문 테이블은 비워져 있어야 한다")
    @Test
    void create_tableMustBeEmpty() {
        tableService.changeEmpty(orderTable1.getId(), TOrderTable.builder().empty(false).build());

        assertThatThrownBy(() -> tableGroupService.create(TTableGroup.builder()
            .orderTables(Arrays.asList(
                orderTable1,
                orderTable2
            )).build())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 주문 테이블은 다른 그룹에 속해있어면 안된다.")
    @Test
    void create_groupDiff() {
        OrderTable orderTable3 = tableService.create(TOrderTable.builder()
            .numberOfGuests(4)
            .empty(true)
            .build());

        tableGroupService.create(TTableGroup.builder()
            .orderTables(Arrays.asList(
                orderTable1,
                orderTable3
            )).build());

        assertThatThrownBy(() -> tableGroupService.create(TTableGroup.builder()
            .orderTables(Arrays.asList(
                orderTable1,
                orderTable2
            )).build())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 그룹을 해제할 수 없다.")
    @Test
    void ungroup() {
        TableGroup tableGroup = tableGroupService.create(TTableGroup.builder()
            .orderTables(Arrays.asList(
                orderTable1,
                orderTable2
            )).build());

        assertThatCode(() -> tableGroupService.ungroup(tableGroup.getId()))
            .doesNotThrowAnyException();
    }

    @DisplayName("주문 테이블의 주문이 COKKING이면 비 그룹화가 불가능 하다.")
    @Test
    void ungroup_inCooking() {
        MenuGroup recommendMenu = menuGroupService.create(TMenuGroup.builder()
            .name("추천메뉴")
            .build()
        );

        TableGroup tableGroup = tableGroupService.create(TTableGroup.builder()
            .orderTables(Arrays.asList(
                orderTable1,
                orderTable2
            )).build());

        Product friedChicken = productService.create(TProduct.후라이드());
        MenuProduct menuProduct = TMenuProduct.builder()
            .productId(friedChicken.getId())
            .quantity(2L)
            .builder();

        Menu menu = menuService.create(TMenu.builder()
            .name("후라이드+후라이드")
            .price(BigDecimal.valueOf(19000))
            .menuGroupId(recommendMenu.getId())
            .menuProducts(Collections.singletonList(menuProduct))
            .build());

        orderService.create(TOrder.builder()
            .orderTableId(orderTable1.getId())
            .orderLineItems(Collections.singletonList(TOrderLineItem.builder()
                .menuId(menu.getId())
                .quantity(1L)
                .build()
            )).build());

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 주문이 MEAL이면 비 그룹화가 불가능 하다.")
    @Test
    void ungroup_inMeal() {
        MenuGroup recommendMenu = menuGroupService.create(TMenuGroup.builder()
            .name("추천메뉴")
            .build()
        );

        TableGroup tableGroup = tableGroupService.create(TTableGroup.builder()
            .orderTables(Arrays.asList(
                orderTable1,
                orderTable2
            )).build());

        Product friedChicken = productService.create(TProduct.후라이드());
        MenuProduct menuProduct = TMenuProduct.builder()
            .productId(friedChicken.getId())
            .quantity(2L)
            .builder();

        Menu menu = menuService.create(TMenu.builder()
            .name("후라이드+후라이드")
            .price(BigDecimal.valueOf(19000))
            .menuGroupId(recommendMenu.getId())
            .menuProducts(Collections.singletonList(menuProduct))
            .build());

        Order order = orderService.create(TOrder.builder()
            .orderTableId(orderTable1.getId())
            .orderLineItems(Collections.singletonList(TOrderLineItem.builder()
                .menuId(menu.getId())
                .quantity(1L)
                .build()
            )).build());

        orderService.changeOrderStatus(order.getId(), TOrder.builder().orderStatus("MEAL").build());

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
