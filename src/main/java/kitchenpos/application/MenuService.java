package kitchenpos.application;

import kitchenpos.application.dto.convertor.MenuConvertor;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException(String.format("존재하지 않는 메뉴 그룹입니다. [%s]", request.getMenuGroupId()));
        }
        final Menu savedMenu = menuRepository.save(toMenu(request));
        return MenuConvertor.toMenuResponse(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuConvertor.toMenuResponses(menus);
    }

    private Menu toMenu(final MenuRequest request) {
        return new Menu(
            request.getName(),
            request.getPrice(),
            request.getMenuGroupId(),
            toMenuProducts(request.getMenuProducts())
        );
    }

    private List<MenuProduct> toMenuProducts(final List<MenuProductRequest> requests) {
        return requests.stream()
            .map(request -> {
                final Product product = findProductById(request);
                return new MenuProduct(request.getProductId(), request.getQuantity(), product.getPrice());
            })
            .collect(Collectors.toUnmodifiableList());
    }

    private Product findProductById(final MenuProductRequest request) {
        return productDao.findById(request.getProductId())
            .orElseThrow(() ->
                new IllegalArgumentException(String.format("존재하지 않는 상품입니다. [%s]", request.getProductId()))
            );
    }
}
