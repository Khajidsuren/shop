package mn.shop.email;

public class MailDto {

    private String phoneNumber;

    private String suggest;

    public MailDto(String phoneNumber, String suggest) {
        this.phoneNumber = phoneNumber;
        this.suggest = suggest;
    }

    public MailDto() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSuggest() {
        return suggest;
    }
}
