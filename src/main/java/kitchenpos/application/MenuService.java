package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.exception.MenuPriceException;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.ui.dto.MenuProductDto;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;
    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuService(MenuGroupDao menuGroupDao, ProductDao productDao, MenuDao menuDao,
                       MenuProductDao menuProductDao) {
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    @Transactional
    public Menu create(MenuCreateRequest request) {
        validateMenuGroupId(request.getMenuGroupId());
        validatePrice(request.getMenuProducts(), request.getPrice());

        Menu savedMenu = saveMenu(request);
        List<MenuProduct> savedMenuProducts = saveMenuProducts(request.getMenuProducts(), savedMenu.getId());

        return new Menu(savedMenu, savedMenuProducts);
    }

    private void validateMenuGroupId(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new NotFoundMenuGroupException();
        }
    }

    private void validatePrice(List<MenuProductDto> menuProductDtos, BigDecimal price) {
        BigDecimal menuProductsPrice = getMenuProductsPrice(menuProductDtos);
        if (price.compareTo(menuProductsPrice) > 0) {
            throw new MenuPriceException();
        }
    }

    public BigDecimal getMenuProductsPrice(List<MenuProductDto> menuProductDtos) {
        BigDecimal sum = BigDecimal.ZERO;

        for (MenuProductDto menuProductDto : menuProductDtos) {
            Product product = productDao.findById(menuProductDto.getProductId())
                    .orElseThrow(NotFoundProductException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductDto.getQuantity())));
        }

        return sum;
    }

    private Menu saveMenu(MenuCreateRequest request) {
        return menuDao.save(new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId()
        ));
    }

    private List<MenuProduct> saveMenuProducts(List<MenuProductDto> menuProductDtos, Long menuId) {
        return getMenuProducts(menuId, menuProductDtos).stream()
                .map(menuProductDao::save)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> getMenuProducts(Long menuId, List<MenuProductDto> menuProductDtos) {
        return menuProductDtos.stream()
                .map(menuProductDto ->
                        new MenuProduct(menuId, menuProductDto.getProductId(), menuProductDto.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menuDao.findAll().stream()
                .map(menu -> new Menu(menu, menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }
}
