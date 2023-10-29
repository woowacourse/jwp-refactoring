package kitchenpos.core.menu.application;

import kitchenpos.core.Product.domain.Product;
import kitchenpos.core.Product.repository.ProductRepository;
import kitchenpos.core.menu.domain.Menu;
import kitchenpos.core.menu.presentation.dto.MenuProductCreateRequest;
import kitchenpos.core.menu.repository.MenuRepository;
import kitchenpos.core.menugroup.repository.MenuGroupRepository;
import kitchenpos.core.price.vo.Price;
import kitchenpos.core.menu.domain.MenuProduct;
import kitchenpos.core.menu.presentation.dto.MenuCreateRequest;
import kitchenpos.core.menu.repository.MenuProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository,
                       final MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    public Menu create(final MenuCreateRequest request) {
        final List<MenuProductCreateRequest> menuProductCreateRequestList = request.getMenuProducts();

        validateSaveMenu(Price.from(request.getPrice()),menuProductCreateRequestList);

        final Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                menuGroupRepository.getById(request.getMenuGroupId())
        );
        final Menu savedMenu = saveMenuProduct(menuProductCreateRequestList, menu);
        return savedMenu;
    }

    private Menu saveMenuProduct(final List<MenuProductCreateRequest> menuProductCreateRequestList, final Menu menu) {
        final Menu savedMenu = menuRepository.save(menu);

        for (final MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequestList) {
            final Product product = productRepository.getById(menuProductCreateRequest.getProductId());

            final MenuProduct menuProduct = new MenuProduct(
                    menu,
                    product,
                    menuProductCreateRequest.getQuantity()
            );
            menuProductRepository.save(menuProduct);
        }
        return savedMenu;
    }

    private void validateSaveMenu(final Price menuPrice,
                                  final List<MenuProductCreateRequest> request){
        if (request.isEmpty()) {
            throw new IllegalArgumentException("메뉴 상품은 한 개 이상입니다.");
        }
        final List<Price> prices = getPrices(request);

        final Price productPriceSum = Price.sum(prices);
        if (productPriceSum.isLessThan(menuPrice)){
            throw new IllegalArgumentException("메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 합니다.");
        }
    }

    private List<Price> getPrices(final List<MenuProductCreateRequest> request) {
        final List<Price> prices = new ArrayList<>();

        for (final MenuProductCreateRequest menuProductCreateRequest : request) {
            final Product product = productRepository.getById(menuProductCreateRequest.getProductId());
            prices.add(product.getPrice());
        }
        return prices;
    }

    @Transactional(readOnly = true)
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }
}
