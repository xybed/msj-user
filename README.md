build executable jar
```bash
mvn -U -Pprod clean package spring-boot:repackage
```

build docker image
```bash
export version=1.0-SNAPSHOT
docker build --rm \
--build-arg version=$version \
-t msj-user:prod .
```

run docker container
```
docker run -d --name msj-user --restart=always -p 9101:9101 msj-user:prod
```

