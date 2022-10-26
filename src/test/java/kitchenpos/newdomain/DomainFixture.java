package kitchenpos.newdomain;

import kitchenpos.newdomain.vo.Name;
import kitchenpos.newdomain.vo.Price;
import kitchenpos.newdomain.vo.Quantity;

public class DomainFixture {

    public static final Quantity 한개 = new Quantity(1);

    public static final Product 뿌링클 = new Product(new Name("뿌링클"), Price.valueOf(18_000));
    public static final Product 치즈볼 = new Product(new Name("치즈볼"), Price.valueOf(5_000));
}
