package nl.hpfxd.limbo.network.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatMessagePosition {
    CHAT((byte) 0),
    SYSTEM((byte) 1),
    ACTIONBAR((byte) 2)
    ;

    private final byte id;
}
