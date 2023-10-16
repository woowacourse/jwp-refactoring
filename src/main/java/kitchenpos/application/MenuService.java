package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menu.MenusResponse;
import kitchenpos.exception.MenuGroupException.NotFoundMenuGroupException;
import kitchenpos.exception.ProductException.NotFoundProductException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
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
    public MenuResponse create(final MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(NotFoundMenuGroupException::new);
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        associateMenuProduct(request.getMenuProducts(), savedMenu);
        menu.verifyPrice();
        return MenuResponse.from(savedMenu);
    }

    private void associateMenuProduct(final List<MenuProductRequest> menuProductRequests, final Menu savedMenu) {
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(NotFoundProductException::new);
            final MenuProduct menuProduct = new MenuProduct(product, menuProductRequest.getQuantity());
            menuProduct.confirmMenu(savedMenu);
        }
    }

    public MenusResponse list() {
        return MenusResponse.from(menuRepository.findAll());
    }
}
