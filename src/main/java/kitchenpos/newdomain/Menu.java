package kitchenpos.newdomain;

import java.util.List;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;
import kitchenpos.newdomain.vo.Name;
import kitchenpos.newdomain.vo.Price;

public class Menu {

    private Long id;
    private Name name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProduct> products;

    public Menu(Name name, Price price, Long menuGroupId, List<MenuProduct> products) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.products = products;
    }

    public Menu(Long id, Name name, Price price, Long menuGroupId,
                List<MenuProduct> products) {
        validateNotOverMenuProducts(price, products);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.products = products;
    }

    private void validateNotOverMenuProducts(final Price price, final List<MenuProduct> products) {
        final Price sum = products.stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce((Price::sum))
                .get();
        if (price.isGreaterThan(sum)) {
            throw new DomainLogicException(CustomErrorCode.MENU_PRICE_ERROR);
        }
    }
}
