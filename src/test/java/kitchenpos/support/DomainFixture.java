package kitchenpos.support;

import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.Empty;
import kitchenpos.domain.table.GuestNumber;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableStatus;

public class DomainFixture {

    public static final Quantity 한개 = new Quantity(1);
    public static final Quantity 두개 = new Quantity(2);
    public static final Name 뿌링클_치즈볼 = new Name("뿌링클+치즈볼");

    public static final Product 뿌링클 = new Product(new Name("뿌링클"), Price.valueOf(18_000));
    public static final Product 치즈볼 = new Product(new Name("치즈볼"), Price.valueOf(5_000));

    public static final MenuGroup 세트_메뉴 = new MenuGroup(new Name("세트 메뉴"));
    public static final MenuGroup 인기_메뉴 = new MenuGroup(new Name("인기 메뉴"));

    public static Menu 뿌링클_치즈볼_메뉴_생성(final long menuGroupId, final Product... products) {
        return new Menu(
                뿌링클_치즈볼, Price.valueOf(23_000), menuGroupId,
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
