package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("테이블 그룹 기능")
@Sql({"classpath:tableInit.sql", "classpath:dataInsert.sql"})
public class TableGroupAcceptanceTest extends ApplicationTest {

    private OrderTable orderTable1 = new OrderTable();
    private OrderTable orderTable2 = new OrderTable();
    private TableGroup tableGroup = new TableGroup();

    @BeforeEach
    void setUp() {
        super.setUp();
        orderTable1.setEmpty(false);
        orderTable2.setEmpty(false);
        TableAcceptanceTest.주문_테이블_비우거나_채우기(orderTable1, 1L);
        TableAcceptanceTest.주문_테이블_비우거나_채우기(orderTable2, 2L);
    }

    @DisplayName("단체 그룹 추가에 성공하면 200 응답을 받는다.")
    @Test
    void createTableGroup() {

        //given
        단체추가_사전준비();

        //when
        ExtractableResponse<Response> response = 단체_추가(tableGroup);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    @DisplayName("단체테이블 목록의 크기가 1이고, 단체 그룹 추가를 시도하면 500 응답을 받는다.")
    @Test
    void createTableGroupFail1() {

        //given
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(2);
        orderTable1.setEmpty(true);

        TableAcceptanceTest.주문_테이블_비우거나_채우기(orderTable1, 1L);

        orderTable1 = new OrderTable();
        orderTable1.setId(1L);

        tableGroup.setOrderTables(Arrays.asList(orderTable1));

        //when
        ExtractableResponse<Response> response = 단체_추가(tableGroup);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("존재하지 않는 테이블로, 단체 그룹 추가를 시도하면 500 응답을 받는다.")
    @Test
    void createTableGroupFail2() {

        //given
        단체추가_사전준비();

        //when
        OrderTable wrongOrderTable = new OrderTable();
        wrongOrderTable.setId(1000L);

        tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, wrongOrderTable));

        ExtractableResponse<Response> response = 단체_추가(tableGroup);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("비어있지 않는 테이블로, 단체 그룹 추가를 시도하면 500 응답을 받는다.")
    @Test
    void createTableGroupFail3() {

        //given
        비어있지않는_테이블_단체추가_사전준비();

        //when
        ExtractableResponse<Response> response = 단체_추가(tableGroup);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("중복된 단체 그룹을 시도하면 500 응답을 받는다..")
    @Test
    void createTableGroupFail4() {

        //given
        단체추가_사전준비();
        단체_추가(tableGroup);

        //when
        ExtractableResponse<Response> response = 단체_추가(tableGroup);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("단체 해제에 성공하면 204 응답을 받는다.")
    @Test
    void deleteTableGroup() {

        단체추가_사전준비();
        단체_추가(tableGroup);

        ExtractableResponse<Response> response = 단쳬_해제(1L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 단체추가_사전준비() {
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(2);
        orderTable1.setEmpty(true);

        orderTable2.setId(2L);
        orderTable2.setNumberOfGuests(2);
        orderTable2.setEmpty(true);

        TableAcceptanceTest.주문_테이블_비우거나_채우기(orderTable1, 1L);
        TableAcceptanceTest.주문_테이블_비우거나_채우기(orderTable2, 2L);

        orderTable1 = new OrderTable();
        orderTable2 = new OrderTable();

        orderTable1.setId(1L);
        orderTable2.setId(2L);

        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
    }

    private void 비어있지않는_테이블_단체추가_사전준비() {
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(2);
        orderTable1.setEmpty(false);

        orderTable2.setId(2L);
        orderTable2.setNumberOfGuests(2);
        orderTable2.setEmpty(false);

        TableAcceptanceTest.주문_테이블_비우거나_채우기(orderTable1, 1L);
        TableAcceptanceTest.주문_테이블_비우거나_채우기(orderTable2, 2L);

        orderTable1 = new OrderTable();
        orderTable2 = new OrderTable();

        orderTable1.setId(1L);
        orderTable2.setId(2L);

        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
    }

    private ExtractableResponse<Response> 단체_추가(final TableGroup tableGroup) {
        return RestAssured
            .given().log().all()
            .body(tableGroup)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/api/table-groups")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 단쳬_해제(final Long tableGroupId) {
        return RestAssured
            .given().log().all()
            .delete(String.format("/api/table-groups/%s", tableGroupId))
            .then().log().all()
            .extract();
    }
}
