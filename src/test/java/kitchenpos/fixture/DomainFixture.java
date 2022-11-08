package kitchenpos.fixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class DomainFixture {

    public static MenuGroup createMenuGroup() {
        return new MenuGroup("순살 메뉴");
    }

    public static Product createProduct() {
        return new Product("뿌링 치킨", BigDecimal.valueOf(15_000));
    }

    public static OrderTable createOrderTable() {
        return new OrderTable(0, false);
    }

    public static OrderTable createOrderTable(TableGroup tableGroup, boolean empty) {
        return new OrderTable(tableGroup, 0, empty);
    }

    public static Menu createMenu(MenuGroup menuGroup) {
        return new Menu("뿌링 치킨",
                new Price(BigDecimal.valueOf(15_000)),
                menuGroup,
                createMenuProducts()
        );
    }

    public static MenuProducts createMenuProducts() {
        return new MenuProducts(List.of(new MenuProduct(createProduct(), 1L)));
    }

    public static TableGroup createTableGroup() {
        return new TableGroup(LocalDateTime.now());
    }
}
