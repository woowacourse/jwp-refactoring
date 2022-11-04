package kitchenpos.menu.application.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.menu.application.dto.request.menu.MenuProductRequest;
import kitchenpos.menu.application.dto.request.menu.MenuRequest;
import kitchenpos.menu.application.dto.request.menugroup.MenuGroupRequest;
import kitchenpos.menu.application.dto.request.product.ProductRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

@Component
public class MenuRequestAssembler {

    public Menu asMenu(final MenuRequest request) {
        return new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                asMenuProducts(request.getMenuProducts())
        );
    }

    public List<MenuProduct> asMenuProducts(final List<MenuProductRequest> requests) {
        return requests.stream()
                .map(this::asMenuProduct)
                .collect(Collectors.toUnmodifiableList());
    }

    private MenuProduct asMenuProduct(final MenuProductRequest request) {
        return new MenuProduct(
                request.getProductId(),
                request.getQuantity().getValue()
        );
    }

    public MenuGroup asMenuGroup(final MenuGroupRequest request) {
        return new MenuGroup(request.getName());
    }

    public Product asProduct(final ProductRequest request) {
        return new Product(
                request.getName(),
                request.getPrice()
        );
    }
}
