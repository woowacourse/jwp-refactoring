package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.OrderLineItem;

@DisplayName("JdbcTemplateOrderLineItemDao 테스트")
@JdbcTest
@Sql("/dao-test.sql")
@Import(JdbcTemplateOrderLineItemDao.class)
class JdbcTemplateOrderLineItemDaoTest {
    @Autowired
    private JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;

    @DisplayName("OrderLineItemDao save 테스트")
    @Test
    void save() {
        // Given
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(3L);
        orderLineItem.setQuantity(1);

        // When
        final OrderLineItem savedOrderLineItem = jdbcTemplateOrderLineItemDao.save(orderLineItem);

        // Then
        assertAll(
                () -> assertThat(savedOrderLineItem)
                        .extracting(OrderLineItem::getSeq)
                        .isNotNull()
                ,
                () -> assertThat(savedOrderLineItem)
                        .extracting(OrderLineItem::getOrderId)
                        .isEqualTo(orderLineItem.getOrderId())
                ,
                () -> assertThat(savedOrderLineItem)
                        .extracting(OrderLineItem::getMenuId)
                        .isEqualTo(orderLineItem.getMenuId())
                ,
                () -> assertThat(savedOrderLineItem)
                        .extracting(OrderLineItem::getQuantity)
                        .isEqualTo(orderLineItem.getQuantity())
        );
    }

    @DisplayName("OrderLineItemDao findById 테스트")
    @Test
    void findById() {
        // When
        final OrderLineItem orderLineItem = jdbcTemplateOrderLineItemDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        // Then
        assertAll(
                () -> assertThat(orderLineItem)
                        .extracting(OrderLineItem::getOrderId)
                        .isEqualTo(1L)
                ,
                () -> assertThat(orderLineItem)
                        .extracting(OrderLineItem::getMenuId)
                        .isEqualTo(1L)
                ,
                () -> assertThat(orderLineItem)
                        .extracting(OrderLineItem::getQuantity)
                        .isEqualTo(1L)
        );
    }

    @DisplayName("OrderLineItemDao findById Id가 존재하지 않을 경우")
    @Test
    void findById_NotExists() {
        // When
        final Optional<OrderLineItem> orderLineItem = jdbcTemplateOrderLineItemDao.findById(3L);

        // Then
        assertThat(orderLineItem.isPresent()).isFalse();
    }

    @DisplayName("OrderLineItemDao findAll 테스트")
    @Test
    void findAll() {
        // When
        final List<OrderLineItem> orderLineItems = jdbcTemplateOrderLineItemDao.findAll();

        // Then
        assertThat(orderLineItems).hasSize(2);
    }

    @DisplayName("OrderLineItemDao findAllByOrderId 테스트")
    @Test
    void findAllByOrderId() {
        // When
        final List<OrderLineItem> allByOrderId = jdbcTemplateOrderLineItemDao.findAllByOrderId(1L);

        // Then
        assertThat(allByOrderId).hasSize(2);
    }
}
