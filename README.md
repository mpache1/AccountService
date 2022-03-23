# account-service

A basic Banking-Account Service

- Create a Checking-Account, Saving-Account or Priavte-Loan-Account
    - In order to create a Saving-Account an existing associated Checking-Account is needed
  

- If needed every Account can be locked or unlocked
    - locked Accounts are read-only, so deposit and transfer are not possible


- From every Account the current Balance and Account-History can be requested


- All existing Accounts can be requested and filtered by Type (checking, saving, privateloan)


- It is possible to deposit money to every Account


- It is possible to transfer money from one Account to another
    - Private-Loan-Accounts can only receive money
    - Checking-Accounts can transfer and receive money without restrictions
    - Saving-Accounts can only transfer money to their associated Accounts
    

### Codeowner
- [@mpache1](https://github.com/mpache1)


### Requirements
 - Java 17.*
 - Maven 3.*

### Build
 - mvn clean install

### Rest Endpoints
**POST** : `/v1/create/checking`
```
curl -H "Content-Type: application/json" -X POST --data '{"iban":"DE90123456781234567891"}' "http://localhost:8080/v1/create/checking"
```

**POST** : `/v1/create/saving`
```
curl -H "Content-Type: application/json" -X POST --data '{"iban":"DE90123456781234567892","associatedIban":"DE90123456781234567891"}' "http://localhost:8080/v1/create/saving"
```

**POST** : `/v1/create/privateloan`
```
curl -H "Content-Type: application/json" -X POST --data '{"iban":"DE90123456781234567891"}' "http://localhost:8080/v1/create/privateloan"
```

**GET** : `/v1/accounts/`
```
curl -H "Content-Type: application/json" -X GET "http://localhost:8080/v1/accounts?type=checking&type=saving&type=privateloan"
```

**GET** : `/v1/{iban}/balance`
```
curl -H "Content-Type: application/json" -X GET "http://localhost:8080/v1/DE90123456781234567891/balance"
```

**GET** : `/v1/{iban}/history`
```
curl -H "Content-Type: application/json" -X GET "http://localhost:8080/v1/DE90123456781234567891/history"
```

**PUT** : `/v1/lock`
```
curl -H "Content-Type: application/json" -X PUT --data '{"iban":"DE90123456781234567891"}' "http://localhost:8080/v1/lock"
```

**PUT** : `/v1/unlock`
```
curl -H "Content-Type: application/json" -X PUT --data '{"iban":"DE90123456781234567891"}' "http://localhost:8080/v1/unlock"
```

**PUT** : `/v1/deposit`
```
curl -H "Content-Type: application/json" -X PUT --data '{"iban":"DE90123456781234567891", "amount":"100.00"}' "http://localhost:8080/v1/deposit"
```

**PUT** : `/v1/transfer`
```
curl -H "Content-Type: application/json" -X PUT --data '{"iban":"DE90123456781234567891","receivingIban":"DE90123456781234567892","amount":"100.00"}' "http://localhost:8080/v1/transfer"
```

### Runs github-actions on push
- Java CI with Maven
- CodeQL
