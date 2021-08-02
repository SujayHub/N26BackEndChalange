package com.n26.mapper;

import javax.validation.constraints.NotNull;

public interface DtoEntityMapper<K, T> {
  T toEntity(@NotNull final K k);

  K toDto(@NotNull final T t);
}
