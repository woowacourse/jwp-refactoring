package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.request.MenuCreateRequest;
import kitchenpos.application.request.MenuProductCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(MenuCreateRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProduct> menuProducts = getMenuProducts(request.getMenuProducts());
        return menuRepository.save(
            new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(), menuProducts));
    }

    public List<Menu> list() {
        return menuRepository.findAllByFetch();
    }

    private List<MenuProduct> getMenuProducts(List<MenuProductCreateRequest> menuProductsDto) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductCreateRequest menuProduct : menuProductsDto) {
            Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(new MenuProduct(product, menuProduct.getQuantity()));
        }
        return menuProducts;
    }
}
