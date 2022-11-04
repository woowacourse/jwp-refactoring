package kitchenpos.menu.application;

import static java.util.stream.Collectors.*;

import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuProductSaveRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuSaveRequest;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final ProductDao productDao;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(final ProductDao productDao, final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository) {
        this.productDao = productDao;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuSaveRequest request) {
        validateExsistsMenuGroup(request);
        Menu savedMenu = menuRepository.save(new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                toMenuProducts(request.getMenuProducts()))
        );

        return new MenuResponse(savedMenu);
    }

    private void validateExsistsMenuGroup(final MenuSaveRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
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
