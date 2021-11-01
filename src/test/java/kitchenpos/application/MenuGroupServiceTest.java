package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static kitchenpos.testutils.TestDomainBuilder.menuGroupBuilder;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@MockitoSettings
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        // given
        MenuGroup newMenuGroup = menuGroupBuilder().name("두마리메뉴").build();

        // when
        menuGroupService.create(newMenuGroup);

        // then
        then(menuGroupDao)
                .should(times(1))
                .save(newMenuGroup);
    }

    @DisplayName("전체 메뉴 그룹 리스트를 가져온다.")
    @Test
    void list() {
        // when
        menuGroupService.list();

        // then
        then(menuGroupDao)
                .should(times(1))
                .findAll();
    }
}
