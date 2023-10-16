package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.request.MenuCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuProductRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private MenuRepository menuRepository;
    private MenuGroupRepository menuGroupRepository;
    private MenuProductRepository menuProductRepository;
    private ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(MenuCreateRequest request) {
        Menu menu = request.toEntity();

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (menu.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        Menu savedMenu = menuRepository.save(menu);

        Long menuId = savedMenu.getId();
        List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        return menuRepository.findAllByFetch();
    }
}
