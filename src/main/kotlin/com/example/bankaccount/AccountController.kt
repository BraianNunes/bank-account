package com.example.bankaccount

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException

@RestController
@RequestMapping("accounts")
class AccountController(val repository: AccountRepository) {

    @PostMapping
    fun create(@RequestBody account: Account) = ResponseEntity.ok(repository.save(account))

    @GetMapping
    fun read() = ResponseEntity.ok(repository.findAll())

    @PutMapping("{document}")
    fun update(@PathVariable document: String, @RequestBody account: Account): ResponseEntity<Account> {
        val accountDbOptional = repository.findByDocument(document)
        val accountDb = accountDbOptional
                .orElseThrow { RuntimeException("Account document: $document not found!") }
                .copy(name = account.name, balance = account.balance)
        val saved = repository.save(accountDb)
        return ResponseEntity.ok(saved)
    }

    @DeleteMapping("{document}")
    fun delete(@PathVariable document: String) = repository
            .findByDocument(document)
            .ifPresent { repository.delete(it) }

}