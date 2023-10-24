package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public Menu create(final MenuCreateRequest menuCreateRequest) {
        MenuGroup menuGroup = getMenugroup(menuCreateRequest);

        Menu menu = new Menu(menuCreateRequest.getName(), menuCreateRequest.getPrice(), menuGroup, new ArrayList<>());

        menu.validatePrice();

        MenuProducts menuProducts = getMenuProducts(menuCreateRequest);

        menu.checkProductPriceSumEqualsPrice(menuProducts.calculateSum());

        menuProducts.setMenu(menu);

        return menuRepository.save(menu);
    }

    private MenuProducts getMenuProducts(MenuCreateRequest menuCreateRequest) {
        List<MenuProduct> menuProduct = menuCreateRequest
                .getMenuProductCreateRequests()
                .stream()
                .map(it -> new MenuProduct(getProduct(it), it.getQuantity()))
                .collect(Collectors.toList());

        return new MenuProducts(menuProduct);
    }

    private MenuGroup getMenugroup(MenuCreateRequest menuCreateRequest) {
        return menuGroupRepository.findById(menuCreateRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private Product getProduct(MenuProductCreateRequest menuProductCreateRequest) {
        return productRepository.findById(menuProductCreateRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
