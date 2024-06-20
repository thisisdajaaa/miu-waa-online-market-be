#### Authentication and Authorization

- The following files have been added for the use in security

```
  <!-- Spring Security dependencies -->
		<!-- JJWT dependencies -->
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-api</artifactId>
			<version>0.11.5</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-impl</artifactId>
			<version>0.11.5</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId>
			<version>0.11.5</version>
		</dependency>

		<!-- Spring Security dependencies -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>

		<!-- Testing dependencies -->
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.junit.platform</groupId>
			<artifactId>junit-platform-launcher</artifactId>
			<scope>test</scope>
		</dependency>
```

- The following properties have been added to the application.properties file

```
security.jwt.secret-key=MCgCIQC9Q3dQcZMbts0vEMR/1dyZQGncUHiswbQjB3FsXgONPwIDAQAB
security.jwt.expiration-time=36000

[//]: # (security.jwt.token-prefix=Bearer)

[//]: # (security.jwt.header-string=Authorization)
```

- Please if by mistake i add this line to the version control, please remove it. I have commented it out because it is
  not needed for the project

```
#spring.datasource.url=jdbc:postgresql://localhost:5432/online-market
#this is for businge database ignore and use the above one
#spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
```

- Upon registration, the user is given a token that is used to authenticate the user. The token is valid for 10 hours.
  The token is used to access the endpoints that require authentication.
- The following endpoints are not secured
- The following are initially returning json objects

```
  @PostMapping("/api/v1/auth/register")
  
  {
    "id": 1,
    "username": "a@gmail.com",
    "password": "$2a$10$uz2jZ8mbg2Ht1WFrfog5ou.8on5s6t4CJTgirbuSNXwu3hxvUT0Ye",
    "email": "a@gmail.com",
    # every user automatically is assigned the role of a buyer but can be updated by nthe admin
    "role": "BUYER",
    # generated by the system
    "createdAt": "2024-06-18T22:25:07.532+00:00",
    "updatedAt": "2024-06-18T22:25:07.532+00:00",
    # authorities are the roles that the user has
    "authorities": [
        {
            "authority": "BUYER"
        }
    ],
    # the user is enabled
    "enabled": true,
    # the following are spring security properties managed internally
    "accountNonExpired": true,
    "credentialsNonExpired": true,
    "accountNonLocked": true
}

  @PostMapping("/api/v1/auth/login")
  
  Success! Please check your email to verify account 
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjpbeyJhdXRob3JpdHkiOiJCVVlFUiJ9XSwic3ViIjoiYUBnbWFpbC5jb20iLCJpYXQiOjE3MTg3NDk5MjAsImV4cCI6MTcxODc0OTk1Nn0.hvTjeIny7uHtVFx4Ter06sWFFJG-WSq2zCOWfLeTRCw
 and refresh token
 eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjpbeyJhdXRob3JpdHkiOiJCVVlFUiJ9XSwic3ViIjoiYUBnbWFpbC5jb20iLCJpYXQiOjE3MTg3NDk5MjAsImV4cCI6MTcxODc0OTk1Nn0.hvTjeIny7uHtVFx4Ter06sWFFJG-WSq2zCOWfLeTRCw
```

- The above end points were updated to carry there tokens in cookies. But the code was left as comments but also need keeping track of bearer and authorization headers

- The login has been updated to attach token and refresh token to the response. The token is used to access the endpoints
  that require authentication. The refresh token is used to refresh the token when it expires. The token is valid for 10
  hours.
```
// set accessToken to cookie header
        String accessToken = authService.login(user).get("accessToken");
        String refreshToken = authService.login(user).get("refreshToken");
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .maxAge(accessExpiration)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
```

- The end point /api/v1/users/showMe will return a body containing all the users details and claims
```
{
    "authorities": [
        {
            "authority": "BUYER"
        }
    ],
    "details": null,
    "authenticated": true,
    "principal": {
        "id": 2,
        "username": "b@gmail.com",
        "password": "$2a$10$59iSnUzIW96DSmCKxNrohuo7IuvVGKoJ9ZfXQ2ZKBXRXL0M6mRbTK",
        "email": "b@gmail.com",
        "role": "BUYER",
        "createdAt": "2024-06-19T04:56:38.013+00:00",
        "updatedAt": "2024-06-19T04:56:38.013+00:00",
        "authorities": [
            {
                "authority": "BUYER"
            }
        ],
        "enabled": true,
        "accountNonLocked": true,
        "accountNonExpired": true,
        "credentialsNonExpired": true
    },
    "credentials": null,
    "name": "b@gmail.com"
}
```

- To ensure security of the variables like passwords and secrets
- For now not ignored so as all developers access the details
```gitignore
#secret.env
```
- The AuthenticateUserAspect might be useful for logging in the future
- In order to use @PreAuthorize, this has to be added to the configuration class
```
@EnableMethodSecurity
```

- For issues arising due to use of abstract classes please refer to the git document below
```
https://github.com/FasterXML/jackson-docs/wiki/JacksonPolymorphicDeserialization
```
- Added a docker compose dependency to the project and moved the docker compose file to root for faster access