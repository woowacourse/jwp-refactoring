package kitchenpos.order.presentation;

import io.restassured.RestAssured;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.order.application.OrderService;
import kitchenpos.common.controller.ControllerTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import kitchenpos.order.presentation.dto.OrderCreateRequest;
import kitchenpos.order.presentation.dto.OrderStatusChangeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class OrderRestControllerTest extends ControllerTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void Order를_생성하면_201을_반환한다() {
        // given
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 0, false));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("후라이드"));
        final Menu menu = menuRepository.save(new Menu("디노공룡메뉴", new MenuPrice(new BigDecimal(17000)), menuGroup.getId()));
        final var 요청_준비 = RestAssured.given()
                .body(new OrderCreateRequest(List.of(menu.getId()), List.of(2), orderTable.getId()))
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .post("/api/orders");

        // then
        응답.then().assertThat().statusCode(CREATED.value());
    }

    @Test
    void Order를_조회하면_200을_반환한다() {
        // given
        final var 요청_준비 = RestAssured.given()
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .get("/api/orders");

        // then
        응답.then().assertThat().statusCode(OK.value());
    }

    @Test
    void 주문상태를_변경하면_200을_반환한다() {
        //given
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 0, false));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("후라이드"));
        final Menu menu = menuRepository.save(new Menu("디노공룡메뉴", new MenuPrice(new BigDecimal(17000)), menuGroup.getId()));
        final Long orderId = orderService.create(List.of(menu.getId()), List.of(2), orderTable.getId());

        final var 요청_준비 = RestAssured.given()
                .body(new OrderStatusChangeRequest(OrderStatus.COMPLETION.name()))
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .put("/api/orders/" + orderId + "/order-status");

        // then
        응답.then().assertThat().statusCode(OK.value());
    }
}
