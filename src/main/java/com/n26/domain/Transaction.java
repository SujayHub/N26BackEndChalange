package com.n26.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

  @NotNull private BigDecimal amount;

  @NotNull private ZonedDateTime timestampInUtc;
}
