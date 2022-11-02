package kitchenpos.fixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
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

    public static OrderTable createOrderTable(Long tableGroupId, boolean empty) {
        return new OrderTable(tableGroupId, 0, empty);
    }

    public static Menu createMenu(MenuGroup menuGroup) {
        return new Menu("뿌링 치킨", BigDecimal.valueOf(15_000), menuGroup);
    }

    public static TableGroup createTableGroup() {
        return new TableGroup(LocalDateTime.now());
    }
}
