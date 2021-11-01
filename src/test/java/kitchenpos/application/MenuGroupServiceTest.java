package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MenuGroupService 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private final MenuGroupFixture menuGroupFixture = new MenuGroupFixture();

    @Test
    @DisplayName("메뉴그룹 생성 테스트 - 성공")
    public void createTest() throws Exception {
        // given
        MenuGroup 메뉴그룹1 = menuGroupFixture.메뉴그룹_생성("메뉴그룹1");
        MenuGroup expected = menuGroupFixture.메뉴그룹_생성(1L, "메뉴그룹1");
        given(menuGroupDao.save(메뉴그룹1)).willReturn(expected);
        // when
        MenuGroup actual = menuGroupService.create(메뉴그룹1);

        // then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("모든 메뉴그룹 조회 테스트 - 성공")
    public void listTest() throws Exception {
        // given
        MenuGroup 메뉴그룹1 = menuGroupFixture.메뉴그룹_생성(1L, "메뉴그룹1");
        MenuGroup 메뉴그룹2 = menuGroupFixture.메뉴그룹_생성(2L, "메뉴그룹2");
        List<MenuGroup> expected = menuGroupFixture.메뉴그룹_리스트_생성(메뉴그룹1, 메뉴그룹2);
        given(menuGroupDao.findAll()).willReturn(expected);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertIterableEquals(expected, actual);
    }
}
