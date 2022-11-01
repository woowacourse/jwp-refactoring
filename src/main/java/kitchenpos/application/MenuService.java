package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(final MenuRepository menuRepository, final MenuGroupDao menuGroupDao, final ProductDao productDao) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final Menu menu = Menu.ofNew(request.getName(), request.getPrice(), request.getMenuGroupId());
        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(menuProductRequest -> getMenuProductOf(menu, menuProductRequest))
                .collect(Collectors.toList());
        menu.changeMenuProducts(menuProducts);

        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.from(menus);
    }

    private MenuProduct getMenuProductOf(final Menu menu, final MenuProductRequest menuProductRequest) {
        final Product product = getProductFrom(menuProductRequest);
        return MenuProduct.ofNew(menu, product, menuProductRequest.getQuantity());
    }

    private Product getProductFrom(final MenuProductRequest menuProductRequest) {
        return productDao.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
