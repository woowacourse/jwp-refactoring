package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.MenuCreateDto;
import kitchenpos.application.dto.MenuProductCreateDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴를_등록할_수_있다() {
        // given
        final Product savedProduct = createProduct(2000);
        final MenuProductCreateDto menuProductCreateDto = new MenuProductCreateDto(
            savedProduct.getId(), 3L);
        final MenuGroup savedMenuGroup = createMenuGroup("테스트 메뉴 그룹");

        final MenuCreateDto menuCreateDto = new MenuCreateDto(
            "테스트 메뉴",
            savedMenuGroup.getId(),
            new BigDecimal(2000),
            List.of(menuProductCreateDto)
        );

        // when
        final Menu result = menuService.create(menuCreateDto);

        // then
        assertThat(result.getName()).isEqualTo(menuCreateDto.getName());
        assertThat(result.getPrice()).isEqualByComparingTo(menuCreateDto.getPrice());
    }

    @Test
    void 메뉴가격이_상품가격보다_클_경우_예외가_발생한다() {
        // given
        final Product savedProduct = createProduct(2000);
        final MenuProductCreateDto menuProductCreateDto = new MenuProductCreateDto(
            savedProduct.getId(), 3L);
        final MenuGroup savedMenuGroup = createMenuGroup("테스트 메뉴 그룹");

        // when
        final MenuCreateDto menuCreateDto = new MenuCreateDto(
            "테스트 메뉴",
            savedMenuGroup.getId(),
            new BigDecimal(10000),
            List.of(menuProductCreateDto)
        );

        // then
        assertThatThrownBy(() -> menuService.create(menuCreateDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴가격이_0보다_작은경우_예외가_발생한다() {
        // given
        final Product savedProduct = createProduct(2000);
        final MenuProductCreateDto menuProductCreateDto = new MenuProductCreateDto(
            savedProduct.getId(), 3L);
        final MenuGroup savedMenuGroup = createMenuGroup("테스트 메뉴 그룹");

        // when
        final MenuCreateDto menuCreateDto = new MenuCreateDto(
            "테스트 메뉴",
            savedMenuGroup.getId(),
            new BigDecimal(-1),
            List.of(menuProductCreateDto)
        );

        // then
        assertThatThrownBy(() -> menuService.create(menuCreateDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴등록시_메뉴그룹이_존재하지_않으면_예외가_발생한다() {
        // given
        final Product savedProduct = createProduct(2000);
        final MenuProductCreateDto menuProductCreateDto = new MenuProductCreateDto(
            savedProduct.getId(), 3L);
        final Long notExistMenuGroupId = 10000000L;
        final Optional<MenuGroup> findMenuGroup = menuGroupRepository.findById(notExistMenuGroupId);

        // when
        final MenuCreateDto menuCreateDto = new MenuCreateDto(
            "테스트 메뉴",
            notExistMenuGroupId,
            new BigDecimal(3000),
            List.of(menuProductCreateDto)
        );

        // then
        assertThat(findMenuGroup.isEmpty()).isTrue();
        assertThatThrownBy(() -> menuService.create(menuCreateDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴목록을_조회할_수_있다() {
        // given
        final Product savedProduct = createProduct(2000);
        final MenuProductCreateDto menuProductCreateDto = new MenuProductCreateDto(
            savedProduct.getId(), 3L);
        final MenuGroup savedMenuGroup = createMenuGroup("테스트 메뉴 그룹");

        final MenuCreateDto menuCreateDto = new MenuCreateDto(
            "테스트 메뉴",
            savedMenuGroup.getId(),
            new BigDecimal(2000),
            List.of(menuProductCreateDto)
        );
        menuService.create(menuCreateDto);

        // when
        final List<Menu> results = menuService.list();
        final Menu savedMenuResult = results.get(0);
        final List<MenuProduct> savedMenuProductResults = savedMenuResult.getMenuProducts();

        // then
        assertThat(results).hasSize(1);
        assertThat(savedMenuResult).usingRecursiveComparison()
            .ignoringFields("id", "price", "menuProducts")
            .isEqualTo(new Menu(menuCreateDto.getName(), menuCreateDto.getPrice(), savedMenuGroup));
        assertThat(savedMenuResult.getPrice()).isEqualByComparingTo(menuCreateDto.getPrice());
        assertThat(savedMenuProductResults.get(0)).usingRecursiveComparison()
            .ignoringFields("seq", "product.price")
            .isEqualTo(
                MenuProduct.of(savedMenuResult, savedProduct, menuProductCreateDto.getQuantity()));
    }

    private Product createProduct(final int price) {
        final Product product = new Product("테스트 상품", new BigDecimal(price));
        return productRepository.save(product);
    }

    private MenuGroup createMenuGroup(final String groupName) {
        final MenuGroup menuGroup = new MenuGroup(groupName);
        return menuGroupRepository.save(menuGroup);
    }
}
