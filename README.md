JavaTests
=========

My Java test code

## Maven command to start the project

mvn archetype:generate -DgroupId=eu.ill.tests -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

```
.
├── my-app
│   ├── pom.xml
│   └── src
│       ├── main
│       │   └── java
│       │       └── eu
│       │           └── ill
│       │               └── tests
│       │                   └── App.java
│       └── test
│           └── java
│               └── eu
│                   └── ill
│                       └── tests
│                           └── AppTest.java
└── README.md
```

## Compile 
mvn package

## Run

```
cd my-app
java -cp target/my-app-1.0-SNAPSHOT.jar eu.ill.tests.App
```

Open in the browser:
``` 
http://localhost:8000/test
```

