.PHONY: all common-logging order-service inventory-service

all: common-logging order-service inventory-service

common-logging:
	@cd common-logging && ./gradlew build

order-service:
	@cd order-service && ./gradlew build

inventory-service:
	@cd inventory-service && ./gradlew build
