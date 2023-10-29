package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ServiceTest;
import kitchenpos.application.CreateMenuCommand.CreateMenuProductCommand;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Nested
    class 메뉴_생성 {

        @Test
        void 가격은_음수일_수_없다() {
            //given
            MenuGroup 메뉴_그룹 = 메뉴_그룹_만들기();

            BigDecimal 음수_가격 = BigDecimal.valueOf(-1);
            List<CreateMenuProductCommand> 상품_요청들 = 상품_요청_만들기();

            CreateMenuCommand 커맨드 = new CreateMenuCommand("메뉴명", 음수_가격, 메뉴_그룹.getId(), 상품_요청들);

            //expect
            assertThatThrownBy(() -> menuService.create(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격은_null일_수_없다() {
            //given
            MenuGroup 메뉴_그룹 = 메뉴_그룹_만들기();
            CreateMenuCommand 커맨드 = new CreateMenuCommand("메뉴명", null, 메뉴_그룹.getId(), 상품_요청_만들기());

            //expect
            assertThatThrownBy(() -> menuService.create(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹이_존재하지_않으면_예외가_발생한다() {
            //given
            MenuGroup 메뉴_그룹 = 메뉴_그룹_만들기();
            menuGroupRepository.deleteById(메뉴_그룹.getId());
            CreateMenuCommand 커맨드 = new CreateMenuCommand("메뉴명", BigDecimal.valueOf(1_000), 메뉴_그룹.getId(), 상품_요청_만들기());

            //expect
            assertThatThrownBy(() -> menuService.create(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴에_속하는_상품이_없으면_예외가_발생한다() {
            //given
            CreateMenuCommand 커맨드 = new CreateMenuCommand("메뉴명", BigDecimal.valueOf(1_000), 메뉴_그룹_만들기().getId(),
                    emptyList());

            //expect
            assertThatThrownBy(() -> menuService.create(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 1})
        void 메뉴가격이_상품가격합_이하여야한다(Long 차감수) {
            //given
            MenuGroup 메뉴_그룹 = 메뉴_그룹_만들기();
            final var 존재하는_상품_목록 = productRepository.findAll().subList(0, 2);
            final long 가격_합 = 메뉴_상품가격합_구하기(존재하는_상품_목록);

            final var 상품_요청_목록 = 존재하는_상품_목록.stream()
                    .map(product -> new CreateMenuProductCommand(product.getId(), 1))
                    .collect(Collectors.toList());

            CreateMenuCommand 커맨드 = new CreateMenuCommand("메뉴명", BigDecimal.valueOf(가격_합 - 차감수), 메뉴_그룹.getId(),
                    상품_요청_목록);

            //when
            MenuDto 생성된_메뉴 = menuService.create(커맨드);

            //then
            assertThat(생성된_메뉴.getId()).isNotNull();
        }

        @Test
        void 메뉴가격이_상품가격합보다_큰_경우_예외가_발생한다() {
            //given
            MenuGroup 메뉴_그룹 = 메뉴_그룹_만들기();

            final var 존재하는_상품_목록 = productRepository.findAll().subList(0, 2);
            final long 가격_합 = 메뉴_상품가격합_구하기(존재하는_상품_목록);

            final var 상품_요청_목록 = 존재하는_상품_목록.stream()
                    .map(product -> new CreateMenuProductCommand(product.getId(), 1))
                    .collect(Collectors.toList());

            CreateMenuCommand 커맨드 = new CreateMenuCommand("메뉴명", BigDecimal.valueOf(가격_합 + 1), 메뉴_그룹.getId(),
                    상품_요청_목록);

            //expect
            assertThatThrownBy(() -> menuService.create(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private long 메뉴_상품가격합_구하기(final List<Product> 상품_목록) {
            long 가격_합 = 상품_목록.stream()
                    .mapToLong(product -> product.getPrice().getValue().longValue())
                    .sum();
            return 가격_합;
        }

    }

    private MenuGroup 메뉴_그룹_만들기() {
        MenuGroup 저장할_그룹 = new MenuGroup("메뉴그룹");
        return menuGroupRepository.save(저장할_그룹);
    }

    private List<CreateMenuProductCommand> 상품_요청_만들기() {
        final var 상품_아이디 = productRepository.findAll().get(0).getId();
        final var 메뉴_상품 = new CreateMenuProductCommand(상품_아이디, 1);
        return List.of(메뉴_상품);
    }

    @Nested
    class 메뉴_목록_조회 {

        @Test
        void 메뉴_목록을_조회할_수_있다() {
            //given
            List<Long> 모든_메뉴_아이디 = menuRepository.findAll().stream()
                    .map(Menu::getId)
                    .collect(Collectors.toList());

            //when
            List<MenuDto> 메뉴_목록 = menuService.list();

            //then
            assertThat(메뉴_목록).extracting(MenuDto::getId)
                    .containsAll(모든_메뉴_아이디);
        }

    }

}
