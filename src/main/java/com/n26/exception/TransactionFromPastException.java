package com.n26.exception;

import com.n26.domain.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class TransactionFromPastException extends RuntimeException {
  public TransactionFromPastException(Transaction transaction) {
    super("Found transaction from past: " + transaction);
  }
}
