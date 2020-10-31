package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductPrice;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        BigDecimal productsPriceSum = calculateProductsPriceSum(menuRequest);
        final Menu savedMenu = menuRepository.save(menuRequest.toMenu(menuGroup, productsPriceSum));

        addMenuProductToMenu(menuRequest, savedMenu);

        return MenuResponse.of(savedMenu);
    }

    private BigDecimal calculateProductsPriceSum(MenuRequest menuRequest) {
        BigDecimal sum = BigDecimal.ZERO;
        final List<MenuProductDto> menuProductDtos = menuRequest.getMenuProducts();
        for (final MenuProductDto menuProductDto : menuProductDtos) {
            final Product product = productRepository.findById(menuProductDto.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            ProductPrice productPrice = product.getProductPrice();
            sum = sum.add(productPrice.multiply(menuProductDto.getQuantity()));
        }
        return sum;
    }

    private void addMenuProductToMenu(MenuRequest menuRequest, Menu savedMenu) {
        List<MenuProduct> menuProducts = savedMenu.getMenuProducts();
        for (final MenuProductDto menuProductDto : menuRequest.getMenuProducts()) {
            Product product = productRepository.findById(menuProductDto.getProductId()).orElseThrow(IllegalArgumentException::new);
            MenuProduct menuProductToSave = new MenuProduct(savedMenu, product, menuProductDto.getQuantity());
            menuProducts.add(menuProductRepository.save(menuProductToSave));
        }
    }

    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuRepository.findAllFetch());
    }
}
