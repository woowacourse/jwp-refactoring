package kitchenpos.application.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.menuGroupException.MenuGroupNotFoundException;
import kitchenpos.exception.productException.ProductNotFoundException;
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
            final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);

        final Menu menu = new Menu(menuRequest.getName(), new Price(menuRequest.getPrice()), menuGroup.getId(),
                getMenuProducts(menuRequest));

        return MenuResponse.from(menuRepository.save(menu));
    }

    private MenuProducts getMenuProducts(final MenuRequest menuRequest) {
        return new MenuProducts(menuRequest.getMenuProducts().stream()
                .map(productRequest -> {
                    final Product product = productRepository.findById(productRequest.getProductId())
                            .orElseThrow(ProductNotFoundException::new);
                    return new MenuProduct(product.getId(),product.getName(),new Price(product.getPrice()), productRequest.getQuantity());
                }).collect(Collectors.toList()), menuRequest.getPrice());
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
