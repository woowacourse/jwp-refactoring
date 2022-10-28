package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.PendingMenuProduct;
import kitchenpos.domain.PendingMenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.ui.request.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupDao.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        final PendingMenuProducts products = createPendingMenuProducts(request);
        final Menu menu = menuGroup.createMenu(request.getName(), request.getPrice(), products);
        return menuRepository.save(menu);
    }

    private PendingMenuProducts createPendingMenuProducts(MenuRequest request) {
        final List<PendingMenuProduct> products = request.getMenuProducts().stream()
                .map(p -> {
                    final Product product = productDao.findById(p.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new PendingMenuProduct(product.getId(), product.getPrice(), p.getQuantity());
                })
                .collect(Collectors.toList());
        return new PendingMenuProducts(products);
    }

    @Transactional
    public Menu findById(final long id) {
        return menuRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
