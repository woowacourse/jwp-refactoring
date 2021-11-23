package kitchenpos;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    private static Menu 후라이드_단품 = new Menu(
            1L,
            "후라이드 단품",
            BigDecimal.valueOf(17000),
            1L,
            Collections.singletonList(new MenuProduct(1L, 1))
    );
    private static Menu 양념반_후라이드반 = new Menu(
            3L,
            "양념 반 + 후라이드 반",
            BigDecimal.valueOf(30000),
            2L,
            Arrays.asList(new MenuProduct(1L, 1), new MenuProduct(2L, 1))
    );

    public static OrderLineItem 후라이드_단품_둘 = new OrderLineItem(후라이드_단품.getId(), 2);
    public static OrderLineItem 양념반_후라이드반_하나 = new OrderLineItem(양념반_후라이드반.getId(), 1);
}
