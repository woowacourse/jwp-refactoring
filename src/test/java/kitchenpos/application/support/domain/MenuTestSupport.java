package kitchenpos.application.support.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private static Long autoCount = 0L;

        private Long id = ++autoCount;
        private String name = "메뉴 이름" + autoCount;
        private BigDecimal price = new BigDecimal("16000");
        private Long menuGroupId = MenuGroupTestSupport.builder().build().getId();
        private List<MenuProduct> menuProducts = List.of(
                MenuProductTestSupport.builder().menuId(id).build(),
                MenuProductTestSupport.builder().menuId(id).build(),
                MenuProductTestSupport.builder().menuId(id).build());

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder price(final BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder menuGroupId(final Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public Builder menuProducts(final List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            final var result = new Menu();
            result.setId(id);
            result.setName(name);
            result.setPrice(price);
            result.setMenuGroupId(menuGroupId);
            result.setMenuProducts(menuProducts);
            return result;
        }

        public MenuCreateRequest buildToMenuCreateRequest() {
            final List<MenuProductDto> menuProductDtos = menuProducts.stream()
                    .map(it -> new MenuProductDto(it.getProductId(), it.getQuantity()))
                    .collect(Collectors.toList());
            if (menuGroupId == null) {
                return new MenuCreateRequest(name, price, -1L, menuProductDtos);
            }
            return new MenuCreateRequest(name, price, menuGroupId, menuProductDtos);
        }
    }
}
