package kitchenpos.application;

import kitchenpos.DatabaseCleaner;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixtures.domain.MenuFixture.createMenu;
import static kitchenpos.fixtures.domain.MenuGroupFixture.createMenuGroupRequest;
import static kitchenpos.fixtures.domain.OrderFixture.createOrder;
import static kitchenpos.fixtures.domain.OrderTableFixture.createOrderTable;
import static kitchenpos.fixtures.domain.ProductFixture.createProduct;
import static kitchenpos.fixtures.domain.TableGroupFixture.createTableGroup;

@Transactional
@SpringBootTest
public class ServiceTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @BeforeEach
    void cleanTables() throws SQLException {
        databaseCleaner.clear();
    }

    protected MenuGroup saveMenuGroup(final String name) {
        final MenuGroup request = createMenuGroupRequest(name);
        return menuGroupService.create(request);
    }

    protected Menu saveMenu(final String name,
                            final int price,
                            final MenuGroup menuGroup,
                            final List<MenuProduct> menuProducts) {
        final Menu menu = createMenu(name, new BigDecimal(price), menuGroup.getId(), menuProducts);
        return menuService.create(menu);
    }

    protected OrderTable saveOrderTable(final int numberOfGuests,
                                        final boolean empty) {
        final OrderTable request = createOrderTable(numberOfGuests, empty);
        return tableService.create(request);
    }

    protected TableGroup saveTableGroup(final OrderTable... orderTables) {
        final TableGroup request = createTableGroup(LocalDateTime.now(), List.of(orderTables));
        return tableGroupService.create(request);
    }

    protected Order saveOrder(final OrderStatus orderStatus,
                              final OrderTable orderTable,
                              final List<OrderLineItem> orderLineItems) {
        final Order request = createOrder(orderTable.getId(), orderStatus, LocalDateTime.now(), orderLineItems);
        return orderService.create(request);
    }

    protected Product saveProduct(final String name,
                                  final int price) {
        final Product request = createProduct(name, new BigDecimal(price));
        return productService.create(request);
    }
}
