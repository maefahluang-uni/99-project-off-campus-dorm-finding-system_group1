package th.mfu.domain;
//For the serach buttons in the homepage as well as more efficient and less error prone to make buttons
public enum City {
    CHIANG_RAI("Chiang Rai"),
    CHIANG_MAI("Chiang Mai"),
    BANGKOK("Bangkok"),
    PHUKET("Phuket"),
    SALAYA("Salaya"),
    PATTAYA("Pattaya"),
    HAT_YAI("Hat Yai");

    private final String city;

    City(String locationName) {
        this.city = locationName;
    }

    public String getCity() {
        return city;
    }
}
