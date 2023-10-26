package kitchenpos.application.support.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;

public class MenuTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String name = "메뉴 이름" + UUID.randomUUID().toString().substring(0, 5);
        private BigDecimal price = new BigDecimal("16000");
        private MenuGroup menuGroup = MenuGroupTestSupport.builder().build();
        private List<MenuProduct> menuProducts = List.of(
                MenuProductTestSupport.builder().build(),
                MenuProductTestSupport.builder().build(),
                MenuProductTestSupport.builder().build());

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder price(final BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder menuGroup(final MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public Builder menuProducts(final List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(name, Price.from(price), menuGroup);
        }

        public MenuCreateRequest buildToMenuCreateRequest() {
            final List<MenuProductRequest> menuProductRequests = menuProducts.stream()
                    .map(it -> new MenuProductRequest(it.getProduct().getId() == null ? -1L : it.getProduct().getId(),
                            it.getQuantity()))
                    .collect(Collectors.toList());
            if (menuGroup == null) {
                return new MenuCreateRequest(name, price, -1L, menuProductRequests);
            }
            return new MenuCreateRequest(name, price, menuGroup.getId() == null ? -1 : menuGroup.getId(),
                    menuProductRequests);
        }
    }
}
