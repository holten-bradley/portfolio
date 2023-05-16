Drone Simulation of the University of Minnesota

## Docker

Link to docker image on Dockerhub. https://hub.docker.com/repository/docker/tenzinchaksam/hw4/general

### To pull image
1) Pull image.
``` bash
docker pull tenzinchaksam/hw4:latest
```
2) Run the container.
``` bash
docker run --rm -it -p 8080:8080 tenzinchaksam/hw4
```
3) Goto [http://localhost:8080](http://localhost:8080).
4) When finished, see docker ps to get the container id so you can kill it.
``` bash
docker ps
```
``` bash
docker kill <container_id>
```

## Youtube

Click here to watch our Youtube video presentation. https://youtu.be/ZpKITahYAcg



Make sure to download docker
Pull the image
Run the image by running "docker run --rm -it -p 8080:8080 holtenbradley/drone_extension"
Go to "http://localhost:8080" on your browser to see the project running
