package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.response.MenuResponse;
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
