package com.n26.controller.rest;

import com.n26.dto.TransactionDto;
import com.n26.mapper.TransactionDtoEntityMapper;
import com.n26.service.TransactionService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
  private final TransactionService transactionService;
  private final TransactionDtoEntityMapper transactionDtoEntityMapper;

  @PostMapping
  public ResponseEntity<Object> addTransaction(
      @RequestBody @NotNull @Valid final TransactionDto transaction) {
    transactionService.addTransaction(transactionDtoEntityMapper.toEntity(transaction));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping
  public ResponseEntity<Object> deleteAllTransactions() {
    transactionService.deleteTransactions();
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
