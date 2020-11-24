package kitchenpos.application;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductPrice;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup savedMenuGroup;
    private Product savedProduct1;
    private Product savedProduct2;
    private Product savedProduct3;
    private MenuCreateRequest.MenuProductDto menuProductDto1;
    private MenuCreateRequest.MenuProductDto menuProductDto2;
    private MenuCreateRequest.MenuProductDto menuProductDto3;

    @BeforeEach
    void setUp() {
        this.savedMenuGroup = createSavedMenuGroup("두마리메뉴");
        this.savedProduct1 = createSavedProduct("양념치킨", ProductPrice.from(BigDecimal.valueOf(16_000)));
        this.savedProduct2 = createSavedProduct("간장치킨", ProductPrice.from(BigDecimal.valueOf(16_000)));
        this.savedProduct3 = createSavedProduct("후라이드치킨", ProductPrice.from(BigDecimal.valueOf(15_000)));
        this.menuProductDto1 = new MenuCreateRequest.MenuProductDto(this.savedProduct1.getId(), 1);
        this.menuProductDto2 = new MenuCreateRequest.MenuProductDto(this.savedProduct2.getId(), 1);
        this.menuProductDto3 = new MenuCreateRequest.MenuProductDto(this.savedProduct3.getId(), 1);
    }

    @DisplayName("새로운 메뉴 생성")
    @Test
    void createMenuTest() {
        List<MenuCreateRequest.MenuProductDto> menuProductDtos = Arrays.asList(this.menuProductDto1, this.menuProductDto2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", BigDecimal.valueOf(28_000),
                                                                    this.savedMenuGroup.getId(), menuProductDtos);

        MenuResponse menuResponse = this.menuService.createMenu(menuCreateRequest);

        assertAll(
                () -> assertThat(menuResponse).isNotNull(),
                () -> assertThat(menuResponse.getName()).isEqualTo(menuCreateRequest.getName()),
                () -> assertThat(menuResponse.getPrice()).isEqualTo(menuCreateRequest.getPrice()),
                () -> assertThat(menuResponse.getMenuGroupId()).isEqualTo(menuCreateRequest.getMenuGroupId()),
                () -> assertThat(menuResponse.getMenuProductDtos()).hasSize(menuCreateRequest.getMenuProductDtos().size())
        );
    }

    @DisplayName("새로운 메뉴를 생성할 때 가격이 존재하지 않으면 예외 발생")
    @Test
    void createMenuWithNullPriceThenThrowException() {
        List<MenuCreateRequest.MenuProductDto> menuProductDtos = Arrays.asList(this.menuProductDto1, this.menuProductDto2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", null, this.savedMenuGroup.getId(),
                                                                    menuProductDtos);

        assertThatThrownBy(() -> this.menuService.createMenu(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 가격이 0 미만이면 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -9999})
    void createMenuWithInvalidPriceThenThrowException(int invalidPrice) {
        List<MenuCreateRequest.MenuProductDto> menuProductDtos = Arrays.asList(this.menuProductDto1, this.menuProductDto2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", BigDecimal.valueOf(invalidPrice),
                                                                    this.savedMenuGroup.getId(), menuProductDtos);

        assertThatThrownBy(() -> this.menuService.createMenu(menuCreateRequest)).isInstanceOf(InvalidMenuPriceException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 존재하지 않는 메뉴 그룹을 지정하면 예외 발생")
    @Test
    void createMenuWithNotExistMenuGroupThenThrowException() {
        long notExistMenuGroupId = -1L;
        List<MenuCreateRequest.MenuProductDto> menuProductDtos = Arrays.asList(this.menuProductDto1, this.menuProductDto2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", BigDecimal.valueOf(28_000),
                                                                    notExistMenuGroupId, menuProductDtos);

        assertThatThrownBy(() -> this.menuService.createMenu(menuCreateRequest)).isInstanceOf(MenuGroupNotFoundException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 존재하지 않는 상품을 지정하면 예외 발생")
    @Test
    void createMenuWithNotExistMenuProductThenThrowException() {
        long notExistProductId = -1L;
        MenuCreateRequest.MenuProductDto menuProductDto = new MenuCreateRequest.MenuProductDto(notExistProductId, 1);
        List<MenuCreateRequest.MenuProductDto> menuProductCreateRequests = Collections.singletonList(menuProductDto);

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", BigDecimal.valueOf(28_000),
                                                                    this.savedMenuGroup.getId(), menuProductCreateRequests);

        assertThatThrownBy(() -> this.menuService.createMenu(menuCreateRequest)).isInstanceOf(ProductNotFoundException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 메뉴의 가격이 지정한 상품 가격의 총합을 초과하면 예외 발생")
    @Test
    void createMenuWithInvalidPriceThenThrowException() {
        Product savedProduct1 = createSavedProduct("양념치킨", ProductPrice.from(BigDecimal.valueOf(16_000)));
        Product savedProduct2 = createSavedProduct("간장치킨", ProductPrice.from(BigDecimal.valueOf(16_000)));
        MenuCreateRequest.MenuProductDto menuProductDto1 = new MenuCreateRequest.MenuProductDto(savedProduct1.getId(), 1);
        MenuCreateRequest.MenuProductDto menuProductDto2 = new MenuCreateRequest.MenuProductDto(savedProduct2.getId(), 1);
        List<MenuCreateRequest.MenuProductDto> menuProductCreateRequests = Arrays.asList(menuProductDto1, menuProductDto2);

        BigDecimal invalidPrice = BigDecimal.valueOf(33_000);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", invalidPrice,
                                                                    this.savedMenuGroup.getId(),
                                                                    menuProductCreateRequests);

        assertThatThrownBy(() -> this.menuService.createMenu(menuCreateRequest)).isInstanceOf(InvalidMenuPriceException.class);
    }

    @DisplayName("존재하는 모든 메뉴를 조회")
    @Test
    void listMenuTest() {
        List<MenuCreateRequest.MenuProductDto> menuProductDtos1 = Arrays.asList(this.menuProductDto1, this.menuProductDto2);
        MenuCreateRequest menuCreateRequest1 = new MenuCreateRequest("양념간장두마리메뉴", BigDecimal.valueOf(28_000),
                                                                     this.savedMenuGroup.getId(), menuProductDtos1);
        List<MenuCreateRequest.MenuProductDto> menuProductDtos2 = Arrays.asList(this.menuProductDto1, this.menuProductDto3);
        MenuCreateRequest menuCreateRequest2 = new MenuCreateRequest("후라이드양념두마리메뉴", BigDecimal.valueOf(27_000),
                                                                     this.savedMenuGroup.getId(), menuProductDtos2);

        List<MenuCreateRequest> menuCreateRequests = Arrays.asList(menuCreateRequest1, menuCreateRequest2);
        menuCreateRequests.forEach(menuCreateRequest -> this.menuService.createMenu(menuCreateRequest));

        List<MenuResponse> menuResponses = this.menuService.listAllMenus();

        assertThat(menuResponses).hasSize(menuCreateRequests.size());
    }

    private MenuGroup createSavedMenuGroup(String menuName) {
        MenuGroup menuGroup = new MenuGroup(menuName);
        return this.menuGroupRepository.save(menuGroup);
    }

    private Product createSavedProduct(String name, ProductPrice productPrice) {
        Product product = new Product(name, productPrice);
        return this.productRepository.save(product);
    }
}