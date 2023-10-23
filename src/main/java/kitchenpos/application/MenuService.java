package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.JpaMenuGroupRepository;
import kitchenpos.dao.JpaMenuProductRepository;
import kitchenpos.dao.JpaMenuRepository;
import kitchenpos.dao.JpaProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
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

        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = jpaProductRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(new MenuProduct(menu, product, menuProductRequest.getQuantity()));
        }

        menu.addMenuProducts(menuProducts);

        final Menu savedMenu = jpaMenuRepository.save(menu);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(savedMenu);
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
