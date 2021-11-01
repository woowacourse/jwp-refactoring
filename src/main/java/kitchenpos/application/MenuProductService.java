package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;

@Service
public class MenuProductService {

    private final MenuProductRepository menuProductRepository;

    public MenuProductService(final MenuProductRepository menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }

    public List<MenuProduct> findAll(List<Long> menuProductIds) {
        return menuProductIds.stream()
            .map(menuProductId -> menuProductRepository.findById(menuProductId)
                .orElseThrow(IllegalArgumentException::new))
            .collect(Collectors.toList());
    }

    public void saveMenu(List<MenuProduct> menuProducts, Menu savedMenu) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.addMenu(savedMenu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }

        savedMenu.addMenuProducts(savedMenuProducts);
    }

    public List<MenuProduct> findAllByMenu(Menu menu) {
        return menuProductRepository.findAllByMenu(menu);
    }

    public List<MenuProduct> saveAll(Menu menu, List<Product> products, long quantity) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (Product product : products) {
            menuProducts.add(new MenuProduct(menu, product, quantity));
        }
        return menuProductRepository.saveAll(menuProducts);
    }
}
