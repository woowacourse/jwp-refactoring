package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.IntegrationTest;
import kitchenpos.common.fixture.TMenu;
import kitchenpos.common.fixture.TMenuGroup;
import kitchenpos.common.fixture.TMenuProduct;
import kitchenpos.common.fixture.TOrder;
import kitchenpos.common.fixture.TOrderLineItem;
import kitchenpos.common.fixture.TOrderTable;
import kitchenpos.common.fixture.TProduct;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest extends IntegrationTest {

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        OrderTable orderTable = TOrderTable.builder()
            .numberOfGuests(0)
            .empty(true)
            .build();

        OrderTable savedOrderTable = tableService.create(orderTable);
        assertThat(savedOrderTable)
            .usingRecursiveComparison()
            .ignoringFields("id", "tableGroupId")
            .isEqualTo(orderTable);
    }

    @DisplayName("numberOfGuests, empty 를 제외한 데이터가 들어와도 무시된다.")
    @Test
    void create_ignoreOtherFields() {
        OrderTable orderTable = TOrderTable.builder()
            .id(9999L)
            .numberOfGuests(0)
            .tableGroupId(1L)
            .empty(true)
            .build();

        OrderTable savedOrderTable = tableService.create(orderTable);
        assertThat(savedOrderTable)
            .usingRecursiveComparison()
            .ignoringFields("id", "tableGroupId")
            .isEqualTo(orderTable);
    }

    @DisplayName("전체 주문 테이블을 반환한다.")
    @Test
    void list() {
        List<OrderTable> orderTables = TOrderTable.multiBuilder()
            .createDefault(10)
            .build();

        orderTables.forEach(tableService::create);

        assertThat(tableService.list()).hasSize(10);
    }

    @DisplayName("테이블 상태(empty)를 변경한다 true -> false")
    @Test
    void changeEmpty_trueToFalse() {
        OrderTable orderTable = TOrderTable.builder()
            .numberOfGuests(4)
            .empty(true)
            .build();

        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderTable falseOrderTable = TOrderTable.builder()
            .empty(false)
            .build();

        OrderTable changedOrderTable = tableService
            .changeEmpty(savedOrderTable.getId(), falseOrderTable);

        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("테이블 상태(empty)를 변경한다 false -> true")
    @Test
    void changeEmpty_falseToTrue() {
        OrderTable orderTable = TOrderTable.builder()
            .numberOfGuests(4)
            .empty(false)
            .build();

        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderTable falseOrderTable = TOrderTable.builder()
            .empty(true)
            .build();

        final OrderTable orderTable1 = tableService
            .changeEmpty(savedOrderTable.getId(), falseOrderTable);

        assertThat(orderTable1.isEmpty()).isTrue();
    }

    @DisplayName("테이블이 그룹화 되어있다면 상태 변경이 불가능 하다.")
    @Test
    void changeEmpty_orderStatus() {
        OrderTable orderTable = TOrderTable.builder()
            .numberOfGuests(4)
            .tableGroupId(1L)
            .empty(false)
            .build();

        Long orderTableId = orderTable.getId();
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블과 관련된 주문의 상태가 COOKING인 경우 주문 가능 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_cooking() {
        OrderTable orderTable = tableService.create(TOrderTable.builder()
            .numberOfGuests(4)
            .empty(true)
            .build());

        MenuGroup recommendMenu = menuGroupService.create(TMenuGroup.builder()
            .name("추천메뉴")
            .build()
        );

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

        tableService.changeEmpty(orderTable.getId(), TOrderTable.builder().empty(false).build());

        orderService.create(TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(Collections.singletonList(TOrderLineItem.builder()
                .menuId(menu.getId())
                .quantity(1L)
                .build()
            )).build());

        Long orderTableId = orderTable.getId();
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블과 관련된 주문의 상태가 MEAL인 경우 주문 가능 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_meal() {
        OrderTable orderTable = tableService.create(TOrderTable.builder()
            .numberOfGuests(4)
            .empty(true)
            .build());

        MenuGroup recommendMenu = menuGroupService.create(TMenuGroup.builder()
            .name("추천메뉴")
            .build()
        );

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

        tableService.changeEmpty(orderTable.getId(), TOrderTable.builder().empty(false).build());

        Order order = orderService.create(TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(Collections.singletonList(TOrderLineItem.builder()
                .menuId(menu.getId())
                .quantity(1L)
                .build()
            )).build());

        orderService.changeOrderStatus(order.getId(), order);

        Long orderTableId = orderTable.getId();
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = tableService.create(TOrderTable.builder()
            .numberOfGuests(1)
            .empty(false)
            .build());

        OrderTable changedOrderTable = tableService.changeNumberOfGuests(
            orderTable.getId(),
            TOrderTable.builder().numberOfGuests(4).build()
        );

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }
}
