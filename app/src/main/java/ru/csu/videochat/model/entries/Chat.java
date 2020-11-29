package ru.csu.videochat.model.entries;

public class Chat {
    private String uid1Token, uid2Token, category, yourAge, age17, age21, age25, age30, age31;

    public Chat() {
    }

    public Chat(String uid1Token, String uid2Token, String category, String yourAge,
                String age17, String age21, String age25, String age30, String age31) {
        this.uid1Token = uid1Token;
        this.uid2Token = uid2Token;
        this.category = category;
        this.yourAge = yourAge;
        this.age17 = age17;
        this.age21 = age21;
        this.age25 = age25;
        this.age30 = age30;
        this.age31 = age31;
    }

    public String getYourAge() {
        return yourAge;
    }

    public void setYourAge(String yourAge) {
        this.yourAge = yourAge;
    }

    public String getAge17() {
        return age17;
    }

    public void setAge17(String age17) {
        this.age17 = age17;
    }

    public String getAge21() {
        return age21;
    }

    public void setAge21(String age21) {
        this.age21 = age21;
    }

    public String getAge25() {
        return age25;
    }

    public void setAge25(String age25) {
        this.age25 = age25;
    }

    public String getAge30() {
        return age30;
    }

    public void setAge30(String age30) {
        this.age30 = age30;
    }

    public String getAge31() {
        return age31;
    }

    public void setAge31(String age31) {
        this.age31 = age31;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUid1Token() {
        return uid1Token;
    }

    public void setUid1Token(String uid1Token) {
        this.uid1Token = uid1Token;
    }

    public String getUid2Token() {
        return uid2Token;
    }

    public void setUid2Token(String uid2Token) {
        this.uid2Token = uid2Token;
    }
}
