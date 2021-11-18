package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("메뉴 그룹 기능")
@Sql({"classpath:tableInit.sql", "classpath:dataInsert.sql"})
public class MenuGroupAcceptanceTest extends ApplicationTest {

    @BeforeEach
    void setUp() {
        super.setUp();
        메뉴_그룹_생성(기본_메뉴_그룹);
    }

    @DisplayName("메뉴그룹 등록에 성공하면 201 응답을 받는다.")
    @Test
    void createMenuGroup() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("포츈메뉴그룹");

        //when
        ExtractableResponse<Response> response = 메뉴_그룹_생성(menuGroup);
        MenuGroup responseMenuGroup = response.as(MenuGroup.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("전체 메뉴그룹을 불러오는데 성공하면, 200 응답을 받는다.")
    @Test
    void getMenuGroup() {

        //given
        ExtractableResponse<Response> response = 전체_그룹_조회();

        //when
        List<MenuGroup> menuGroups = response.jsonPath().getList(".", MenuGroup.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(menuGroups).hasSize(5);  // defaultData 4 + insertData 1
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성(final MenuGroup menuGroup) {
        return RestAssured
            .given().log().all()
            .body(menuGroup)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/api/menu-groups")
            .then()
            .extract();
    }

    private ExtractableResponse<Response> 전체_그룹_조회() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menu-groups")
            .then().log().all()
            .extract();
    }
}
