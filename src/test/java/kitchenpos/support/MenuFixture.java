package kitchenpos.support;

import static org.assertj.core.util.Lists.emptyList;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;

public abstract class MenuFixture {

    public static MenuRequest menuRequest(int price) {

        return MenuRequest.builder()
                .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                .intPrice(price)
                .menuGroup(new MenuGroup(1L, ""))
                .menuProductRequests(emptyList())
                .build();
    }

    public static MenuRequest menuRequest(BigDecimal price) {

        return MenuRequest.builder()
                .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                .price(price)
                .menuGroup(new MenuGroup(1L, null))
                .menuProductRequests(emptyList())
                .build();
    }

    public static MenuRequest menuRequest(MenuGroup menuGroup,
                                          Product product,
                                          int price) {

        return MenuRequest.builder()
                .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                .intPrice(price)
                .menuGroup(menuGroup)
                .menuProductRequests(List.of(new MenuProductRequest(product.getId(), 1L)))
                .build();
    }

    public static WrapMenuRequest menuRequest(String name,
                                              int price,
                                              Long menuGroupId,
                                              List<MenuProductRequest> menuProducts) {

        final MenuGroup menuGroup = new MenuGroup(menuGroupId, null);
        return new WrapMenuRequest(name, BigDecimal.valueOf(price), menuGroup, menuProducts);
    }

    /**
     * need jackson binding default constructor
     */
    public static class WrapMenuRequest extends MenuRequest {

        WrapMenuRequest(String name,
                        BigDecimal bigDecimal,
                        MenuGroup menuGroup,
                        List<MenuProductRequest> menuProducts) {
            super(name, bigDecimal, menuGroup.getId(), menuProducts);
        }

        public static class WrapMenu extends Menu {

            /**
             * need jackson binding default constructor
             */
            private WrapMenu() {
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

        public static class WrapMenuResponse extends MenuResponse {

            private WrapMenuResponse() {
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
}
