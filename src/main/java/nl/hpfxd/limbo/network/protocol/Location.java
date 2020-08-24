package nl.hpfxd.limbo.network.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Location {
    private final int x;
    private final int y;
    private final int z;
}
