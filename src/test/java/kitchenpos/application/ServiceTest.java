package kitchenpos.application;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.DatabaseCleaner;
import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuValidator;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.ProductResponse;
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
