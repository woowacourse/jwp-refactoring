package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;

@Service
public class MenuProductService {

    private final MenuProductRepository menuProductRepository;

    public MenuProductService(final MenuProductRepository menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }

    public List<MenuProduct> findAll(List<Long> menuProductIds) {
        return menuProductRepository.findAllById(menuProductIds);
    }

    public List<MenuProduct> saveAll(Menu menu, List<Product> products, long quantity) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (Product product : products) {
            menuProducts.add(new MenuProduct(menu, product.getId(), quantity));
        }
        return menuProductRepository.saveAll(menuProducts);
    }
}
