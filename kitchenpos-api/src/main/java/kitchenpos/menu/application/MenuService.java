package kitchenpos.menu.application;

import kitchenpos.menu.application.request.MenuCreateRequest;
import kitchenpos.menu.application.request.MenuProductCreateRequest;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuService {

    private MenuRepository menuRepository;
    private MenuGroupRepository menuGroupRepository;
    private ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        MenuGroup menuGroup = getMenugroup(menuCreateRequest);
        MenuProducts menuProducts = getMenuProducts(menuCreateRequest);

        Menu menu = Menu.of(menuCreateRequest.getName(), menuCreateRequest.getPrice(), menuGroup, menuProducts);
        Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    private MenuGroup getMenugroup(MenuCreateRequest menuCreateRequest) {
        return menuGroupRepository.findById(menuCreateRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
    }


    private MenuProducts getMenuProducts(MenuCreateRequest menuCreateRequest) {
        List<MenuProduct> menuProduct = menuCreateRequest
                .getMenuProductCreateRequests()
                .stream()
                .map(it -> new MenuProduct(getProduct(it), it.getQuantity()))
                .collect(Collectors.toList());

        return new MenuProducts(menuProduct);
    }

    private Product getProduct(MenuProductCreateRequest menuProductCreateRequest) {
        return productRepository.findById(menuProductCreateRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
