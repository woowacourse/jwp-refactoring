package kitchenpos.support;

import static org.assertj.core.util.Lists.emptyList;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuRequest;

public abstract class MenuFixture {

    public static MenuRequest menuRequest(MenuGroup menuGroup,
                                          Product product,
                                          int price) {

        return MenuRequest.builder()
                .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                .intPrice(price)
                .menuGroup(menuGroup)
                .menuProducts(List.of(new MenuProduct(product, 1L)))
                .build();
    }

    public static MenuRequest menuRequest(MenuGroup menuGroup,
                                          List<MenuProduct> menuProducts,
                                          int price) {

        return MenuRequest.builder()
                .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                .menuGroup(menuGroup)
                .intPrice(price)
                .menuProducts(menuProducts)
                .build();
    }

    public static MenuRequest menuRequest(int price) {

        return MenuRequest.builder()
                .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                .intPrice(price)
                .menuGroup(new MenuGroup(1L, ""))
                .menuProducts(emptyList())
                .build();
    }

    public static MenuRequest menuRequest(BigDecimal price) {

        return MenuRequest.builder()
                .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                .price(price)
                .menuGroup(new MenuGroup(1L, null))
                .menuProducts(emptyList())
                .build();
    }

    public static MenuRequest menuRequest(int price,
                                          long menuGroupId) {

        return MenuRequest.builder()
                .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                .intPrice(price)
                .menuGroup(new MenuGroup(menuGroupId, ""))
                .menuProducts(emptyList())
                .build();
    }

    public static MenuRequest menuRequest(Product product,
                                          int price) {

        return MenuRequest.builder()
                .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                .intPrice(price)
                .menuGroup(new MenuGroup(1L, ""))
                .menuProducts(List.of(new MenuProduct(product, 1L)))
                .build();
    }

    public static MenuRequest menuRequest(int price, List<MenuProduct> menuProducts) {

        return MenuRequest.builder()
                .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                .intPrice(price)
                .menuGroup(new MenuGroup(1L, null))
                .menuProducts(menuProducts)
                .build();
    }

    public static WrapMenu menu(String name,
                                int price,
                                Long menuGroupId,
                                List<MenuProduct> menuProducts) {

        final MenuGroup menuGroup = new MenuGroup(menuGroupId, null);
        return new WrapMenu(name, BigDecimal.valueOf(price), menuGroup, menuProducts);
    }

    public static class WrapMenu extends Menu {

        /**
         * need jackson binding default constructor
         */
        public WrapMenu() {
        }

        public WrapMenu(String name,
                        BigDecimal price,
                        MenuGroup menuGroup,
                        List<MenuProduct> menuProducts) {

            super(name, price, menuGroup, menuProducts);
        }

        public Long id() {
            return super.getId();
        }

        public String name() {
            return super.getName();
        }

        public BigDecimal price() {
            return super.getPrice();
        }

        public int intPrice() {
            return super.getPrice().intValue();
        }

        public double doublePrice() {
            return super.getPrice().doubleValue();
        }
    }
}
