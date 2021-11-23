package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuRequest.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuProducts menuProducts = newMenuProducts(menuRequest);
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(),
            menuRequest.getMenuGroupId(),
            menuProducts);

        return MenuResponse.from(menuRepository.save(menu));
    }

    private MenuProducts newMenuProducts(final MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Long productId = menuProductRequest.getProductId();
            menuProducts.add(new MenuProduct(productId, menuProductRequest.getQuantity()));
        }

        return new MenuProducts(menuProducts);
    }

    public List<MenuResponse> list() {
        return MenuResponse.listFrom(menuRepository.findAll());
    }
}
