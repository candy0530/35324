package character;

public class Ghost extends Character{
  
  public Ghost() {
    
   this.setSpeed( this.getSpeed() * 1.2);
   this.setId(0);
   for ( int i=0; i < itemNums; i++){
     
     assert itemStatus[i] == true: "Ghost item init failed...";
     this.removeItem(i);
   }
  }

  private Ghost(int id) {
    return;
  }

  @Override
  public void loadImg() {
    assert(false):"What are you doing.....";
    return;
  }    

}
