package kitchenpos.application;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class DomainFixture {

    public static Menu getMenu(final Long menuGroupId) {
        return new Menu("마이쮸 포도맛", BigDecimal.valueOf(800), menuGroupId);
    }

    public static Product getProduct() {
        return new Product("마이쮸", BigDecimal.valueOf(800));
    }

    public static OrderTable getEmptyTable() {
        return new OrderTable(0, true);
    }

    public static OrderTable getNotEmptyTable(final int numberOfGuests) {
        return new OrderTable(numberOfGuests, false);
    }

    public static MenuGroup getMenuGroup() {
        return new MenuGroup("마이쮸 1종 세트");
    }
}
