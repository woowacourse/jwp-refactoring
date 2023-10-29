package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        final BigDecimal price = request.getPrice();

        final MenuGroup menuGroup = findMenuGroupBy(request.getMenuGroupId());

        final List<MenuProduct> menuProducts = getMenuProducts(request.getMenuProducts());
        final Menu menu = Menu.of(menuGroup.getId(), menuProducts, request.getName(), price, menuValidator);
        return menuRepository.save(menu);
    }

    private MenuGroup findMenuGroupBy(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 메뉴 그룹이 존재하지 않습니다."));
    }

    private List<MenuProduct> getMenuProducts(final List<MenuProductRequest> menuProductRequests) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = findProductBy(menuProductRequest.getProductId());
            final MenuProduct menuProduct = new MenuProduct(null, product.getId(), menuProductRequest.getQuantity());

            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    private Product findProductBy(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 상품이 존재하지 않습니다."));
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
