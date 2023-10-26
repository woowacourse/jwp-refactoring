package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.menu.domain.MenuProduct;

@Embeddable
public class MenuProductInfo {

    private long menuProductQuantity;

    @Embedded
    private ProductInfo productInfo;

    protected MenuProductInfo() {
    }

    private MenuProductInfo(final long menuProductQuantity,
                            final ProductInfo productInfo) {
        this.menuProductQuantity = menuProductQuantity;
        this.productInfo = productInfo;
    }

    public static MenuProductInfo from(final MenuProduct menuProduct) {
        return new MenuProductInfo(menuProduct.getQuantity(),
                                   ProductInfo.from(menuProduct.getProduct()));
    }

    public static List<MenuProductInfo> from(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                           .map(MenuProductInfo::from)
                           .collect(Collectors.toList());
    }

    public long getMenuProductQuantity() {
        return menuProductQuantity;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }
}
