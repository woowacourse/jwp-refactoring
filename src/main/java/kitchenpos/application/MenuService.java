package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dtos.MenuRequest;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Menu create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final Menu menu = Menu.builder()
                .name(request.getName())
                .price(BigDecimal.valueOf(request.getPrice()))
                .menuGroupId(request.getMenuGroupId())
                .build();

        final Menu savedMenu = menuRepository.save(menu);
        final MenuProducts savedMenuProducts = new MenuProducts(menuProductRepository.findAllByMenuId(savedMenu.getId()));
        final List<Product> savedProducts = productRepository.findAllByIdIn(savedMenuProducts.getProductIds());
        savedMenuProducts.checkValidityOfMenuPrice(savedProducts, savedMenu.getPrice());
        savedMenu.updateMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
