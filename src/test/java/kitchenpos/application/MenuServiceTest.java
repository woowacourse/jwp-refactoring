package kitchenpos.application;

import static kitchenpos.support.DomainFixture.뿌링클;
import static kitchenpos.support.DomainFixture.뿌링클_치즈볼_메뉴_생성;
import static kitchenpos.support.DomainFixture.세트_메뉴;
import static kitchenpos.support.DomainFixture.치즈볼;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.menu.MenuGroupRepository;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:truncate.sql")
class MenuServiceTest {

    private MenuRepository menuRepository;
    private MenuGroupRepository menuGroupRepository;
    private ProductRepository productRepository;
    private MenuService menuService;

    private Product productA;
    private Product productB;
    private Long menuGroupId;

    @Autowired
    public MenuServiceTest(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
                           final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuService = new MenuService(menuRepository, menuGroupRepository, productRepository);
    }

    @BeforeEach
    void setUp() {
        productA = productRepository.save(뿌링클);
        productB = productRepository.save(치즈볼);
        menuGroupId = menuGroupRepository.save(세트_메뉴).getId();
    }

    @Test
    void 메뉴를_생성하고_결과를_반환한다() {
        final var request = new MenuCreateRequest("뿌링클+치즈볼", 23_000, menuGroupId,
                List.of(new MenuProductCreateRequest(productA.getId(), 1),
                        new MenuProductCreateRequest(productB.getId(), 1))
        );

        // when
        final var created = menuService.create(request);

        // then
        assertAll(
                () -> assertThat(created.getId()).isNotNull(),
                () -> assertThat(created.getName()).isEqualTo(request.getName()),
                () -> assertThat(created.getPrice().intValue()).isEqualTo(request.getPrice()),
                () -> assertThat(created.getMenuGroupId()).isEqualTo(menuGroupId),
                () -> assertThat(created.getMenuProducts()).hasSize(2)
        );
    }

    @Test
    void 메뉴_가격이_없는_경우_예외를_던진다() {
        final var request = new MenuCreateRequest("뿌링클과 치즈볼", null, menuGroupId,
                List.of(new MenuProductCreateRequest(productA.getId(), 1),
                        new MenuProductCreateRequest(productB.getId(), 1))
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 존재하지_않는_메뉴_그룹인_경우_예외를_던진다() {
        final var request = new MenuCreateRequest("뿌링클과 치즈볼", 23_000, 0L,
                List.of(new MenuProductCreateRequest(productA.getId(), 1),
                        new MenuProductCreateRequest(productB.getId(), 1))
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.MENU_GROUP_NOT_FOUND_ERROR);
    }

    @Test
    void 존재하지_않는_상품이_있는_경우_예외를_던진다() {
        final var request = new MenuCreateRequest("뿌링클과 치즈볼", 23_000, menuGroupId,
                List.of(new MenuProductCreateRequest(0L, 1),
                        new MenuProductCreateRequest(productB.getId(), 1))
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.PRODUCT_NOT_FOUND_ERROR);
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        menuRepository.save(뿌링클_치즈볼_메뉴_생성(menuGroupId, productA, productB));
        menuRepository.save(뿌링클_치즈볼_메뉴_생성(menuGroupId, productA, productB));

        // when
        final var foundMenus = menuService.list();

        // then
        assertThat(foundMenus).hasSize(2);
    }
}
