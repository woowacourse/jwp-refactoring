package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        validateMenuGroupExisted(menuRequest);

        Menu menu = menuRepository.save(new Menu(menuRequest.getName(), menuRequest.getPrice(),
                menuRequest.getMenuGroupId(), mapToMenuProduct(menuRequest)));

        return MenuResponse.from(menu);
    }

    private void validateMenuGroupExisted(final MenuRequest menuRequest) {
        if (!menuGroupDao.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private List<MenuProduct> mapToMenuProduct(final MenuRequest menuRequest) {
        return menuRequest.getMenuProducts()
                .stream()
                .map(menuProduct -> {
                    Product product = productDao.findById(menuProduct.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
                    return new MenuProduct(product.getId(), menuProduct.getQuantity(), product.getPrice());
                }).collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
