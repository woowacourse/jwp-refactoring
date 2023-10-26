package kitchenpos.application;

import kitchenpos.domain.dto.MenuRequest;
import kitchenpos.domain.dto.MenuRequest.MenuProductRequest;
import kitchenpos.domain.dto.MenuResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuProductRepository menuProductRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        final MenuProducts menuProducts = convertMenuProducts(request);

        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);

        menuRepository.save(menu);

        menuProducts.updateMenu(menu);
        menuProductRepository.saveAll(menuProducts.getValues());

        return MenuResponse.from(menu);
    }

    private MenuProducts convertMenuProducts(final MenuRequest request) {
        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();

        final List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(menuProduct -> new MenuProduct(null,
                        productRepository.findById(menuProduct.getProductId()).orElseThrow(IllegalArgumentException::new),
                        menuProduct.getQuantity()))
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
