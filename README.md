[![Actions Status](https://github.com/mbode/flink-prometheus-example/workflows/Gradle/badge.svg)](https://github.com/mbode/flink-prometheus-example/actions)
[![Docker Hub](https://img.shields.io/docker/cloud/build/maximilianbode/flink-prometheus-example.svg)](https://hub.docker.com/r/maximilianbode/flink-prometheus-example)
[![codecov](https://codecov.io/gh/mbode/flink-prometheus-example/branch/master/graph/badge.svg)](https://codecov.io/gh/mbode/flink-prometheus-example)
[![Flink v1.14.2](https://img.shields.io/badge/flink-v1.14.2-blue.svg)](https://github.com/apache/flink/releases/tag/release-1.14.2)
[![Prometheus v2.32.1](https://img.shields.io/badge/prometheus-v2.32.1-blue.svg)](https://github.com/prometheus/prometheus/releases/tag/v2.32.1)

## Getting Started

### Startup
```
./gradlew composeUp
```

### Web UIs
(When using [docker-machine](https://docs.docker.com/machine/), substitute your `docker-machine ip` for _localhost_ in the URLs.)
- [Flink JobManager](http://localhost:8081/#/overview)
- [Prometheus](http://localhost:9090/graph)
- [Grafana](http://localhost:3000) (credentials _admin:flink_)
- Prometheus endpoints
    - [Job Manager](http://localhost:9249/metrics)
    - [Task Manager 1](http://localhost:9250/metrics)
    - [Task Manager 2](http://localhost:9251/metrics)

## Built With

- [Apache Flink](https://flink.apache.org)
- [Prometheus](https://prometheus.io)
- [Grafana](https://grafana.com)
- [docker-compose](https://docs.docker.com/compose/) – provisioning of the test environment
- [Gradle](https://gradle.org) with [kotlin-dsl](https://github.com/gradle/kotlin-dsl)
    - [gradle-testsets-plugin](https://github.com/unbroken-dome/gradle-testsets-plugin)
    - [shadow](https://github.com/johnrengelman/shadow)
    - [spotless](https://github.com/diffplug/spotless/tree/master/plugin-gradle)
    - [spotbugs](https://github.com/spotbugs/spotbugs-gradle-plugin)
    - [gradle-docker-compose-plugin](https://github.com/avast/gradle-docker-compose-plugin)
    - [gradle-versions-plugin](https://github.com/ben-manes/gradle-versions-plugin)

## Development
typical tasks:
- verify: `./gradlew check`
- integration tests: `./gradlew integrationTest`
- list outdated dependenices: `./gradlew dependencyUpdates`
- update gradle: `./gradlew wrapper --gradle-version=<x.y>` (twice)