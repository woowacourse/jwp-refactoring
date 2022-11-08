package kitchenpos.support;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.request.MenuGroupRequest;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.request.OrderTableRequest;
import kitchenpos.order.dto.response.OrderTableResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void cleanTables() throws SQLException {
        databaseCleaner.clean();
    }

    protected MenuGroup saveMenuGroup(final String name) {
        MenuGroupRequest request = new MenuGroupRequest(name);
        MenuGroupResponse createdMenuGroup = menuGroupService.create(request);
        return new MenuGroup(createdMenuGroup.getId(), createdMenuGroup.getName());
    }

    protected Menu saveMenu(final String name, final int price, final MenuGroup menuGroup,
                            final List<MenuProduct> menuProducts) {
        MenuRequest menu = new MenuRequest(
                name,
                BigDecimal.valueOf(price),
                menuGroup.getId(),
                menuProducts.stream()
                        .map(menuProduct -> new MenuProductRequest(
                                menuProduct.getProductId(),
                                menuProduct.getQuantity())
                        )
                        .collect(Collectors.toList())
        );

        MenuResponse createdMenu = menuService.create(menu);
        return new Menu(createdMenu.getId(), createdMenu.getName(), new Price(createdMenu.getPrice()), menuGroup,
                menuProducts, menuValidator);
    }

    protected OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, empty);
        OrderTableResponse createdOrderTable = tableService.create(request);
        return new OrderTable(createdOrderTable.getId(), null, createdOrderTable.getNumberOfGuests(),
                createdOrderTable.isEmpty());
    }

    protected Product saveProduct(final String name, final int price) {
        ProductRequest request = new ProductRequest(name, new BigDecimal(price));
        ProductResponse createdProduct = productService.create(request);
        return new Product(createdProduct.getId(), createdProduct.getName(), createdProduct.getPrice());
    }
}
