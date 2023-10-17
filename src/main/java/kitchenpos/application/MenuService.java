package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuPriceValidator;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.ui.dto.MenuProductDto;
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

    public MenuService(MenuRepository menuRepository, ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다. 메뉴를 등록할 수 없습니다."));

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        List<MenuProduct> menuProducts = getMenuProducts(menuRequest, menu);
        Menu savedMenu = menuRepository.save(menu);

        MenuPriceValidator.validate(savedMenu, menuProducts);

        savedMenu.addMenuProducts(menuProducts);

        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> getMenuProducts(MenuRequest menuRequest, Menu menu) {
        List<MenuProductDto> menuProductRequests = menuRequest.getMenuProducts();
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductDto request : menuProductRequests) {
            Product product = productRepository.findById(request.getProductId())
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
