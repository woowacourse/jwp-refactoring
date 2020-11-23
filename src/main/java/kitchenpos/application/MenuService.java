package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.menu.MenuProductRequest;
import kitchenpos.ui.dto.menu.MenuRequest;
import kitchenpos.ui.dto.menu.MenuResponse;
import kitchenpos.ui.dto.menu.MenuResponses;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
        MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
            .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 없습니다. 메뉴 그룹을 선택하세요"));

        List<MenuProductRequest> menuProducts = request.getMenuProducts();
        BigDecimal sumOfProductsPrice = sumProductsPrice(menuProducts);

        Menu menu = request.toEntity(sumOfProductsPrice, menuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.of(savedMenu, findMenuProducts(menuProducts, savedMenu));
    }

    private BigDecimal sumProductsPrice(List<MenuProductRequest> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    private List<MenuProduct> findMenuProducts(List<MenuProductRequest> menuProducts, Menu savedMenu) {
        return menuProducts.stream()
            .map(menuProduct -> {
                Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
                return menuProductRepository.save(new MenuProduct(savedMenu, product, menuProduct.getQuantity()));
            }).collect(Collectors.toList());
    }

    public MenuResponses list() {
        final List<Menu> menus = menuRepository.findAll();

        final List<MenuResponse> menuResponses = menus.stream()
            .map(menu -> MenuResponse.of(menu, menuProductRepository.findAllByMenuId(menu.getId())))
            .collect(Collectors.toList());

        return MenuResponses.from(menuResponses);
    }
}
