package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.request.MenuCreateRequest;
import kitchenpos.application.request.MenuProductCreateRequest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.persistence.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    @Nested
    class 생성 {

        @Test
        void 성공() {
            // given
            Product productA = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000.00)));
            Product productB = productRepository.save(new Product("치즈볼", BigDecimal.valueOf(1000.00)));
            Long menuGroupId = menuGroupDao.save(new MenuGroup("스폐셜")).getId();
            MenuCreateRequest request = new MenuCreateRequest(
                "고추바사삭 스폐셜 세트", BigDecimal.valueOf(22000.00), menuGroupId,
                List.of(
                    new MenuProductCreateRequest(productA.getId(), 2),
                    new MenuProductCreateRequest(productB.getId(), 2)
                ));

            // when
            Menu actual = menuService.create(request);

            // then
            assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getMenuProducts())
                    .allSatisfy(it -> assertThat(it.getSeq()).isPositive())
            );
        }

        @Test
        void 가격이_음수면_예외() {
            // given
            Long menuGroupId = menuGroupDao.save(new MenuGroup("순살")).getId();
            Product productA = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000.00)));
            Product productB = productRepository.save(new Product("치즈볼", BigDecimal.valueOf(1000.00)));
            MenuCreateRequest request = new MenuCreateRequest(
                "고추바사삭 스폐셜 세트", BigDecimal.valueOf(-22000.00), menuGroupId,
                List.of(
                    new MenuProductCreateRequest(productA.getId(), 2),
                    new MenuProductCreateRequest(productB.getId(), 2)
                ));

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 해당하는_아이디의_메뉴그룹이_없으면_예외() {
            // given
            Product productA = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000.00)));
            Product productB = productRepository.save(new Product("치즈볼", BigDecimal.valueOf(1000.00)));
            MenuCreateRequest request = new MenuCreateRequest(
                "고추바사삭 스폐셜 세트", BigDecimal.valueOf(-22000.00), -100L,
                List.of(
                    new MenuProductCreateRequest(productA.getId(), 2),
                    new MenuProductCreateRequest(productB.getId(), 2)
                ));

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 해당하는_상품이_없으면_예외() {
            // given
            Long menuGroupId = menuGroupDao.save(new MenuGroup("스폐셜")).getId();
            MenuCreateRequest request = new MenuCreateRequest(
                "고추바사삭 스폐셜 세트", BigDecimal.valueOf(22000.00), menuGroupId,
                List.of(
                    new MenuProductCreateRequest(100L, 2)
                ));

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴상품_가격의_합이_메뉴_가격보다_크면_예외() {
            // given
            Product productA = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000.00)));
            Product productB = productRepository.save(new Product("치즈볼", BigDecimal.valueOf(1000.00)));
            Long menuGroupId = menuGroupDao.save(new MenuGroup("스폐셜")).getId();
            MenuCreateRequest request = new MenuCreateRequest(
                "고추바사삭 스폐셜 세트", BigDecimal.valueOf(20001.00), menuGroupId,
                List.of(
                    new MenuProductCreateRequest(productA.getId(), 1),
                    new MenuProductCreateRequest(productB.getId(), 1)
                ));

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 모든_메뉴를_반환() {
        // given
        Product productA = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000.00)));
        Product productB = productRepository.save(new Product("치즈볼", BigDecimal.valueOf(2000.00)));
        Product productC = productRepository.save(new Product("감튀", BigDecimal.valueOf(1000.00)));

        Long menuGroupIdA = menuGroupDao.save(new MenuGroup("치즈볼 세트")).getId();
        Long menuGroupIdB = menuGroupDao.save(new MenuGroup("감튀 세트")).getId();
        MenuCreateRequest menuA = new MenuCreateRequest(
            "고추바사삭 치즈볼 세트", BigDecimal.valueOf(8000.00), menuGroupIdA,
            List.of(
                new MenuProductCreateRequest(productA.getId(), 1),
                new MenuProductCreateRequest(productB.getId(), 1)
            ));
        MenuCreateRequest menuB = new MenuCreateRequest(
            "고추바사삭 감튀 세트", BigDecimal.valueOf(9000.00), menuGroupIdB,
            List.of(
                new MenuProductCreateRequest(productA.getId(), 1),
                new MenuProductCreateRequest(productC.getId(), 1)
            ));
        List<Menu> expected = new ArrayList<>();

        expected.add(menuService.create(menuA));
        expected.add(menuService.create(menuB));

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
