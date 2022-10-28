package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.request.MenuRequest;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository,
            final MenuProductRepository menuProductRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final Menu menu = Menu.of(request.getName(), request.getPrice(), request.getMenuGroupId(),
                getMenuProducts(request));

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("어느 하나의 메뉴 그룹에는 속해야 합니다.");
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        final BigDecimal price = menu.getPrice();

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격이 상품(product)의 금액 총합보다 크면 안됩니다.");
        }

        final Menu savedMenu = menuRepository.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(MenuResponse::from)
                .collect(toList());
    }

    private static List<MenuProduct> getMenuProducts(final MenuRequest request) {
        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        return menuProductRequests.stream()
                .map(it -> new MenuProduct(it.getMenuId(), it.getProductId(), it.getQuantity()))
                .collect(toList());
    }
}
