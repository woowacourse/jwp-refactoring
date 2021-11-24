package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.Repository.MenuGroupRepository;
import kitchenpos.Repository.MenuProductRepository;
import kitchenpos.Repository.MenuRepository;
import kitchenpos.Repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.request.MenuRequest;
import kitchenpos.ui.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository, final MenuProductRepository menuProductRepository,
        final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProducts()
            .stream()
            .map(value -> new MenuProduct(productRepository.findById(value.getProductId()).orElseThrow(IllegalArgumentException::new), value.getQuantity()))
            .collect(Collectors.toList());
        Menu menu = new Menu(request.getName(),
            request.getPrice(),
            menuGroupRepository.findById(request.getMenuGroupId()).orElseThrow(IllegalArgumentException::new),
            menuProducts);

        if (menuGroupRepository.existsById(menu.menuGroupId())) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.productId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        menu.validatePrice(sum);

        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenu(savedMenu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.changeMenuProduct(savedMenuProducts);

        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProduct(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
