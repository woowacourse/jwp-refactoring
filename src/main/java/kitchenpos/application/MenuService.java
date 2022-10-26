package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productRepository;

    public MenuService(final MenuDao menuDao,
                       final MenuGroupDao menuGroupDao,
                       final ProductDao productRepository) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuGroupDao.findById(menuRequest.getMenuGroupId());
        final MenuProducts menuProducts = new MenuProducts(
                new Price(menuRequest.getPrice()),
                toMenuProducts(menuRequest.getMenuProducts()));

        final Menu menu = menuDao.save(toMenu(menuRequest));
        menuProducts.changeAllMenuId(menu.getId());

        return toMenuResponse(menu, toMenuProductResponses(menu.getId(), menu.getMenuProducts()));
    }

    private Menu toMenu(MenuRequest menuRequest) {
        return new Menu(null, menuRequest.getName(), menuRequest.getPrice(),
                menuRequest.getMenuGroupId(),
                toMenuProducts(menuRequest.getMenuProducts()));
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequest> savedMenuProducts) {
        return savedMenuProducts.stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(MenuProductRequest mp) {
        Long productId = mp.getProductId();
        return new MenuProduct(productId, mp.getQuantity(), productRepository.findById(productId).getPrice());
    }

    private List<MenuProductResponse> toMenuProductResponses(Long menuId, List<MenuProduct> savedMenuProducts) {
        return savedMenuProducts.stream()
                .map(mp -> new MenuProductResponse(mp.getSeq(), menuId, mp.getProductId(), mp.getQuantity()))
                .collect(Collectors.toList());
    }

    private MenuResponse toMenuResponse(Menu menu, List<MenuProductResponse> menuProductResponses) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                menu.getMenuGroupId(), menuProductResponses);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu -> toMenuResponse(menu, toMenuProductResponses(menu.getId(), menu.getMenuProducts())))
                .collect(Collectors.toList());
    }
}
