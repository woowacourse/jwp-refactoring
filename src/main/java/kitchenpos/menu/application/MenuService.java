package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    public MenuResponse create(MenuCreateRequest menuCreateRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuCreateRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 메뉴 그룹입니다."));

        Menu menu = menuRepository.save(new Menu(menuCreateRequest.getName(), menuCreateRequest.getPrice(), menuGroup));
        List<MenuProduct> menuProducts = createdProductsForMenu(menu, menuCreateRequest.getMenuProducts());
        menu.updateProducts(menuProducts);

        return MenuResponse.from(menu);
    }

    private List<MenuProduct> createdProductsForMenu(Menu menu, List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> createMenuProduct(menu, menuProduct))
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(Menu menu, MenuProductRequest menuProduct) {
        Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품입니다."));
        return new MenuProduct(menu, product, menuProduct.getQuantity());
    }
}
