package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuRepositoryTest(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Test
    void 저장한다() {
        // given
        Menu menu = menu(1L, "pasta", 13000);

        // when
        Menu savedMenu = menuRepository.save(menu);

        // then
        Assertions.assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo("pasta")
        );
    }

    @Test
    void ID로_조회한다() {
        Optional<Menu> menu = menuRepository.findById(1L);

        // then
        Assertions.assertAll(
                () -> assertThat(menu).isPresent(),
                () -> {
                    Menu actual = menu.get();
                    Assertions.assertAll(
                            () -> assertThat(actual.getName()).isEqualTo("후라이드치킨"),
                            () -> assertThat(actual.getMenuGroupId()).isEqualTo(2L)
                    );
                }
        );
    }

    @Test
    void 일치하는_ID가_없는_경우_empty를_반환한다() {
        Optional<Menu> foundMenu = menuRepository.findById(-1L);
        assertThat(foundMenu).isEmpty();
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given & when
        List<Menu> menus = menuRepository.findAll();

        // then
        Assertions.assertAll(
                () -> assertThat(menus)
                        .hasSize(6)
                        .usingRecursiveComparison()
                        .ignoringFields("price", "menuProducts")
                        .isEqualTo(
                                Arrays.asList(
                                        menu(1L, "후라이드치킨", 16000),
                                        menu(2L, "양념치킨", 16000),
                                        menu(3L, "반반치킨", 16000),
                                        menu(4L, "통구이", 16000),
                                        menu(5L, "간장치킨", 17000),
                                        menu(6L, "순살치킨", 17000)
                                )
                        )
        );
    }

    @Test
    void ID로_개수를_센다() {
        // given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        // when
        long count = menuRepository.countByIdIn(ids);

        // then
        assertThat(count).isEqualTo(3);
    }

    @Test
    void fetch_join으로_메뉴를_조회한다() {
        // given
        List<Menu> menus = menuRepository.findAllWithMenuProducts();

        // then
        Assertions.assertAll(
                () -> assertThat(menus).hasSize(6),
                () -> assertThat(menus.get(0))
                        .usingRecursiveComparison()
                        .ignoringFields("price", "menuProducts")
                        .isEqualTo(menu(1L, "후라이드치킨", 16000)));
    }

    private Menu menu(final long id, final String name, final int price) {
        return new Menu(id, name, price(price), 2L, new ArrayList<>());
    }

    private Price price(final int price) {
        return new Price(BigDecimal.valueOf(price));
    }
}
