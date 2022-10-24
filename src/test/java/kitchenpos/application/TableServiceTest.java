package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

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
    void 주문_테이블을_생성할_수_있다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, true));

        assertAll(
                () -> assertThat(orderTable.getId()).isNotNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(3),
                () -> assertThat(orderTable.isEmpty()).isTrue()
        );
    }

    @Test
    void 주문_테이블을_조회할_수_있다() {
        tableService.create(new OrderTable(null, 3, true));
        tableService.create(new OrderTable(null, 4, true));

        List<OrderTable> orderTables = tableService.list();

        assertAll(
                () -> assertThat(orderTables.size()).isEqualTo(2),
                () -> assertThat(orderTables.get(0).getId()).isNotNull(),
                () -> assertThat(orderTables.get(1).getId()).isNotNull()
        );
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경할_수_있다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, true));

        OrderTable targetTable = tableService.changeEmpty(orderTable.getId(),
                new OrderTable(null, 3, false));

        assertThat(targetTable.isEmpty()).isFalse();
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경하려_할_때_주문_테이블이_존재하지_않으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> tableService.changeEmpty(1L,
                        new OrderTable(null, 3, false))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경하려_할_때_주문_테이블이_단체_지정이_되어_있으면_예외가_발생한다() {
        List<OrderTable> orderTables = new ArrayList<>();
        OrderTable orderTable1 = tableService.create(new OrderTable(null, 3, true));
        OrderTable orderTable2 = tableService.create(new OrderTable(null, 4, true));
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables));
        orderTables.get(0).setTableGroupId(tableGroup.getId());
        orderTables.get(1).setTableGroupId(tableGroup.getId());

        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable1.getId(),
                        new OrderTable(tableGroup.getId(), 3, true))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경하려_할_때_주문_테이블이_식사중이거나_요리중이면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, false));

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        Menu ramen = 라면_메뉴를_생성한다("라면");
        Menu chapagetti = 라면_메뉴를_생성한다("짜파게티");
        orderLineItems.add(new OrderLineItem(order.getId(), ramen.getId(), 1));
        orderLineItems.add(new OrderLineItem(order.getId(), chapagetti.getId(), 1));

        orderService.create(order);

        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(),
                        new OrderTable(null, 3, false))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_방문한_손님의_명_수를_수정할_수_있다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, false));

        OrderTable targetOrder = tableService.changeNumberOfGuests(orderTable.getId(),
                new OrderTable(null, 4, false));

        assertAll(
                () -> assertThat(targetOrder.getId()).isNotNull(),
                () -> assertThat(targetOrder.getNumberOfGuests()).isEqualTo(4)
        );
    }

    @Test
    void 주문_테이블의_방문한_손님의_명_수를_0명보다_적게_수정하면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, false));

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(),
                        new OrderTable(null, -1, false))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_명_수를_수정할_때_존재하지_않는_테이블이면_예외가_발생한다() {
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(1L,
                        new OrderTable(null, 1, false))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_명_수를_수정할_때_주문_테이블이_비어있으면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, true));

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(),
                        new OrderTable(null, 1, false))
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
