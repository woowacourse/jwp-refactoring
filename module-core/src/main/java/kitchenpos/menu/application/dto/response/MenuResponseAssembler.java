package kitchenpos.menu.application.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

@Component
public class MenuResponseAssembler {

    public List<MenuResponse> asMenuResponses(final List<Menu> menus) {
        return menus.stream()
                .map(this::asMenuResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public MenuResponse asMenuResponse(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice().getValue(),
                menu.getMenuGroupId(),
                asMenuProductResponses(menu.getMenuProducts())
        );
    }

    private List<MenuProductResponse> asMenuProductResponses(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(this::asMenuProductResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    private MenuProductResponse asMenuProductResponse(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getProductId(),
                menuProduct.getQuantity().getValue()
        );
    }

    public List<MenuGroupResponse> asMenuGroupResponses(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(this::asMenuGroupResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public MenuGroupResponse asMenuGroupResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(
                menuGroup.getId(),
                menuGroup.getName()
        );
    }

    public List<ProductResponse> asProductResponses(final List<Product> products) {
        return products.stream()
                .map(this::asProductResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public ProductResponse asProductResponse(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice().getValue()
        );
    }
}
