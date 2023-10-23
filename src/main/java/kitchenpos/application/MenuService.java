package kitchenpos.application;

import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.application.dto.response.CreateMenuResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.mapper.MenuMapper;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductRepository productRepository
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public CreateMenuResponse create(final CreateMenuRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final Menu menu = getMenu(request);
        final Menu entity = menuDao.save(menu);
        final Long menuId = entity.getId();
        final List<MenuProduct> menuProductEntities = entity.getMenuProducts().stream()
                .map(menuProduct -> menuProduct.updateMenuId(menuId))
                .map(menuProductDao::save).collect(Collectors.toList());

        return CreateMenuResponse.from(entity.updateMenuProducts(menuProductEntities));
    }

    private Menu getMenu(final CreateMenuRequest request) {
        final Menu menu = MenuMapper.toMenu(request);

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        menu.validPrice(sum);
        return menu;
    }

    public List<MenuResponse> list() {
        final List<Menu> result = new ArrayList<>();

        for (final Menu menu : menuDao.findAll()) {
            List<MenuProduct> allByMenuId = menuProductDao.findAllByMenuId(menu.getId());
            result.add(menu.updateMenuProducts(allByMenuId));
        }

        return result.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
