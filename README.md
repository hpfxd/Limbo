# Limbo
A simple and lightweight Minecraft limbo server.  
Usually used in a network of servers to host a large amount of players at a low CPU cost. (Queue/fallback server)

**Currently implemented:**
- Spawning in a void world
- Server list ping

**To Do**
- World loading from a schematic file. (shouldn't be too difficult for 1.8.x)
- Bungeecord IP forwarding (wouldn't really do much)
- Support more versions

## Minecraft Protocol Versions Supported
- 5 (1.7.6-1.7.10)
- 47 (1.8.x) (Base version)

1.12.2 support was attempted, but when sending the join game packet my client would disconnect and I haven't been able to fix it.  
PRs to support more versions are welcome.  
You may also be able to run ViaVersion on your proxy to support newer versions, I haven't tested this though.

## Usage
Just download the latest release and run it with Java, a configuration file will be created and you may edit it as you wish.  
Note: To run the server with a specific port without editing the configuration file (useful for deploying on a cloud system) just set the first argument to a port. Example: `java -jar Limbo.jar 25566` will run the server on port 25566.  
To compile it yourself just run `mvn package`.

### Native Builds
Making native builds with [GraalVM](https://www.graalvm.org/) is supported and recommended.  
Native builds come with a much faster startup time (from 2 seconds to 3ms for me), as well as less overall CPU usage.

Just install GraalVM and the `native-image` component and run `native-image -jar <limbo jar file>.jar` and you can run the server using `./Limbo`.
