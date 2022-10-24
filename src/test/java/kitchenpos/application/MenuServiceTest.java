package kitchenpos.application;

import static kitchenpos.DomainFixture.메뉴_세팅;
import static kitchenpos.DomainFixture.뿌링클_생성;
import static kitchenpos.DomainFixture.뿌링클_치즈볼_메뉴_생성;
import static kitchenpos.DomainFixture.세트_메뉴;
import static kitchenpos.DomainFixture.치즈볼_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    private long productAId;
    private long productBId;
    private long menuGroupId;

    @BeforeEach
    void setUp() {
        productAId = 상품_저장(뿌링클_생성()).getId();
        productBId = 상품_저장(치즈볼_생성()).getId();

        menuGroupId = 메뉴_그룹_저장(세트_메뉴).getId();
    }

    @DisplayName("메뉴 생성 테스트")
    @Nested
    class CreateTest {

        @Test
        void 메뉴를_생성하고_결과를_반환한다() {
            // given
            final var menu = 메뉴_세팅(뿌링클_치즈볼_메뉴_생성(), menuGroupId, productAId, productBId);

            // when
            final var createdMenu = menuService.create(menu);

            // then
            assertAll(
                    () -> assertThat(createdMenu.getId()).isNotNull(),
                    () -> assertThat(createdMenu.getName()).isEqualTo(menu.getName()),
                    () -> assertThat(createdMenu.getPrice()).isEqualByComparingTo(menu.getPrice()),
                    () -> assertThat(createdMenu.getMenuGroupId()).isEqualTo(menuGroupId),
                    () -> assertThat(createdMenu.getMenuProducts()).hasSize(2)
            );
        }

        @Test
        void 메뉴_가격이_없는_경우_예외를_던진다() {
            // given
            final var menu = 뿌링클_치즈볼_메뉴_생성();
            menu.setPrice(null);
            final var setMenu = 메뉴_세팅(menu, menuGroupId, productAId, productBId);

            // when & then
            assertThatThrownBy(() -> menuService.create(setMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격이_0보다_작은_경우_예외를_던진다() {
            // given
            final var menu = 뿌링클_치즈볼_메뉴_생성();
            menu.setPrice(BigDecimal.valueOf(-1));
            final var setMenu = 메뉴_세팅(menu, menuGroupId, productAId, productBId);

            // when & then
            assertThatThrownBy(() -> menuService.create(setMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격이_메뉴_상품들의_가격들의_합보다_크면_예외를_던진다() {
            // given
            final var menu = 뿌링클_치즈볼_메뉴_생성();
            menu.setPrice(BigDecimal.valueOf(30_000));
            final var setMenu = 메뉴_세팅(menu, menuGroupId, productAId, productBId);

            // when & then
            assertThatThrownBy(() -> menuService.create(setMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_목록을_조회한다() {
            // given
            final var menuA = 뿌링클_치즈볼_메뉴_생성();
            메뉴_세팅_및_저장(menuA, menuGroupId, productAId, productBId);
            final var menuB = 뿌링클_치즈볼_메뉴_생성();
            메뉴_세팅_및_저장(menuB, menuGroupId, productAId, productBId);

            // when
            List<Menu> foundMenus = menuService.list();

            // then
            assertThat(foundMenus).hasSizeGreaterThanOrEqualTo(2);
        }
    }
}
