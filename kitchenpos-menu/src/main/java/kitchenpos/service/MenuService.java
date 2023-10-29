package kitchenpos.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductDto;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.InvalidNumberException;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.value.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public MenuResponse create(final CreateMenuRequest request) {

        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 메뉴 그룹이 없습니다."));

        final List<MenuProductDto> menuProductDtos = request.getMenuProducts();

        final List<Long> productIds = menuProductDtos.stream()
                .map(MenuProductDto::getProductId)
                .collect(Collectors.toList());

        final List<Product> products = productRepository.findAllById(productIds);

        BigDecimal sum = BigDecimal.ZERO;

        final List<MenuProduct> menuProducts = new ArrayList<>();

        for (final MenuProductDto dto : menuProductDtos) {
            final Product product = findByIdIn(products, dto.getProductId());
            menuProducts.add(MenuProduct.of(dto, product.getId()));
            sum = sum.add(product.getPrice().multiply(dto.getQuantity()));
        }

        if (sum.compareTo(request.getPrice()) < 0) {
            throw new InvalidNumberException("상품 가격의 총합보다 메뉴가 더 비쌀 수 없습니다.");
        }

        final Menu menu = new Menu(
                request.getName(),
                new Price(request.getPrice()),
                menuGroup.getId(),
                menuProducts
        );

        final Menu savedMenu = menuRepository.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            final MenuProduct newMenuProduct = new MenuProduct(
                    menuProduct.getMenuId(),
                    menuId,
                    menuProduct.getProductId(),
                    menuProduct.getQuantity()
            );
            savedMenuProducts.add(menuProductRepository.save(newMenuProduct));
        }

        return MenuResponse.from(Menu.of(savedMenu, savedMenuProducts));
    }

    private Product findByIdIn(final List<Product> products, final Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 상품이 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
