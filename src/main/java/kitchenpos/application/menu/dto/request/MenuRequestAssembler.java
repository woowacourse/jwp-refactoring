package kitchenpos.application.menu.dto.request;

import kitchenpos.application.menu.dto.request.menu.MenuProductRequest;
import kitchenpos.application.menu.dto.request.menu.MenuRequest;
import kitchenpos.application.menu.dto.request.menugroup.MenuGroupRequest;
import kitchenpos.application.menu.dto.request.product.ProductRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
