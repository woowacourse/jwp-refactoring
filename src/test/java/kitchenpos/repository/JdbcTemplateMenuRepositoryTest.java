package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class JdbcTemplateMenuRepositoryTest {

    private final MenuRepository menuRepository;

    @Autowired
    public JdbcTemplateMenuRepositoryTest(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Test
    void 저장한다() {
        // given
        BigDecimal price = BigDecimal.valueOf(13000);
        Menu menu = new Menu(null, "pasta", price, 1L, new ArrayList<>());

        // when
        Menu savedMenu = menuRepository.save(menu);

        // then
        Assertions.assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo("pasta"),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(1L)
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
                                        new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), 2L, new ArrayList<>()),
                                        new Menu(2L, "양념치킨", BigDecimal.valueOf(16000), 2L, new ArrayList<>()),
                                        new Menu(3L, "반반치킨", BigDecimal.valueOf(16000), 2L, new ArrayList<>()),
                                        new Menu(4L, "통구이", BigDecimal.valueOf(16000), 2L, new ArrayList<>()),
                                        new Menu(5L, "간장치킨", BigDecimal.valueOf(17000), 2L, new ArrayList<>()),
                                        new Menu(6L, "순살치킨", BigDecimal.valueOf(17000), 2L, new ArrayList<>())
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
                        .isEqualTo(new Menu(
                                1L,
                                "후라이드치킨",
                                BigDecimal.valueOf(16000),
                                2L,
                                new ArrayList<>()
                        )));
    }
}
