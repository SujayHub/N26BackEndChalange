package com.n26.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistics {

  @JsonSerialize(using = ToStringSerializer.class)
  private BigDecimal avg;

  @JsonSerialize(using = ToStringSerializer.class)
  private BigDecimal min;

  @JsonSerialize(using = ToStringSerializer.class)
  private BigDecimal max;

  private long count;

  @JsonSerialize(using = ToStringSerializer.class)
  private BigDecimal sum;

  public Statistics withScale(int scale, RoundingMode roundingMode) {
    return new Statistics(
        avg.setScale(scale, roundingMode),
        min.setScale(scale, roundingMode),
        max.setScale(scale, roundingMode),
        count,
        sum.setScale(scale, roundingMode));
  }
}
