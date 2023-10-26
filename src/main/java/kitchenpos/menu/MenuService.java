package kitchenpos.menu;

import kitchenpos.product.Product;
import kitchenpos.product.ProductDao;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final ApplicationEventPublisher eventPublisher;

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(ApplicationEventPublisher eventPublisher, MenuDao menuDao, MenuProductDao menuProductDao, ProductDao productDao) {
        this.eventPublisher = eventPublisher;
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        Menu menu = menuDto.toDomain();
        validateProductSum(menu);
        eventPublisher.publishEvent(MenuCreatedEvent.from(menu));
        final Menu savedMenu = menuDao.save(menu);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();

        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }

        return MenuDto.of(savedMenu, savedMenuProducts);
    }

    private void validateProductSum(Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        menu.validatePrice(sum);
    }

    public List<MenuDto> list() {
        final List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(menu -> new Menu(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
                        menuProductDao.findAllByMenuId(menu.getId())))
                .map(MenuDto::from)
                .collect(Collectors.toList());
    }
}
