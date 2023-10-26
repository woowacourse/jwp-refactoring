package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.ProductRepository;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(final MenuRepository menuRepository,
                       final ProductRepository productRepository,
                       final MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다. 메뉴를 등록할 수 없습니다."));
        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        final List<MenuProduct> menuProducts = getMenuProducts(menuRequest, menu);

        final Menu savedMenu = menuRepository.save(menu);
        savedMenu.registerMenuProducts(menuProducts);

        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> getMenuProducts(final MenuRequest menuRequest, final Menu menu) {
        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        final List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductRequest request : menuProductRequests) {
            final Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴상품입니다. 메뉴를 등록할 수 없습니다."));
            menuProducts.add(new MenuProduct(request.getSeq(), menu, product, request.getQuantity()));
        }
        return menuProducts;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream().map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
