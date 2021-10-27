package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequestDto;
import kitchenpos.dto.MenuCreateResponseDto;
import kitchenpos.dto.MenuProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
        final MenuDao menuDao,
        final MenuGroupDao menuGroupDao,
        final MenuProductDao menuProductDao,
        final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuCreateResponseDto create(final MenuCreateRequestDto menuDto) {
        List<MenuProduct> menuProductGroup = new ArrayList<>();
        for (MenuProductDto menuProductDto : menuDto.getMenuProducts()) {
            Menu menu = menuDao.findById(menuProductDto.getMenuId())
                .orElseGet(null);
            menuProductGroup.add(new MenuProduct(menuProductDto.getSeq(), menuProductDto.getProductId(), menuProductDto.getQuantity(), menu));
        }
        Menu menu = new Menu(menuDto.getId(), menuDto.getName(), menuDto.getPrice(), menuDto.getMenuGroupId(), menuProductGroup);
        menu.validateMenuPrice();

        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal totalMenuProductsPrice = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            totalMenuProductsPrice = totalMenuProductsPrice.add(product.multiplyPrice(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        final BigDecimal price = menu.getPrice();
        if (price.compareTo(totalMenuProductsPrice) > 0) {
            throw new IllegalArgumentException();
        }

        // menu를 DB에 저장
        final Menu savedMenu = menuDao.save(menu);

        // menu에 있는 여러 MenuProduct들을 DB에 저장
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(savedMenu);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);


        return new MenuCreateResponseDto(savedMenu);
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
