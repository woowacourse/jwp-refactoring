package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRequests;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRequests, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRequests = menuProductRequests;
        this.productRepository = productRepository;
    }

    public MenuResponse create(MenuRequest request) {
        MenuGroup menuGroup = findMenuGroup(request.getMenuGroupId());
        Menu menu = menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup));

        List<MenuProduct> menuProducts = createMenuProducts(menu, request);
        menu.addMenuProducts(menuProducts);

        return new MenuResponse(menu);
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."));
    }

    private List<MenuProduct> createMenuProducts(Menu menu, MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(m -> {
                    Product product = findProduct(m.getProductId());
                    return new MenuProduct(menu, product, m.getQuantity());
                }).collect(Collectors.toList());
    }

    private Product findProduct(Long menuProductId) {
        return productRepository.findById(menuProductId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }
}
