package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import kitchenpos.product.repository.ProductRepository;
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

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹을 선택해주세요."));

        Products products = new Products(productRepository.findAllByIdIn(request.getProductIds()));

        List<MenuProduct> menuProductCollection = new ArrayList<>();
        for (MenuProductCreateRequest menuProductRequest : request.getMenuProducts()) {
            Product product = products.findProductBy(menuProductRequest.getProductId());
            Long quantity = menuProductRequest.getQuantity();
            menuProductCollection.add(new MenuProduct(product, quantity));
        }

        Menu savedMenu = menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup));
        MenuProducts menuProducts = new MenuProducts(menuProductCollection, savedMenu);

        menuProductRepository.saveAll(menuProducts.getMenuProducts());

        return savedMenu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
