package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenuProduct;
import static kitchenpos.fixture.MenuFixture.한마리메뉴_DTO;
import static kitchenpos.fixture.MenuFixture.후라이드치킨;
import static kitchenpos.fixture.OrderFixture.createOrderLineItem;
import static kitchenpos.fixture.ProductFixture.후라이드_DTO;
import static kitchenpos.fixture.TableFixture.비어있는_주문_테이블_DTO;
import static kitchenpos.fixture.TableFixture.비어있지_않는_주문_테이블_DTO;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.application.dto.TableGroupDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
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
        final ProductDto savedProductDto = productService.create(후라이드_DTO());
        final MenuProduct menuProduct = createMenuProduct(savedProductDto, 1L);
        final MenuGroupDto savedMenuGroupDto = menuGroupService.create(한마리메뉴_DTO());
        final Menu menu = 후라이드치킨(savedMenuGroupDto, List.of(menuProduct));

        return menuService.create(menu);
    }

    protected Order createOrderSuccessfully() {
        final Menu menu = createMenu();
        final OrderLineItem orderLineItem = createOrderLineItem(menu.getId(), 1L);
        final OrderTableDto savedOrderTableDto = createNotEmptyOrderTable();

        final Order order = new Order();
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(savedOrderTableDto.getId());
        return orderService.create(order);
    }

    protected OrderTableDto createNotEmptyOrderTable() {
        final OrderTableDto orderTable = 비어있지_않는_주문_테이블_DTO();
        return tableService.create(orderTable);
    }

    protected void saveTableGroup(final OrderTableDto savedOrderTableDto) {
        final OrderTableDto orderTable = tableService.create(비어있는_주문_테이블_DTO());
        final TableGroupDto tableGroupDto = new TableGroupDto(
            null,
            LocalDateTime.now(),
            List.of(savedOrderTableDto, orderTable)
        );
        tableGroupService.create(tableGroupDto);
    }
}
