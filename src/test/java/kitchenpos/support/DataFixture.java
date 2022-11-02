package kitchenpos.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class DataFixture {
    public static MenuGroup createMenuGroup(final String name) {
        return new MenuGroup(name);
    }

    public static Product createProduct(final String name, final Long price) {
        return new Product(name, new BigDecimal(price));
    }

    public static Menu createMenu(final String name, final Long price, final MenuGroup menuGroup,
                                  final List<Product> products) {
        final List<MenuProduct> menuProducts = products.stream()
                .map(product -> new MenuProduct(product, 100))
                .collect(Collectors.toList());
        return new Menu(name, new BigDecimal(price), menuGroup, menuProducts);
    }

    public static OrderTable createOrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests,
                                              final boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(final TableGroup tableGroup, final int numberOfGuests,
                                              final boolean empty) {
        return createOrderTable(null, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(final int numberOfGuests, final boolean empty) {
        return createOrderTable(null, null, numberOfGuests, empty);
    }

    public static TableGroup createTableGroup(final LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }

}
