package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.ui.dto.menu.MenuRequest;
import kitchenpos.ui.dto.menu.MenuResponse;
import kitchenpos.ui.dto.menuproduct.MenuProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new NoSuchElementException("해당 메뉴 그룹이 존재하지 않습니다. id: " + request.getMenuGroupId()));
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);
        final List<MenuProduct> menuProducts = getMenuProducts(request);
        menu.changeMenuProducts(menuProducts);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    private List<MenuProduct> getMenuProducts(MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct getMenuProduct(MenuProductRequest request) {
        final Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 존재하지 않습니다. id: " + request.getProductId()));
        return new MenuProduct(product, request.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.toList(menus);
    }
}
