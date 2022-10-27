package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class DomainFixture {

    public static Menu givenDefaultMenu() {
        MenuGroup menuGroup = givenDefaultMenuGroup();
        return new Menu("메뉴1", BigDecimal.valueOf(17_000), menuGroup.getId());
    }

    public static MenuGroup givenDefaultMenuGroup() {
        return new MenuGroup("메뉴그룹1");
    }

    public static OrderTable givenEmptyTable() {
        return new OrderTable(null, 5, true);
    }

    public static Product givenDefaultProduct() {
        return new Product("상품1", BigDecimal.valueOf(17_000));
    }

    public static MenuProduct givenMenuProduct(long productId) {
        return new MenuProduct(productId, 1);
    }
}
