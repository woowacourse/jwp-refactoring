package kitchenpos.menu.application;

import static kitchenpos.exception.ExceptionType.NOT_FOUND_MENU_GROUP_EXCEPTION;
import static kitchenpos.exception.ExceptionType.NOT_FOUND_PRODUCT_EXCEPTION;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.CustomIllegalArgumentException;
import kitchenpos.menu.domain.JpaMenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menuGroup.domain.JpaMenuGroupRepository;
import kitchenpos.product.domain.JpaProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final JpaMenuRepository menuRepository;
    private final JpaMenuGroupRepository menuGroupRepository;
    private final JpaProductRepository productRepository;

    public MenuService(final JpaMenuRepository menuRepository, final JpaMenuGroupRepository menuGroupRepository,
                       final JpaProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public Menu create(final MenuRequest request) {
        validMenuGroup(request.getMenuGroupId());
        return menuRepository.save(toMenu(request));
    }

    private Menu toMenu(final MenuRequest request) {
        return new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(),
                toMenuProducts(request.getMenuProducts()));
    }

    private List<MenuProduct> toMenuProducts(final List<MenuProductRequest> requests) {
        return requests.stream().map(request -> {
            final Product product = getProduct(request);
            return new MenuProduct(request.getProductId(), request.getQuantity(), product.getPrice());
        }).collect(Collectors.toList());
    }

    private Product getProduct(final MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_PRODUCT_EXCEPTION));
    }

    private void validMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new CustomIllegalArgumentException(NOT_FOUND_MENU_GROUP_EXCEPTION);
        }
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
