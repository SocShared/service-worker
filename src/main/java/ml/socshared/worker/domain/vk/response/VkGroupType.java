package ml.socshared.worker.domain.vk.response;

public enum VkGroupType {
    PAGE("page"),
    GROUP("group"),
    EVENT("event");

    private VkGroupType(String type) {
        _type = type;
    }

    public static VkGroupType fromString(String text) {
        for (VkGroupType t : VkGroupType.values()) {
            if (t._type.equalsIgnoreCase(text)) {
                return t;
            }
        }
        return null;
    }
    public String type() {
        return this._type;
    }
    private String _type;
}