package me.juancarloscp52.bedrockify.common.features.salmonSizes;

public interface SalmonSized {
    int bedrockify$getSize();

    static float calculateScale(int size) {
        // Could use following formula
        // (1 << (size + 1)) / 2f
        return switch (size) {
            case 1 -> 2f;
            case 0 -> 1f;
            case -1 -> 0.5f;
            default -> throw new IllegalArgumentException("Invalid Salmon size!");
        };
    }
}
