# Spring Batch Udemy Exercises
- These are the exercise from a Udemy course that I'm currently completing
- It's a bit different on the Batch config set up (I preferred to not do it too manually) and database selection as I
I preferred PostgreSQL to MySQL

## Software needed
- JDK 17
- Docker/Podman

## How to run

### Database:

- You can use the compose file that is included, but first you will have to create
a .env file with the env variables from the compose file with your credentials


- .env file needed variables:
``POSTGRES_USER, POSTGRES_PASSWORD``

``docker compose -up``

### Application:

I used an SpringBoot runner for executing it,
you will have to set the environment values of your database on your runner, 
with the same credentials


