package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuRepository,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        validateMenuGroupExisted(menuRequest);

        return menuRepository.save(new Menu(menuRequest.getName(), menuRequest.getPrice(),
                menuRequest.getMenuGroupId(), mapToMenuProduct(menuRequest)));
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

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
