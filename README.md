# Maplink
Technical Code of Maplink:

### How to compile application?
`mvn clean install`

### How to run the application?
`mvn spring-boot:run`

### Input
The villain address, location or coordinate.

### Sample:

* GET localhost:8080/maplink/address/Midtown West, Nova York, NY 10018, EUA
* GET localhost:8080/maplink/location/Times Square
* GET localhost:8080/maplink/coordinate/40,758882/-73,979141 `using "," instead of "."`

### Output:
The location of the villain and the information about the locations that may be under attack.
