package kitchenpos.application.menu;

import kitchenpos.application.menu.dto.MenuCreateRequest;
import kitchenpos.application.menu.dto.MenuProductCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
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
