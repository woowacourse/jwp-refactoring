package kitchenpos.application;

import static java.util.stream.Collectors.*;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductSaveRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.MenuSaveRequest;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;
    private final MenuRepository menuRepository;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao,
            final MenuRepository menuRepository) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuSaveRequest request) {

        Menu savedMenu = menuRepository.save(new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                toMenuProducts(request.getMenuProducts()))
        );

        return new MenuResponse(savedMenu);
    }

    private List<MenuProduct> toMenuProducts(final List<MenuProductSaveRequest> requests) {
        return requests.stream()
                .map(this::toMenuProduct)
                .collect(toList());
    }

    private MenuProduct toMenuProduct(final MenuProductSaveRequest request) {
        Product product = productDao.getById(request.getProductId());
        return new MenuProduct(product.getId(), request.getQuantity(), product.getPrice());
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::new)
                .collect(toList());
    }
}
