package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        final Menu menu = menuCreateRequest.toMenu();
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new NotFoundMenuGroupException();
        }
        validateMenuPrice(menu);
        updateMenuProducts(menu.getMenuProducts(), menu);
        final Menu saved = menuRepository.save(menu);
        return MenuResponse.from(saved);
    }

    private void validateMenuPrice(final Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(NotFoundProductException::new);
            sum = sum.add(product.calculateTotalPrice(menuProduct.getQuantity()));
        }
        menu.validatePrice(sum);
    }

    private void updateMenuProducts(final List<MenuProduct> menuProducts, final Menu menu) {
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenu(menu);
        }
        menu.updateMenuProducts(menuProducts);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
