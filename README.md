# cash-register-system

## Description
Cash register system. Cashier can open a check add products found
by code (parsley = 234, bread = 222) or by title, specify quantity of
the products or weight and close the check. Senior cashier is capable
to reject a check, remove one product from a check and refund the money.
Make X and Z reports. Commodities expert is responsible for products,
create and specify quantity in the stock.


## Installation and running
#### Prerequisites
- JDK and JRE 8 or later [Download](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- PostgreSQL 9 or later [Download](https://www.postgresql.org/download/)
- Apache Maven [Download](https://maven.apache.org/download.cgi)
- Apache Tomcat [Download](https://tomcat.apache.org/download-90.cgi)

#### Set up and run
- Clone or download the project and build `.war` by executing `mvn clean package -DskipTests`
from the root.
- Create database `cash_register`, in the `database.properties` 
specify values for `root.user` and `root.password`. It must be either
root user or those who is allowed to create tables and roles
- Modify `schema.sql` and `init.sql` if it does not fit the requirements. 
Depending on your needs you can choose what to establish by setting poroperties 
`schema` and `init` to false or true in `database.properties` file.
- By default role `cash_register_admin` will be created in database and 
used to access `cash_register` database. It is configurable by changing
properties `user` and `password` in the `database.properties` and modifying
creation of the role in `schema.sql`
- Having everything set up deploy `.war` to Tomcat

##### Tests running
- Create database `cash_register_test`
- Check the presence of the `cash_register_admin`
- Run tests by command `mvn test` from the root of the project
