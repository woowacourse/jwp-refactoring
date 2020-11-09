package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kitchenpos.ui.dto.jsonparser.PriceDeserializer;
import kitchenpos.ui.dto.jsonparser.PriceSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@JsonSerialize(using = PriceSerializer.class)
@JsonDeserialize(using = PriceDeserializer.class)
@EqualsAndHashCode
@Getter
public class Price implements Comparable<Price> {
    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal value;

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || BigDecimal.ZERO.compareTo(price) > 0) {
            throw new IllegalArgumentException("금액이 유효하지 않습니다.");
        }
    }

    public Price add(final Price operand) {
        return new Price(this.value.add(operand.value));
    }

    public Price multiplesOf(final long quantity) {
        return new Price(this.value.multiply(BigDecimal.valueOf(quantity)));
    }

    @Override
    public int compareTo(final Price o) {
        return this.value.compareTo(o.value);
    }
}
