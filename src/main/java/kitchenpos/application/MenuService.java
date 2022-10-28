package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<Product> products = mapToProducts(menuRequest);

        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(),
                menuRequest.getMenuProducts(), products);

        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();

        for (final MenuProduct menuProduct : menuRequest.getMenuProducts()) {
            final MenuProduct target = new MenuProduct(menuRepository.findById(savedMenu.getId())
                    .orElseThrow(IllegalArgumentException::new), menuProduct.getProductId(), menuProduct.getQuantity());

            final MenuProduct tempMenuProduct = menuProductRepository.save(target);
            savedMenuProducts.add(tempMenuProduct);
        }

        return new Menu(savedMenu.getId(), savedMenu.getName(),
                savedMenu.getPrice(), savedMenu.getMenuGroupId(), savedMenuProducts, products);
    }

    private List<Product> mapToProducts(MenuRequest menuRequest) {
        final List<Product> products = menuRequest.getMenuProducts()
                .stream()
                .map(mp -> productRepository.findById(mp.getProductId()).get())
                .collect(Collectors.toList());
        return products;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
