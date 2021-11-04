package kitchenpos.dao;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.domain.Menu;
import kitchenpos.fixture.MenuFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MenuDao 테스트")
@SpringBootTest
@Transactional
class MenuDaoTest {
    @Autowired
    private MenuDao menuDao;

    private static Stream<Arguments> saveFailureWhenDbLimit() {
        return Stream.of(
                Arguments.of(IntStream.rangeClosed(1, 256)
                                .mapToObj(iter -> "x")
                                .collect(Collectors.joining()),
                        BigDecimal.valueOf(19000L),
                        MenuFixture.후라이드치킨.getMenuGroupId()
                ),
                Arguments.of(null,
                        BigDecimal.valueOf(19000L),
                        MenuFixture.후라이드치킨.getMenuGroupId()
                ),
                Arguments.of(MenuFixture.후라이드치킨.getName(),
                        new BigDecimal("123456789123456789.00"),
                        MenuFixture.후라이드치킨.getMenuGroupId()
                ),
                Arguments.of(MenuFixture.후라이드치킨.getName(),
                        null,
                        MenuFixture.후라이드치킨.getMenuGroupId()
                ),
                Arguments.of(MenuFixture.후라이드치킨.getName(),
                        MenuFixture.후라이드치킨.getPrice(),
                        0L
                ),
                Arguments.of(MenuFixture.후라이드치킨.getName(),
                        MenuFixture.후라이드치킨.getPrice(),
                        null
                )
        );
    }

    @DisplayName("메뉴 저장 - 실패 - DB 제약사항")
    @CustomParameterizedTest
    @MethodSource("saveFailureWhenDbLimit")
    void saveFailureWhenDbLimit(String name, BigDecimal price, Long menuGroupId) {
        //given
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        //when
        //then
        assertThatThrownBy(() -> menuDao.save(menu))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("메뉴 조회 - 성공 - id 기반 조히")
    @Test
    void findById() {
        //given
        //when
        final Optional<Menu> actual = menuDao.findById(MenuFixture.후라이드치킨.getId());
        //then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getName()).isEqualTo(MenuFixture.후라이드치킨.getName());
    }

    @DisplayName("메뉴 조회 - 성공 - 저장된 id가 없을때")
    @Test
    void findByIdWhenNotFound() {
        //given
        //when
        final Optional<Menu> actual = menuDao.findById(0L);
        //then
        assertThat(actual).isEmpty();
    }

    @DisplayName("메뉴 수 조회 - 성공")
    @Test
    void countByIdIn() {
        //given
        final List<Long> ids = Arrays.asList(MenuFixture.후라이드치킨.getId(), MenuFixture.양념치킨.getId(), MenuFixture.간장치킨.getId());
        //when
        final long actual = menuDao.countByIdIn(ids);
        //then
        assertThat(actual).isEqualTo(ids.size());
    }

    @DisplayName("메뉴 수 조회 - 성공 - 저장된 메뉴가 없을 때")
    @Test
    void countByIdInWhenNotFound() {
        //given
        final List<Long> ids = Arrays.asList(0L, -1L);
        //when
        final long actual = menuDao.countByIdIn(ids);
        //then
        assertThat(actual).isZero();
    }
}