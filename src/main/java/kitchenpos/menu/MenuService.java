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

        Menu savedMenu = jpaMenuRepository.save(menu);

        List<MenuProductRequest> menuProductRequests = request.getMenuProductRequests();

        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            menuProducts.add(
                    new MenuProduct(savedMenu, menuProductRequest.getProductId(), menuProductRequest.getQuantity())
            );
        }

        validateMenuProduct(savedMenu, menuProducts);

        return new MenuResponse(menu, menuProducts);
    }


    private void validateMenuProduct(final Menu menu, final List<MenuProduct> menuProducts) {
        Price sum = new Price(0);
        for (MenuProduct menuProduct : menuProducts) {
            Product product = jpaProductRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }
        if (menu.getPrice().isGreaterThan(sum)) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = jpaMenuRepository.findAll();

        List<MenuResponse> menuResponses = new ArrayList<>();
        for (final Menu menu : menus) {
            List<MenuProduct> menuProducts = jpaMenuProductRepository.findAllByMenuId(menu.getId());
            menuResponses.add(new MenuResponse(menu, menuProducts));
        }

        return menuResponses;
    }
}
