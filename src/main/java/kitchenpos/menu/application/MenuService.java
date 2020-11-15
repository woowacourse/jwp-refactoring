package kitchenpos.menu.application;

import kitchenpos.generic.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {

        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹을 선택해주세요."));

        Price sum = Price.ofZero();
        List<Product> products = productRepository.findAllByIdIn(request.getProductIds());

        List<MenuProductCreateRequest> menuProductRequest = request.getMenuProducts();
        for (MenuProductCreateRequest menuProduct : menuProductRequest) {
            for (Product product : products) {
                if (product.isSameId(menuProduct.getProductId())) {
                    sum = sum.add(product.calculatePrice(menuProduct.getQuantity()));
                }
            }
        }

        final BigDecimal requestPrice = BigDecimal.valueOf(request.getPrice());
        if (sum.isLessThan(requestPrice)) {
            throw new IllegalArgumentException(String.format("상품 금액의 합(%d)이 메뉴의 가격(%d)보다 작습니다.", sum.longValue(), requestPrice.longValue()));
        }

        Menu savedMenu = menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup));

        List<MenuProduct> menuProducts = menuProductRequest.stream()
                .map(menuProduct -> new MenuProduct(savedMenu, menuProduct.getProductId(), menuProduct.getQuantity()))
                .collect(Collectors.toList());

        menuProductRepository.saveAll(menuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
