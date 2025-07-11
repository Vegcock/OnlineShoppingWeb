public class Customer {
    private String userId;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String vip;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public Customer(String userId, String name, String phone, String email, String address, String vip) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.vip = vip;
    }

    @Override
    public String toString() {
        return userId + " | " + name + " | " + phone + " | " + email + " | " + address + " | " + vip;
    }
}
