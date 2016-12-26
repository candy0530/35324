package data;
import character.Character;

public class UserInfo {

  int id;
  String ip;
  String name;
  Character character;
  
  
  public UserInfo(String name) {
    super();
    this.name = name;
  }


  public int getId() {
    return id;
  }


  public void setId(int id) {
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


public String getName() {
    return name;
}
  
  
}
