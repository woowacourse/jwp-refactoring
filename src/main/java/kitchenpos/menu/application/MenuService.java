package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository,
                       final MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
        final Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());
        final MenuProducts menuProducts = getMenuProducts(request.getMenuProducts(), menu);
        menu.validateOverPrice(menuProducts.getTotalPrice());
        menuRepository.save(menu);
        return MenuResponse.from(menu, menuProducts);
    }

    private MenuProducts getMenuProducts(final List<MenuProductRequest> menuProductRequests, final Menu menu) {
        final List<MenuProduct> menuProducts = menuProductRequests.stream()
            .map(menuProductDto -> {
                final Product product = productRepository.findById(menuProductDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
                return new MenuProduct(product, menu, menuProductDto.getQuantity());
            })
            .collect(Collectors.toUnmodifiableList());
        return new MenuProducts(menuProducts);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
            .map(menu -> {
                final List<MenuProduct> menuProducts = menuProductRepository.findByMenu(menu);
                return MenuResponse.from(menu, new MenuProducts(menuProducts));
            })
            .collect(Collectors.toUnmodifiableList());
    }
}
