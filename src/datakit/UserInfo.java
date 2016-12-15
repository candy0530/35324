package datakit;

public class UserInfo {

  String id;
  String ip;
  String name;
  Character character;
  
  
  public UserInfo(String name) {
    super();
    this.name = name;
  }


  public String getId() {
    return id;
  }


  public void setId(String id) {
    this.id = id;
  }


  public String getIp() {
    return ip;
  }


  public void setIp(String ip) {
    this.ip = ip;
  }


  public Character getCharacter() {
    return character;
  }


  public void setCharacter(Character character) {
    this.character = character;
  }
  
  
}
