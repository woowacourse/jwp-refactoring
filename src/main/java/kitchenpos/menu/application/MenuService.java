package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.ui.request.CreateMenuRequest;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.response.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

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
    public MenuResponse create(final CreateMenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
        final Menu menu = getMenu(request);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private Menu getMenu(CreateMenuRequest request) {
        final Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());

        List<MenuProduct> menuProducts = new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : request.getMenuProducts()) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                                                     .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
            sum = sum.add(product.calculatePrice(menuProduct.getQuantity()));
            menuProducts.add(new MenuProduct(menu, product.getId(), menuProduct.getQuantity()));
        }

        menu.addMenuProducts(menuProducts, sum);
        return menu;
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                             .stream()
                             .map(MenuResponse::from)
                             .collect(Collectors.toList());
    }
}
