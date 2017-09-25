### Spring Boot and Mockito double-proxying problem

This repository contains a minimal reproduction of a double-proxying problem
with Spring Framework and Mockito. To work around this problem Spring Boot is
currently relying upon Mockito's internals, something which we'd like to
change.

To reproduce the failure run `./mvnw test`. Two tests will run. One should
pass and one should fail.