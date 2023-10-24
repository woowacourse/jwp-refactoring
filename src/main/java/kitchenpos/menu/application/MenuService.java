package kitchenpos.menu.application;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuName;
import kitchenpos.menu.MenuPrice;
import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.menugroup.application.MenuGroupRepository;
import kitchenpos.menuproduct.SaveMenuProductsEvent;
import kitchenpos.menuproduct.application.request.MenuProductRequest;
import kitchenpos.product.Product;
import kitchenpos.product.application.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class MenuService {
    private final ApplicationEventPublisher publisher;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final ApplicationEventPublisher publisher,
                       final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.publisher = publisher;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }


    public Menu create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        final Menu savedMenu = saveMenu(request, request.getPrice(), menuProductRequests);
        publisher.publishEvent(new SaveMenuProductsEvent(menuProductRequests, savedMenu));

        return savedMenu;
    }

    private Menu saveMenu(final MenuRequest request, final BigDecimal price, final List<MenuProductRequest> requests) {
        final BigDecimal sum = getSumOfMenuProductRequests(requests);
        validatePriceAndSum(price, sum);
        return menuRepository.save(
                new Menu(new MenuName(request.getName()), new MenuPrice(request.getPrice()), request.getMenuGroupId())
        );
    }

    private BigDecimal getSumOfMenuProductRequests(final List<MenuProductRequest> menuProductRequests) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }
        return sum;
    }

    private void validatePriceAndSum(final BigDecimal price, final BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
