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

    public static MenuGroup testMenuGroup = new MenuGroup(1L, "테스트 메뉴 그룹");
    public static Product testProduct1 = new Product(1L, "테스트상품1", BigDecimal.valueOf(1000L));
    public static Product testProduct2 = new Product(2L, "테스트상품2", BigDecimal.valueOf(2000L));
    public static MenuProduct testMenuProduct1 = new MenuProduct(1L, 1L, testProduct1.getId(), 1);
    public static MenuProduct testMenuProduct2 = new MenuProduct(2L, 1L, testProduct2.getId(), 1);
    public static Menu testMenu = new Menu(
            1L,
            "테스트메뉴",
            Price.valueOf(BigDecimal.valueOf(3000L)),
            testMenuGroup.getId(),
            List.of(testMenuProduct1, testMenuProduct2)
    );
    public static OrderTable testOrderTable1 = new OrderTable(1L, null, 0, true);
    public static OrderTable testOrderTable2 = new OrderTable(2L, null, 0, true);
}
