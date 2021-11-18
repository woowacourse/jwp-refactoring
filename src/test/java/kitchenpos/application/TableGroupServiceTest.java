package kitchenpos.application;

import kitchenpos.SpringBootTestSupport;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.order.OrderTableIdRequest;
import kitchenpos.ui.dto.tablegroup.TableGroupRequest;
import kitchenpos.ui.dto.tablegroup.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.MenuFixture.createMenu1;
import static kitchenpos.MenuFixture.createMenuGroup1;
import static kitchenpos.OrderFixture.createOrder;
import static kitchenpos.OrderFixture.createOrderLineItem;
import static kitchenpos.ProductFixture.createProduct1;
import static kitchenpos.TableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TableGroupServiceTest extends SpringBootTestSupport {

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 생성은")
    @Nested
    class Create extends SpringBootTestSupport {

        private TableGroupRequest request;
        private OrderTable orderTable1;
        private OrderTable orderTable2;

        @BeforeEach
        void setUp() {
            orderTable1 = save(createOrderTable(true));
            orderTable2 = save(createOrderTable(true));
        }

        @DisplayName("주문 테이블의 개수가 2 미만일 경우 생성할 수 없다.")
        @Test
        void createIfLessThanTwo() {
            request = new TableGroupRequest(Collections.singletonList(new OrderTableIdRequest(orderTable1.getId())));

            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 주문 테이블이 포함되어 있는 경우 생성할 수 없다.")
        @Test
        void createIfHasNotExistOrderTable() {
            request = new TableGroupRequest(Arrays.asList(
                    new OrderTableIdRequest(0L), new OrderTableIdRequest(0L)));

            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블들이 빈 테이블이 아닌 경우 생성할 수 없다.")
        @Test
        void createIfNotEmptyOrderTable() {
            orderTable2 = save(createOrderTable(false));
            request = new TableGroupRequest(Arrays.asList(
                    new OrderTableIdRequest(orderTable1.getId()), new OrderTableIdRequest(orderTable2.getId())));

            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정에 이미 속해있는 주문 테이블을 포함할 경우 생성할 수 없다.")
        @Test
        void createIfAlreadyInTableGroup() {
            final TableGroup tableGroup = save(new TableGroup());
            orderTable2 = save(createOrderTable(tableGroup));
            request = new TableGroupRequest(Arrays.asList(
                    new OrderTableIdRequest(orderTable1.getId()), new OrderTableIdRequest(orderTable2.getId())));

            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족하는 경우 생성할 수 있다.")
        @Test
        void create() {
            request = new TableGroupRequest(Arrays.asList(
                    new OrderTableIdRequest(orderTable1.getId()), new OrderTableIdRequest(orderTable2.getId())));

            final TableGroupResponse actual = tableGroupService.create(request);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getCreatedDate()).isNotNull(),
                    () -> assertThat(actual.getOrderTables()).hasSize(2),
                    () -> assertThat(actual.getOrderTables().get(0).isEmpty()).isFalse(),
                    () -> assertThat(actual.getOrderTables().get(1).isEmpty()).isFalse()
            );
        }
    }

    @DisplayName("단체 지정 해제는")
    @Nested
    class Ungroup extends SpringBootTestSupport {

        private MenuGroup menuGroup;
        private Product product;
        private Menu menu;
        private TableGroup tableGroup;
        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            menuGroup = save(createMenuGroup1());
            product = save(createProduct1());
            menu = save(createMenu1(menuGroup, Collections.singletonList(product)));
            tableGroup = save(new TableGroup());
            orderTable = save(createOrderTable(tableGroup));
        }

        @DisplayName("조리, 식사 상태의 주문 테이블을 포함한 경우 해제할 수 없다.")
        @Test
        void ungroupException() {
            save(createOrder(orderTable, OrderStatus.COOKING, Collections.singletonList(createOrderLineItem(menu))));

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족하는 경우 해제할 수 있다.")
        @Test
        void ungroup() {
            save(createOrder(orderTable, OrderStatus.COMPLETION, Collections.singletonList(createOrderLineItem(menu))));

            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        }
    }
}
