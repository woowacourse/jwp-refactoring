package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

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

    public static OrderTable createOrderTable(boolean empty) {
        return new OrderTable(0, empty);
    }

    public static Menu createMenu(Long menuGroupId) {
        return new Menu("뿌링 치킨", BigDecimal.valueOf(15_000), menuGroupId);
    }
}
