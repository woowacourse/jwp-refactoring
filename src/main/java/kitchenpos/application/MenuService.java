package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.exception.MenuGroupNotFoundException;
import kitchenpos.application.exception.ProductNotFoundException;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.domain.product.repository.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.request.CreateMenuProductRequest;
import kitchenpos.ui.dto.request.CreateMenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final CreateMenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                                                       .orElseThrow(MenuGroupNotFoundException::new);
        final List<MenuProduct> menuProducts = findMenuProducts(request);
        final Menu menu = Menu.of(request.getName(), request.getPrice(), menuProducts, menuGroup);

        return menuRepository.save(menu);
    }

    private List<MenuProduct> findMenuProducts(final CreateMenuRequest menuRequest) {
        final List<MenuProduct> menuProducts = new ArrayList<>();

        for (final CreateMenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                                              .orElseThrow(ProductNotFoundException::new);

            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }

        return menuProducts;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
