# SifriTaub • Assignment 1

## Authors
* David Deres
* Adan Abo Hyeah

## Notes

### Implementation Summary
Short summary of your implementation, including data structures used, design choices made, and
a short tour of the class hierarchy you created.

We started off by implementing the DataBase class, its responsible for working with databases and implements open,write and read functions.
Then we moved on to implementing the tokens managment - in a class called TokensManager which is responsible for creating & validating tokens;
it creates two DataBases one is used to store mappings of token -> "V"\"X" representing valid\invalid tokens, while the other stores username -> token mappings.

after that we implemented the IDs manager which stores all the ids in a single entry, the reason behind doing this is for convinent support of deletion operations
for future assignments.

SifriTaub creates three Databases called "users","books" & "auth" which are responsible for storing data regarding users,books and tokens
which are used and updated in SifriTaub's required functions.
it uses a tokens manager and IDs manager

### Testing Summary
Short summary describing the ways you chose to test your code.

- Unit tests for the primitive storage stub we created, in order to assure the reliability of the lowest layer.
  - in those tests we've checked that the assumptions given to us in the assignment really hold (like persistence)
- Unit tests for the SifriTaub App - first, all the trivial tests in order to check that our methods work properly.
  this includes a check for all the exceptions expected to be thrown by actions that are described in the documentation.
  Next, we thought about some corner cases and also used questions asked in Piazza to test that our code covers those cases as well.
  And lastly, we implemented some integration tests that span several methods to check for the proper interaction between different methods in the class.

### Difficulties
Please list any technological difficulties you had while working on this assignment, especially
with the tools used: Kotlin, JUnit, MockK, Guice, and Gradle.

- It took time to understand how to properly use the modules with Guice.
- Deciding about how to implement the token factory was difficult, since we want consistency, reliability (tokens do not repeat themselves),
  and also security (although not part of the requirements) - but also efficient run time.
  Also, we wanted the Tokens Manager to be independent of the Tokens Factory,
  so we couldn't hold local data about the token in the manager in order to produce new tokens effectively.

### Feedback
Put any feedback you may have for this assignment here. This **will** be read by the course staff,
and may influence future assignments!
