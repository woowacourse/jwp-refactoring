package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MenuGroupDao 테스트")
@SpringBootTest
@Transactional
class MenuGroupDaoTest {
    @Autowired
    private MenuGroupDao menuGroupDao;

    private static Stream<Arguments> saveFailureWhenDbLimit() {
        return Stream.of(
                Arguments.of(IntStream.rangeClosed(1, 256)
                        .mapToObj(iter -> "x")
                        .collect(Collectors.joining())
                ),
                Arguments.of((Object) null)
        );
    }

    @DisplayName("메뉴그룹 저장 - 실패 - DB 제약사항")
    @ParameterizedTest
    @MethodSource("saveFailureWhenDbLimit")
    void saveFailureWhenDbLimit(String name) {
        //given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        //when
        //then
        assertThatThrownBy(() -> menuGroupDao.save(menuGroup))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("메뉴그룹 조회 - 성공 - id 기반 조회")
    @Test
    void findById() {
        //given
        //when
        final Optional<MenuGroup> actual = menuGroupDao.findById(MenuGroupFixture.두마리메뉴.getId());
        //then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getName()).isEqualTo(MenuGroupFixture.두마리메뉴.getName());
    }

    @DisplayName("메뉴그룹 조회 - 성공 - 저장된 id가 없을때")
    @Test
    void findByIdWhenNotFound() {
        //given
        //when
        final Optional<MenuGroup> actual = menuGroupDao.findById(0L);
        //then
        assertThat(actual).isEmpty();
    }

    @DisplayName("메뉴그룹 존재 여부 조회 - 성공 - id 기반 조회")
    @Test
    void exitsById() {
        //given
        //when
        final boolean actual = menuGroupDao.existsById(MenuGroupFixture.두마리메뉴.getId());
        //then
        assertThat(actual).isTrue();
    }
}