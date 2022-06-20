DOCKER_IMG="login-service:$(if ${IMG_VERSION},${IMG_VERSION},testing)"
CONTAINER_PORT:=$(if ${CONTAINER_PORT},${CONTAINER_PORT},8080)
HOST_PORT:=$(if ${HOST_PORT},${HOST_PORT},${CONTAINER_PORT})


ENV=$(if ${ENV_FILE},${ENV_FILE},test.env)

run-img-env: build-img
	docker run --env-file ${ENV} -p ${HOST_PORT}:${CONTAINER_PORT} --rm $(DOCKER_IMG)

build-img:
	docker build \
		-t $(DOCKER_IMG) \
		-f Dockerfile .
remove-img:
	docker image rm -f $(DOCKER_IMG)

.PHONY: build-img  remove-img
