# Meetroom

Meetroom is a testing project for booking meeting room inside a company.

## System requirements

- Java 8
- PostgreSQL
- Maven

## Using the application

To use the app, you must complete the following steps

### Create the Database

To create a new database, run the following commands at the postgres prompt:

   `$ sudo -u postgres psql`
   
   `postgres=# create database meetroom;`
    
   `postgres=# create user admin with encrypted password 'qwerty';`
    
   `postgres=# grant all privileges on database meetroom to admin;`

Then you need to restore a dump of the database (run command in meetroom directory):

   `$ sudo -u postgres psql -d meetroom -f db/meetroom_dump`

### Run application:

- Build package with maven:

    `$ ./mvnw clean package`

- Run the jar file:

    `$ java -jar target/meetroom-0.0.1-SNAPSHOT.jar`

### Usage

Open link in a browser: http://localhost:8080/.
You need to log in (for example, username: 'first', password: '1234').
On the booking page you can reserve a meeting room at the interest date and time.
All events shown on the booking page.
To create an event you need click a date in the header table and on the opened page insert  a start time, duration, title and description.
If you want to see a title and description you must hover the mouse cursor over the title on the event card.
If you want to see a full detail you need click on the event card.

### Сonstraints

- It is assumed that the meeting room is one.
- Event duration can be 00:30 - 23:59.
- Delete an event can only owner.
- Events can't overlap.





