package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.vo.Price;

public class DomainTestFixture {

    public static MenuGroup getTestMenuGroup() {
        return new MenuGroup(1L, "테스트 메뉴 그룹");
    }

    public static Product getTestProduct1() {
        return new Product(1L, "테스트 상품1", BigDecimal.valueOf(1000L));
    }

    public static Product getTestProduct2() {
        return new Product(2L, "테스트 상품2", BigDecimal.valueOf(1500L));
    }

    public static OrderTable getTestOrderTable1() {
        return new OrderTable(1L, null, 0, true);
    }

    public static OrderTable getTestOrderTable2() {
        return new OrderTable(2L, null, 0, true);
    }

    public static Menu getTestMenu() {
        return new Menu(
                1L,
                "테스트 메뉴",
                Price.valueOf(BigDecimal.valueOf(2500L)),
                getTestMenuGroup().getId(),
                List.of(
                        new MenuProduct(getTestProduct1().getId(), 1),
                        new MenuProduct(getTestProduct2().getId(), 1)
                )
        );
    }
}
