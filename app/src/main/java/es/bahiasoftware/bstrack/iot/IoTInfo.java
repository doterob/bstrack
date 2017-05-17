package es.bahiasoftware.bstrack.iot;

public class IoTInfo {

    private final String userId;
    private final String token;
    private final String phone;

    public IoTInfo(String userId, String token,String phone){
        this.userId = userId;
        this.token = token;
        this.phone = phone;
    }

    public String getUserId(){
        return userId;
    }

    public String getToken(){
        return token;
    }

    public String getPhone() { return phone; }
}
