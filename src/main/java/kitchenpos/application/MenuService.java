package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductRepository productRepository;

    public MenuService(
        MenuDao menuDao,
        MenuGroupDao menuGroupDao,
        MenuProductDao menuProductDao,
        ProductRepository productRepository
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(MenuRequest menuRequest) {
        validateMenuGroupExist(menuRequest);

        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();
        validateMenuPrice(menuRequest, menuProductRequests);

        Menu savedMenu = menuDao.save(menuRequest.toEntity());
        addProducts(menuRequest, savedMenu, savedMenu.getId());
        return MenuResponse.from(savedMenu);
    }

    private void validateMenuGroupExist(MenuRequest menuRequest) {
        if (!menuGroupDao.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private void validateMenuPrice(MenuRequest menuRequest, List<MenuProductRequest> menuProductRequests) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductRequest menuProduct : menuProductRequests) {
            Product product = getProductById(menuProduct.getProductId());
            sum = sum.add(product.multiplyPrice(menuProduct.getQuantity()));
        }

        BigDecimal price = menuRequest.getPrice();
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("가격은 상품들 가격 합보다 클 수 없습니다.");
        }
    }

    private void addProducts(MenuRequest menuRequest, Menu savedMenu, Long menuId) {
        List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProductRequests()) {
            savedMenuProducts.add(menuProductDao.save(menuProductRequest.toEntity(menuId)));
        }
        savedMenu.addMenuProducts(savedMenuProducts);
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuDao.findAll();

        for (Menu menu : menus) {
            menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
