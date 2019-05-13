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

利用docker compose启动容器
```bash
docker-compose -f docker_user.yml up -d
```

停止容器
```bash
docker stop msj-user
```

移除容器
```bash
docker rm msj-user
```

移除旧的镜像
```bash
docker rmi 旧镜像的IMAGE ID
```