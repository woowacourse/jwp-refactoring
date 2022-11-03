package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;

public class DomainTestFixture {

    public static MenuGroup testMenuGroup = new MenuGroup("테스트 메뉴 그룹");
    public static Product testProduct1 = Product.create("테스트 상품1", BigDecimal.valueOf(1000L));
    public static Product testProduct2 = Product.create("테스트 상품2", BigDecimal.valueOf(1500L));
    public static OrderTable testOrderTable1 = new OrderTable(1L, null, 0, true);
    public static OrderTable testOrderTable2 = new OrderTable(2L, null, 0, true);
}
