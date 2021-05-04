package com.n26.controller.rest;

import com.n26.domain.Transaction;
import com.n26.dto.TransactionDto;
import com.n26.mapper.TransactionDtoEntityMapper;
import com.n26.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
  private final TransactionService transactionService;
  private final TransactionDtoEntityMapper transactionDtoEntityMapper;

  @PostMapping
  public ResponseEntity<?> addTransaction(
      @RequestBody @NotNull @Valid final TransactionDto transaction) {
    boolean success =
        transactionService.addTransaction(transactionDtoEntityMapper.toEntity(transaction));
    if (success) {
      return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteMapping
  public ResponseEntity<?> deleteAllTransactions() {
    transactionService.deleteTransactions();
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
