# stackoverflow-63030967

1. There is only one secured url `http://localhost:8080/secured` and uses in-memory map to keep track of registered users.
2. Signup is very simple as it is not the puprose of this demo, it is a `GET http://localhost:8080/sign-up/{newUserName}`. 
    - A new user with username `newUserName` will be created with `password` as password and role `USER` added to the map of registered users.
    - I think only this point matters for your question. When logging in normally `AuthenticationWebFilter` will call 
     `securityContextRepository.save(exchange, securityContext)` to save the context. But I have passed all the filters and I am inside the controller
     method for sign-up. So I just replicate that behaviour.
     
3. After `GET http://localhost:8080/sign-up/{newUserName}`, user can go to `http://localhost:8080/secured` and it will work as if the user logged in using sign-up url
