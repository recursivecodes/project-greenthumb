## Project GreenThumb

Read about Project GreenThumb:

* https://blogs.oracle.com/developers/project-greenthumb-part-1-automating-monitoring-seedling-growth-with-microcontrollers-the-cloud
* https://blogs.oracle.com/developers/project-greenthumb-part-2-the-data-collection
* https://blogs.oracle.com/developers/project-greenthumb-part-3-consuming-and-persisting-the-sensor-data-in-the-cloud
* https://blogs.oracle.com/developers/project-greenthumb-part-4-reporting-queries-and-websockets
* https://blogs.oracle.com/developers/project-greenthumb-part-5-the-front-end,-build-pipeline,-push-notifications-and-overall-progress

### Build & Deploy

Build

```shell
$ ./gradlew assemble && docker build -t phx.ocir.io/toddrsharp/greenthumb/greenthumb-client:latest . && docker push phx.ocir.io/toddrsharp/greenthumb/greenthumb-client:latest
```

```shell
$ docker build -t phx.ocir.io/toddrsharp/insulin-helper/greenthumb-client:latest . 
```

Push

```shell
$ docker push phx.ocir.io/toddrsharp/insulin-helper/greenthumb-client:latest                                         
```
