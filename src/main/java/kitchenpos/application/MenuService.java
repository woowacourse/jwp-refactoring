package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.vo.menu.MenuProductRequest;
import kitchenpos.vo.menu.MenuRequest;
import kitchenpos.vo.menu.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    @PersistenceContext
    EntityManager entityManager;

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
    public MenuResponse create(final MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId());
        Menu savedMenu = menuRepository.save(menu);

        List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            MenuProduct menuProduct = new MenuProduct(savedMenu, product, menuProductRequest.getQuantity());
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return MenuResponse.of(savedMenu);
    }


    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
