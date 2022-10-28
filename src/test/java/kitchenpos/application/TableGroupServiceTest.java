package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tableGroup.AddOrderTableToTableGroupRequest;
import kitchenpos.dto.tableGroup.CreateTableGroupRequest;

class TableGroupServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create()")
    class CreateMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 테이블 그룹을 생성한다.")
        void create() {
            // given
            OrderTable orderTable1 = createAndSaveOrderTable();
            OrderTable orderTable2 = createAndSaveOrderTable();
            CreateTableGroupRequest request = new CreateTableGroupRequest(
                new ArrayList<AddOrderTableToTableGroupRequest>() {{
                    add(new AddOrderTableToTableGroupRequest(orderTable1.getId()));
                    add(new AddOrderTableToTableGroupRequest(orderTable2.getId()));
                }}
            );

            // when
            TableGroup savedTableGroup = tableGroupService.create(request);

            // then
            assertThat(savedTableGroup.getId()).isNotNull();
        }

        @Test
        @DisplayName("존재하지 않는 테이블인 경우 예외가 발생한다.")
        void invalidOrderTable() {
            // given
            CreateTableGroupRequest request = new CreateTableGroupRequest(
                new ArrayList<AddOrderTableToTableGroupRequest>() {{
                    add(new AddOrderTableToTableGroupRequest(0L));
                    add(new AddOrderTableToTableGroupRequest(0L));
                }}
            );

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 테이블입니다.");
        }

    }

    @Nested
    @DisplayName("ungroup()")
    class UngroupMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 그룹이 해제된다.")
        void ungroup() {
            // given
            OrderTable orderTable1 = createAndSaveOrderTable();
            OrderTable orderTable2 = createAndSaveOrderTable();
            long savedGroupId = createAndSaveTableGroup(orderTable1, orderTable2).getId();

            // when
            tableGroupService.ungroup(savedGroupId);

            // then
            assertThat(orderTableDao.findAllByTableGroupId(savedGroupId)).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("COOKING 혹은 MEAL 상태의 테이블인 경우 예외가 발생한다.")
        void wrongTableState(String status) {
            // given
            OrderTable orderTable1 = createAndSaveOrderTable();
            OrderTable orderTable2 = createAndSaveOrderTable();
            long savedGroupId = createAndSaveTableGroup(orderTable1, orderTable2).getId();

            Order order = createOrder(orderTable1);
            order.changeStatus(status);
            orderDao.save(order);

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 시작되어 그룹을 해제할 수 없습니다.");
        }
    }

    private OrderTable createAndSaveOrderTable() {
        OrderTable orderTable = new OrderTable(10, true);

        return orderTableDao.save(orderTable);
    }

    private TableGroup createAndSaveTableGroup(OrderTable orderTable1, OrderTable orderTable2) {
        CreateTableGroupRequest request = new CreateTableGroupRequest(
            new ArrayList<AddOrderTableToTableGroupRequest>() {{
                add(new AddOrderTableToTableGroupRequest(orderTable1.getId()));
                add(new AddOrderTableToTableGroupRequest(orderTable2.getId()));
            }}
        );

        return tableGroupService.create(request);
    }

    private Order createOrder(OrderTable orderTable) {
        Product product = productDao.save(new Product("product", new BigDecimal(5000)));

        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroup"));

        Menu menu = menuDao.save(new Menu(
            "menu",
            new BigDecimal(2000),
            menuGroup,
            new HashMap<Product, Long>() {{
                put(product, 1L);
            }}
        ));

        return new Order(
            orderTable,
            new HashMap<Menu, Long>() {{
                put(menu, 1L);
            }}
        );
    }

}
