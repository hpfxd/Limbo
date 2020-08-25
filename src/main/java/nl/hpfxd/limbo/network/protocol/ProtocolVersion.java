package nl.hpfxd.limbo.network.protocol;

import java.util.Arrays;
import java.util.List;

public class ProtocolVersion {
    public static final List<Integer> supportedVersions = Arrays.asList(
            ProtocolVersion.PROTOCOL_1_7_10,
            ProtocolVersion.PROTOCOL_1_8
    );

    public static final int PROTOCOL_1_7_10 = 5;
    public static final int PROTOCOL_1_8 = 47;
    public static final int PROTOCOL_1_12_2 = 340;
}
