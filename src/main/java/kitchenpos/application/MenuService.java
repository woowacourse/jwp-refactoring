package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductCreateRequest;
import kitchenpos.ui.response.MenuResponse;
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
