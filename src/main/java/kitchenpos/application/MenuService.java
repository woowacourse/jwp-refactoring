package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.exception.MenuGroupNotExistException;
import kitchenpos.exception.NullRequestException;
import kitchenpos.exception.ProductNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
    public Menu create(final MenuCreateRequest menuCreateRequest) {
        validateMenuCreateRequest(menuCreateRequest);

        String name = menuCreateRequest.getName();
        BigDecimal price = new BigDecimal(menuCreateRequest.getPrice());
        Long menuGroupId = menuCreateRequest.getMenuGroupId();
        List<MenuProductRequest> menuProductRequests = menuCreateRequest.getMenuProductRequests();

        Menu savedMenu = menuDao.save(new Menu(name, price, menuGroupId));

        menuProductRequests.stream()
            .map(request -> new MenuProduct(savedMenu.getId(), request.getProductId(), request.getQuantity()))
            .forEach(menuProductDao::save);

        return savedMenu;
    }

    private void validateMenuCreateRequest(MenuCreateRequest menuCreateRequest) {
        BigDecimal price = new BigDecimal(menuCreateRequest.getPrice());
        Long menuGroupId = menuCreateRequest.getMenuGroupId();
        List<MenuProductRequest> menuProductRequests = menuCreateRequest.getMenuProductRequests();

        validateEmpty(menuCreateRequest);
        validateMenuGroupExistence(menuGroupId);
        validatePriceSum(price, menuProductRequests);
    }

    private void validateEmpty(MenuCreateRequest menuCreateRequest) {
        String name = menuCreateRequest.getName();
        Long price = menuCreateRequest.getPrice();
        Long menuGroupId = menuCreateRequest.getMenuGroupId();
        List<MenuProductRequest> menuProductRequests = menuCreateRequest.getMenuProductRequests();

        if (Objects.isNull(name) || Objects.isNull(price) || Objects.isNull(menuGroupId) || menuProductRequests.isEmpty()) {
            throw new NullRequestException();
        }

        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Long productId = menuProductRequest.getProductId();
            Long quantity = menuProductRequest.getQuantity();
            if (Objects.isNull(productId) || Objects.isNull(quantity)) {
                throw new NullRequestException();
            }
        }
    }

    private void validateMenuGroupExistence(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new MenuGroupNotExistException();
        }
    }

    private void validatePriceSum(BigDecimal price, List<MenuProductRequest> menuProductRequests) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productDao.findById(menuProductRequest.getProductId())
                .orElseThrow(ProductNotExistException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new InvalidPriceException();
        }
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
