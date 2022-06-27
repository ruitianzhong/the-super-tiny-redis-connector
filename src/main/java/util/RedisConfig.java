package util;

public class RedisConfig {
    private String password;
    private String username;
    public RedisConfig(String username,String password){
        this.password=password;
        this.username=username;
    }
    public RedisConfig(String password){
        this.password=password;
        this.username=null;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
