package kitchenpos.application;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.DatabaseCleaner;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
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
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TableService tableService;

    @BeforeEach
    void cleanTables() throws SQLException {
        databaseCleaner.clean();
    }

    protected MenuGroupResponse saveMenuGroup(final String name) {
        MenuGroupRequest request = new MenuGroupRequest(name);
        return menuGroupService.create(request);
    }

    protected MenuResponse saveMenu(final String name, final int price, final MenuGroup menuGroup,
                                    final List<MenuProduct> menuProducts) {
        MenuRequest menu = new MenuRequest(
                name,
                BigDecimal.valueOf(price),
                menuGroup.getId(),
                menuProducts.stream()
                        .map(menuProduct -> new MenuProductRequest(
                                menuProduct.getProduct().getId(),
                                menuProduct.getQuantity())
                        )
                        .collect(Collectors.toList())
        );

        return menuService.create(menu);
    }

    protected OrderTableResponse saveOrderTable(final int numberOfGuests, final boolean empty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, empty);
        return tableService.create(request);
    }

    protected ProductResponse saveProduct(final String name, final int price) {
        ProductRequest request = new ProductRequest(name, new BigDecimal(price));
        return productService.create(request);
    }
}
