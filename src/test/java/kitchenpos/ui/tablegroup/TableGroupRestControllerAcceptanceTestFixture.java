package kitchenpos.ui.tablegroup;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import kitchenpos.helper.IntegrationTestHelper;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static kitchenpos.fixture.TableGroupFixture.단체_지정_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupRestControllerAcceptanceTestFixture extends IntegrationTestHelper {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    protected <T> ExtractableResponse 단체를_지정한다(final String url, final T request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    protected OrderTable 주문_테이블을_생성한다(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    protected void 단체가_성공적으로_지정된다(final ExtractableResponse response, final TableGroup tableGroup) {
        TableGroupResponse result = response.as(TableGroupResponse.class);

        assertThat(result.getId())
                .isEqualTo(tableGroup.getId());
    }

    protected ExtractableResponse 단체를_제거한다(final String url) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then().log().all()
                .extract();
    }

    protected void 단체가_성공적으로_제거된다(final ExtractableResponse response) {
        int statusCode = response.statusCode();

        assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    protected TableGroupResponse 단체_데이터_생성(final TableGroup tableGroup) {
        return tableGroupService.create(단체_지정_생성_요청(tableGroup));
    }
}
