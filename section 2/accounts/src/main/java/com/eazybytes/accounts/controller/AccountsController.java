package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.constans.AccountsConstants;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.dto.ResponseDto;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    path = "/api",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class AccountsController {
  private final CustomerRepository customerRepository;
  private final AccountsRepository accountsRepository;
  private IAccountsService iAccountsService;

  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
    iAccountsService.createAccount(customerDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
  }

  @GetMapping("/fetch")
  public ResponseEntity<CustomerDto> fetchAccount(
      @RequestParam
          @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number should be 10 digits")
          String mobileNumber) {
    CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
    return ResponseEntity.status(HttpStatus.OK).body(customerDto);
  }

  @PutMapping("/update")
  public ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody CustomerDto customerDto) {
    boolean status = iAccountsService.updateAccount(customerDto);
    return ResponseEntity.status(status ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new ResponseDto(
                status ? AccountsConstants.STATUS_200 : AccountsConstants.STATUS_500,
                status ? AccountsConstants.MESSAGE_200 : AccountsConstants.MESSAGE_500));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ResponseDto> deleteAccount(
      @RequestParam
          @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number should be 10 digits")
          String mobileNumber) {
    boolean status = iAccountsService.deleteAccount(mobileNumber);
    return ResponseEntity.status(status ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new ResponseDto(
                status ? AccountsConstants.STATUS_200 : AccountsConstants.STATUS_500,
                status ? AccountsConstants.MESSAGE_200 : AccountsConstants.MESSAGE_500));
  }
}
