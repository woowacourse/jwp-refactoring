package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.후라이드치킨;
import static kitchenpos.fixture.OrderFixture.createOrderLineItem;
import static kitchenpos.fixture.ProductFixture.후라이드;
import static kitchenpos.fixture.TableFixture.비어있는_주문_테이블;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.application.dto.TableGroupDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/test-data.sql")
public abstract class ServiceIntegrationTest {

    @Autowired
    protected MenuService menuService;
    @Autowired
    protected TableGroupService tableGroupService;
    @Autowired
    protected ProductService productService;
    @Autowired
    protected TableService tableService;
    @Autowired
    protected MenuGroupService menuGroupService;
    @Autowired
    protected OrderService orderService;

    protected Menu createMenu() {
        final Product savedProduct = productService.create(후라이드());
        final MenuProduct menuProduct = MenuFixture.createMenuProduct(savedProduct, 1L);
        final MenuGroup savedMenuGroup = menuGroupService.create(MenuFixture.한마리메뉴());
        final Menu menu = 후라이드치킨(savedMenuGroup, List.of(menuProduct));

        return menuService.create(menu);
    }

    protected Order createOrderSuccessfully() {
        final Menu menu = createMenu();
        final OrderLineItem orderLineItem = createOrderLineItem(menu.getId(), 1L);
        final OrderTable savedOrderTable = createOrderTable();

        final Order order = new Order();
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(savedOrderTable.getId());
        return orderService.create(order);
    }

    protected OrderTable createOrderTable() {
        final OrderTable orderTable = 비어있는_주문_테이블();
        orderTable.setEmpty(false);
        return tableService.create(orderTable);
    }

    protected void saveTableGroup(final OrderTable savedOrderTable) {
        final OrderTable orderTable = tableService.create(비어있는_주문_테이블());
        final TableGroupDto tableGroupDto = new TableGroupDto(
            null,
            LocalDateTime.now(),
            List.of(OrderTableDto.from(savedOrderTable), OrderTableDto.from(orderTable))
        );
        tableGroupService.create(tableGroupDto);
    }
}
