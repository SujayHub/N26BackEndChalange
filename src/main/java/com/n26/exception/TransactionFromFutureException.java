package com.n26.exception;

import com.n26.domain.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class TransactionFromFutureException extends RuntimeException {
  public TransactionFromFutureException(Transaction transaction) {
      super("Found transaction from future: " + transaction);
  }
}
