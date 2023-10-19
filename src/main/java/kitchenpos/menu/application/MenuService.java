package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductCreateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public Menu create(final MenuCreateRequest req) {
        MenuGroup menuGroup = findMenuGroup(req.getMenuGroupId());
        Menu menu = new Menu(null, req.getName(), req.getPrice(), menuGroup);

        List<MenuProduct> menuProducts = makeMenuProducts(req.getMenuProducts(), menu);
        menu.initMenuProducts(menuProducts);

        return menuRepository.save(menu);
    }

    private List<MenuProduct> makeMenuProducts(final List<MenuProductCreateRequest> menuProductCreateRequests, final Menu menu) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (final MenuProductCreateRequest menuProduct : menuProductCreateRequests) {
            Product product = findProduct(menuProduct.getProductId());
            menuProducts.add(new MenuProduct(menu, product, menuProduct.getQuantity()));
        }

        return menuProducts;
    }

    private Product findProduct(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    private MenuGroup findMenuGroup(final Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(MenuGroupNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
