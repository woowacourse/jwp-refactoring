package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.exception.MenuGroupNotFoundException;
import kitchenpos.application.exception.ProductNotFoundException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.CreateMenuProductRequest;
import kitchenpos.ui.dto.request.CreateMenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productDao;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productDao
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final CreateMenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                                                       .orElseThrow(MenuGroupNotFoundException::new);
        final List<MenuProduct> menuProducts = findMenuProducts(request);
        final Menu menu = Menu.of(request.getName(), request.getPrice(), menuProducts, menuGroup);

        return menuRepository.save(menu);
    }

    private List<MenuProduct> findMenuProducts(final CreateMenuRequest menuRequest) {
        final List<MenuProduct> menuProducts = new ArrayList<>();

        for (final CreateMenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                                              .orElseThrow(ProductNotFoundException::new);

            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }

        return menuProducts;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
