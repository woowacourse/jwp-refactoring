package kitchenpos.application;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MenuGroup 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룸 저장 - 성공")
    @CustomParameterizedTest
    @ValueSource(strings = {"두마리메뉴", "두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴"})
    void create(@ConvertWith(MenuGroupConverter.class) MenuGroup expect) {
        //given
        //when
        final MenuGroup actual = menuGroupService.create(expect);
        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(expect.getName());
    }

    @DisplayName("메뉴그룹 조회 - 전체 메뉴그룹 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<MenuGroup> actual = menuGroupService.list();
        //then
        assertThat(actual).extracting(MenuGroup::getName)
                .containsAnyElementsOf(MenuGroupFixture.menuGroupNames());
    }

    private static class MenuGroupConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertThat(targetType).isEqualTo(MenuGroup.class);
            final MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName(source.toString());
            return menuGroup;
        }
    }
}