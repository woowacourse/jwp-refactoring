package kitchenpos.menu;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.prodcut.JpaProductRepository;
import kitchenpos.prodcut.Price;
import kitchenpos.prodcut.Product;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.request.MenuRequest;
import kitchenpos.ui.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final JpaMenuRepository jpaMenuRepository;
    private final JpaMenuGroupRepository jpaMenuGroupRepository;
    private final JpaMenuProductRepository jpaMenuProductRepository;
    private final JpaProductRepository jpaProductRepository;

    public MenuService(
            final JpaMenuRepository jpaMenuRepository,
            final JpaMenuGroupRepository jpaMenuGroupRepository,
            final JpaMenuProductRepository jpaMenuProductRepository,
            final JpaProductRepository jpaProductRepository
    ) {
        this.jpaMenuRepository = jpaMenuRepository;
        this.jpaMenuGroupRepository = jpaMenuGroupRepository;
        this.jpaMenuProductRepository = jpaMenuProductRepository;
        this.jpaProductRepository = jpaProductRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        if (request.getMenuGroupId() == null) {
            throw new IllegalArgumentException();
        }

        MenuGroup menuGroup = jpaMenuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        Menu menu = new Menu(request.getName(), new Price(request.getPrice()), menuGroup);

        List<MenuProductRequest> menuProductRequests = request.getMenuProductRequests();

        Menu savedMenu = jpaMenuRepository.save(menu);

        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = jpaProductRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(new MenuProduct(savedMenu, product, menuProductRequest.getQuantity()));
        }

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            savedMenuProducts.add(jpaMenuProductRepository.save(menuProduct));
        }
        savedMenu.addMenuProducts(savedMenuProducts);

        return new MenuResponse(savedMenu);
    }

    public List<Menu> list() {
        final List<Menu> menus = jpaMenuRepository.findAll();

        for (final Menu menu : menus) {
            menu.addMenuProducts(jpaMenuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
