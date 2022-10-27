package kitchenpos;

import java.math.BigDecimal;
import java.util.ArrayList;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class DomainFixtures {

    private DomainFixtures() {
    }

    public static Product 맛있는_라면() {
        return new Product("맛있는 라면", new BigDecimal(1300));
    }

    public static MenuRequest 라면_메뉴() {
        return new MenuRequest("라면", new BigDecimal(1200), 1L, new ArrayList<>());
    }

    public static MenuGroup 면_메뉴_그룹() {
        return new MenuGroup("면");
    }

    public static OrderTable 빈_주문_테이블_3인() {
        return new OrderTable(null, 3, true);
    }

    public static OrderTable 빈_주문_테이블_4인() {
        return new OrderTable(null, 4, true);
    }

    public static OrderTable 주문_테이블_3인() {
        return new OrderTable(null, 3, false);
    }

    public static OrderTable 주문_테이블_4인() {
        return new OrderTable(null, 4, false);
    }
}
