package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Test
    void 주문_테이블을_그룹화할_수_있다() {
        List<OrderTable> orderTables = new ArrayList<>();
        OrderTable orderTable1 = tableService.create(new OrderTable(null, 3, true));
        OrderTable orderTable2 = tableService.create(new OrderTable(null, 4, true));
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables));
        orderTables.get(0).setTableGroupId(tableGroup.getId());
        orderTables.get(1).setTableGroupId(tableGroup.getId());

        assertAll(
                () -> assertThat(tableGroup.getOrderTables().size()).isEqualTo(2),
                () -> assertThat(tableGroup.getId()).isNotNull()
        );
    }

    @Test
    void 주문_테이블이_1개_이하이면_예외가_발생한다() {
        List<OrderTable> orderTables = new ArrayList<>();
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, true));
        orderTables.add(orderTable);

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_테이블이_존재하지_않으면_예외가_발생한다() {
        List<OrderTable> orderTables = new ArrayList<>();

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문과_존재하는_테이블의_수가_같지_않으면_예외가_발생한다() {
        List<OrderTable> orderTables = new ArrayList<>();
        OrderTable orderTable1 = tableService.create(new OrderTable(null, 3, true));
        orderTables.add(orderTable1);
        tableService.create(new OrderTable(null, 4, true));

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_테이블이_빈_테이블이_아니면_예외가_발생한다() {
        List<OrderTable> orderTables = new ArrayList<>();
        OrderTable orderTable1 = tableService.create(new OrderTable(null, 3, false));
        OrderTable orderTable2 = tableService.create(new OrderTable(null, 4, true));
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정을_해제할_수_있다() {
        List<OrderTable> orderTables = new ArrayList<>();
        OrderTable orderTable1 = tableService.create(new OrderTable(null, 3, true));
        OrderTable orderTable2 = tableService.create(new OrderTable(null, 4, true));
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables));
        orderTables.get(0).setTableGroupId(tableGroup.getId());
        orderTables.get(1).setTableGroupId(tableGroup.getId());

        tableGroupService.ungroup(tableGroup.getId());

        final List<OrderTable> tables = tableService.list();

        assertAll(
                () -> assertThat(tables.get(0).getTableGroupId()).isNull(),
                () -> assertThat(tables.get(1).getTableGroupId()).isNull()
        );
    }

    @Test
    void 단체를_해제하려는_테이블의_상태가_식사_중이거나_요리_중이면_예외가_발생한다() {
        List<OrderTable> orderTables = new ArrayList<>();
        OrderTable orderTable1 = tableService.create(new OrderTable(null, 3, true));
        orderTables.add(orderTable1);
        OrderTable orderTable2 = tableService.create(new OrderTable(null, 4, true));
        orderTables.add(orderTable2);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        Menu ramen = 라면_메뉴를_생성한다("라면");
        Menu chapagetti = 라면_메뉴를_생성한다("짜파게티");
        orderLineItems.add(new OrderLineItem(order.getId(), ramen.getId(), 1));
        orderLineItems.add(new OrderLineItem(order.getId(), chapagetti.getId(), 1));

        TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables));
        orderTables.get(0).setTableGroupId(tableGroup.getId());
        orderTables.get(1).setTableGroupId(tableGroup.getId());

        orderService.create(order);

        assertThatThrownBy(
                () -> tableGroupService.ungroup(tableGroup.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private Menu 라면_메뉴를_생성한다(String name) {
        Product product = productService.create(new Product("맛있는 라면", new BigDecimal(1300)));
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(null, product.getId(), 1));

        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("면"));
        Menu ramen = menuService.create(new Menu(name, new BigDecimal(1200), menuGroup.getId(), menuProducts));
        menuProducts.get(0).setMenuId(ramen.getId());

        return ramen;
    }
}
