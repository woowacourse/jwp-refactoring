package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);

        final Menu menu = new Menu(menuRequest.getName(), new Price(menuRequest.getPrice()), menuGroup);
        final List<MenuProduct> menuProducts = getMenuProducts(menu, menuRequest.getMenuProducts());
        menu.initMenuProducts(menuProducts);

        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> getMenuProducts(final Menu menu, final List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(productRequest -> {
                    final Product product = productRepository.findById(productRequest.getProductId())
                            .orElseThrow(ProductNotFoundException::new);
                    return new MenuProduct(menu, product, productRequest.getQuantity());
                }).collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
