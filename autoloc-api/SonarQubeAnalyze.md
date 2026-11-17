# SonarQube Analyze



## Impact Faible

* Remove redundant visibility modifiers from the methods of this test class. [java:S5786]
* Explicitly specify the time zone by passing a ZoneId or a Clock to the .now() method. [java:S8688]
* Remove this unused import. [java:S1128]
* Rename this generic name to match the regular expression '^[A-Z][0-9]?$'. [java:S119]

## Impact Moyen

* Return an empty collection instead of null. [java:S1168]
* ID is not used in the interface. [java:S2326]
* Remove this unused private method. [java:S1144]
* Replace this use of System.out by a logger. [java:S106]
* Add a private constructor to hide the implicit public one. [java:S1118]

## Impact Important

* Replace this persistent entity with a simple POJO or DTO object. [java:S4684]
* Add at least one assertion to this test case. [java:S2699]
* Revoke and change this secret, as it is compromised. [java:S6437]
* Make sure this database password gets changed and removed from the code. [secrets:S6703]