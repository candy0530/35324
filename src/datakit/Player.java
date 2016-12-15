package datakit;

public class Player {


  final static public int buildParaNums = 3;
  final static public int buildParaNumsNoBurger = 2;
  String name;
  String ip;
  int burger = 0;

  public Player(String name, String ip) {
    super();
    this.name = name;
    this.ip = ip;
  }
  
  public Player(String name, String ip, String burger) {
    
    this(name, ip);
    this.burger = Integer.parseInt( burger);
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getIp() {
    return ip;
  }
  public void setIp(String ip) {
    this.ip = ip;
  }
  public int getBurger() {
    return burger;
  }
  public void setBurger(int burger) {
    this.burger = burger;
  }
  
}
