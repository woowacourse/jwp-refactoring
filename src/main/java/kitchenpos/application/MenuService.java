package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupDao menuGroupDao, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다. menuGroupId = " + request.getMenuGroupId());
        }
        return MenuResponse.from(
                menuRepository.save(
                        new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(),
                                mapToMenuProducts(request)))
        );
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> mapToMenuProducts(MenuRequest request) {
        List<MenuProduct> result = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            Long productId = menuProductRequest.getProductId();
            Product product = productRepository.getById(productId);
            result.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return result;
    }
}
