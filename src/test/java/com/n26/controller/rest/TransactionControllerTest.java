package com.n26.controller.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.domain.Transaction;
import com.n26.dto.TransactionDto;
import com.n26.exception.TransactionFromFutureException;
import com.n26.exception.TransactionFromPastException;
import com.n26.mapper.TransactionDtoEntityMapper;
import com.n26.service.TransactionService;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

  private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
  private static final DateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat(TIMESTAMP_FORMAT);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Autowired private MockMvc mockMvc;

  @MockBean private TransactionService transactionService;
  @MockBean private TransactionDtoEntityMapper transactionDtoEntityMapper;

  @Test
  public void addTransactionShouldReturnStatusCreatedWhenTransactionTimeStampIsWithIn60SecRange()
      throws Exception {
    final String amount = "12.3343";
    final String date = "2018-07-17T09:59:51.312Z";
    final ZonedDateTime timeStamp = ZonedDateTime.ofInstant(Instant.parse(date), ZoneId.of("UTC"));

    doNothing().when(transactionService).addTransaction(any(Transaction.class));
    when(transactionDtoEntityMapper.toEntity(any(TransactionDto.class)))
        .thenReturn(getTransactionObject(new BigDecimal(amount), timeStamp));
    this.mockMvc
        .perform(
            post("/transactions")
                .contentType(APPLICATION_JSON_UTF8)
                .content(getValidTransactionRequestJsonBody(amount, date)))
        .andDo(print())
        .andExpect(status().isCreated());
    verify(transactionDtoEntityMapper, atLeastOnce()).toEntity(any(TransactionDto.class));
    verify(transactionService, atLeastOnce()).addTransaction(any(Transaction.class));
  }

  @Test
  public void addTransactionShouldReturnNoContentWhenTransactionTimeStampIsOlderThan60Sec()
      throws Exception {
    final String amount = "12.3343";
    final String date = "2018-07-17T09:59:51.312Z";
    final ZonedDateTime timeStamp = ZonedDateTime.ofInstant(Instant.parse(date), ZoneId.of("UTC"));

    doThrow(
            new TransactionFromPastException(
                getTransactionObject(new BigDecimal(amount), timeStamp)))
        .when(transactionService)
        .addTransaction(any(Transaction.class));
    when(transactionDtoEntityMapper.toEntity(any(TransactionDto.class)))
        .thenReturn(getTransactionObject(new BigDecimal(amount), timeStamp));
    this.mockMvc
        .perform(
            post("/transactions")
                .contentType(APPLICATION_JSON_UTF8)
                .content(getValidTransactionRequestJsonBody(amount, date)))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(transactionDtoEntityMapper, atLeastOnce()).toEntity(any(TransactionDto.class));
    verify(transactionService, atLeastOnce()).addTransaction(any(Transaction.class));
  }

  @Test
  public void addTransactionShouldReturnUnprocessableEntityWhenTransactionTimeStampIs60SecInFuture()
      throws Exception {
    final String amount = "12.3343";
    final String date = "2018-07-17T09:59:51.312Z";
    final ZonedDateTime timeStamp = ZonedDateTime.ofInstant(Instant.parse(date), ZoneId.of("UTC"));

    doThrow(
            new TransactionFromFutureException(
                getTransactionObject(new BigDecimal(amount), timeStamp)))
        .when(transactionService)
        .addTransaction(any(Transaction.class));
    when(transactionDtoEntityMapper.toEntity(any(TransactionDto.class)))
        .thenReturn(getTransactionObject(new BigDecimal(amount), timeStamp));
    this.mockMvc
        .perform(
            post("/transactions")
                .contentType(APPLICATION_JSON_UTF8)
                .content(getValidTransactionRequestJsonBody(amount, date)))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());

    verify(transactionDtoEntityMapper, atLeastOnce()).toEntity(any(TransactionDto.class));
    verify(transactionService, atLeastOnce()).addTransaction(any(Transaction.class));
  }

  @Test
  public void addTransactionShouldReturnUnprocessableEntityWhenBodyIsNotParsable()
      throws Exception {
    this.mockMvc
        .perform(
            post("/transactions")
                .contentType(APPLICATION_JSON_UTF8)
                .content(nonParsableTransactionRequestBody()))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());
    verifyNoMoreInteractions(transactionDtoEntityMapper);
    verifyNoMoreInteractions(transactionService);
  }

  @Test
  public void addTransactionShouldReturnBadRequestWhenRequestBodyHasInvalidJson() throws Exception {
    this.mockMvc
        .perform(
            post("/transactions").contentType(APPLICATION_JSON_UTF8).content("Invalid json body"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    verifyNoMoreInteractions(transactionDtoEntityMapper);
    verifyNoMoreInteractions(transactionService);
  }

  @Test
  public void deleteAllTransactionsShouldReturnNoContent() throws Exception {
    doNothing().when(transactionService).deleteTransactions();
    this.mockMvc
        .perform(delete("/transactions").contentType(APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());
  }

  private static Transaction getTransactionObject(BigDecimal amount, ZonedDateTime zonedDateTime) {
    return Transaction.builder().amount(amount).timestampInUtc(zonedDateTime).build();
  }

  private static String getValidTransactionRequestJsonBody(String amount, String timeStamp)
      throws JsonProcessingException, ParseException {
    TransactionDto transactionDto =
        TransactionDto.builder()
            .amount(amount)
            .timestamp(TIMESTAMP_FORMATTER.parse(timeStamp))
            .build();
    return OBJECT_MAPPER.writeValueAsString(transactionDto);
  }

  private static String nonParsableTransactionRequestBody() {
    return "{\n"
        + "    \"timestamp\": \"4/23/2018 11:32 PM\",\n"
        + "    \"amount\": \"262.01\"\n"
        + "}";
  }
}
