For Task 4. part b, to allow an Array of URI's to be passed as argument I used the @Value Spring annotation.
From https://www.baeldung.com/spring-value-annotation :
"This annotation can be used for injecting values into fields in Spring-managed beans and it can be applied at the field or constructor/method parameter level."
This allowed me to set a default value for hosts when no program arguments were set while
allowing hosts to be set at runtime via --host= flag.
Example for hosts argument: 
--hosts=http://localhost:8081/quotations,http://localhost:8082/quotations,http://localhost:8083/quotations