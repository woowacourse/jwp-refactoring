package kitchenpos.support;

import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.Empty;
import kitchenpos.domain.table.GuestNumber;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableStatus;

public class DomainFixture {

    public static final Product 뿌링클 = new Product("뿌링클", 18_000);
    public static final Product 치즈볼 = new Product("치즈볼", 5_000);

    public static final MenuGroup 세트_메뉴 = new MenuGroup("세트 메뉴");
    public static final MenuGroup 인기_메뉴 = new MenuGroup("인기 메뉴");

    public static Menu 뿌링클_치즈볼_메뉴_생성(final long menuGroupId, final Product... products) {
        return new Menu(
                "뿌링클+치즈볼", 23_000, menuGroupId,
                Arrays.stream(products)
                        .map(product -> new MenuProduct(product.getId(), product.getPrice(), 1))
                        .collect(Collectors.toList())
        );
    }

    public static OrderTable 빈_테이블_생성() {
        return new OrderTable(new TableStatus(new Empty(true), new GuestNumber(0)));
    }

    public static OrderTable 채워진_테이블_생성() {
        return new OrderTable(new TableStatus(new Empty(false), new GuestNumber(0)));
    }
}
