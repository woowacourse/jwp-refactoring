package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuFixture.메뉴_생성_요청;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.ProductFixture.상품;
import static kitchenpos.fixture.ProductFixture.양념치킨_16000;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuDto;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Test
    void 메뉴를_생성할_수_있다() {
        // given
        final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

        final var 후라이드 = productRepository.save(후라이드_16000);
        final var 양념치킨 = productRepository.save(양념치킨_16000);

        final var 후라이드_1개 = 메뉴상품(후라이드, 1);
        final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

        final var request = 메뉴_생성_요청("후1양1", 25000, 두마리메뉴.getId(), List.of(후라이드_1개, 양념치킨_1개));

        // when
        final var response = menuService.create(request);

        // then
        assertThat(menuRepository.findById(response.getId())).isPresent();
        assertThat(menuProductRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void 메뉴의_가격이_0원_미만일_경우_생성할_수_없다() {
        // given
        final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

        final var 후라이드 = productRepository.save(후라이드_16000);
        final var 양념치킨 = productRepository.save(양념치킨_16000);

        final var 후라이드_1개 = 메뉴상품(후라이드, 1);
        final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

        final var request = 메뉴_생성_요청("후1양1", -10000, 두마리메뉴.getId(), List.of(후라이드_1개, 양념치킨_1개));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_메뉴그룹일_경우_생성할_수_없다() {
        // given
        final var 후라이드 = productRepository.save(후라이드_16000);
        final var 양념치킨 = productRepository.save(양념치킨_16000);

        final var 후라이드_1개 = 메뉴상품(후라이드, 1);
        final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

        final var wrongMenuGroupId = 999L;

        final var request = 메뉴_생성_요청("후1양1", 25000, wrongMenuGroupId, List.of(후라이드_1개, 양념치킨_1개));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품일_경우_생성할_수_없다() {
        // given
        final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

        final var wrongProduct1 = 상품(998L, "잘못된 상품", 16000L);
        final var wrongProduct2 = 상품(999L, "잘못된 상품", 20000L);

        final var 후라이드_1개 = 메뉴상품(wrongProduct1, 1);
        final var 양념치킨_1개 = 메뉴상품(wrongProduct2, 1);

        final var request = 메뉴_생성_요청("후1양1", 25000, 두마리메뉴.getId(), List.of(후라이드_1개, 양념치킨_1개));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_상품가격과_수량의_곱의_합보다_클_경우_생성할_수_없다() {
        // given
        final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

        final var 후라이드 = productRepository.save(후라이드_16000);
        final var 양념치킨 = productRepository.save(양념치킨_16000);

        final var 후라이드_1개 = 메뉴상품(후라이드, 1);
        final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

        final var request = 메뉴_생성_요청("후1양1", 35000, 두마리메뉴.getId(), List.of(후라이드_1개, 양념치킨_1개));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_목록을_조회할_수_있다() {
        // given
        final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

        final var 후라이드 = productRepository.save(후라이드_16000);
        final var 양념치킨 = productRepository.save(양념치킨_16000);

        final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
        menuRepository.save(후라이드메뉴);

        final var 양념메뉴 = 메뉴("싼양념", 15000, 두마리메뉴, List.of(메뉴상품(양념치킨, 1)));
        menuRepository.save(양념메뉴);

        final var 메뉴목록 = List.of(후라이드메뉴, 양념메뉴);
        final var expected = 메뉴목록.stream()
                .map(MenuDto::toDto)
                .collect(Collectors.toList());

        // when
        final var actual = menuService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
