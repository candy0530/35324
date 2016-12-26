package data;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import background.Observer;


public class PlayerInfo extends LinkedList<Player> implements Observer{

  @Override
  public void receiveNotify(String msg) {
    // TODO Auto-generated method stub
    // lack of message formats.
     //System.out.println("1123"+msg);
    List<String> msgs;
    String[] headers = {"List","ListRemove","ListAdd"};
    MsgHandler[] handler = new MsgHandler[]{
        
      new MsgHandler() {  @Override public void onGoing(List<String> msgs) { cmdList(msgs);} },
      new MsgHandler() {  @Override public void onGoing(List<String> msgs) { cmdListRemove(msgs);} },
      new MsgHandler() {  @Override public void onGoing(List<String> msgs) { cmdListAdd(msgs);} },
    };
    
    msgs = new LinkedList<>( Arrays.asList(msg.split("#")));
    
    for ( int i =0; i < headers.length; i++){
      
      if ( msgs.get(0).equals( headers[i])){
        
        msgs.remove(0);
        handler[i].onGoing(msgs);
        break;
      }
    }
    
  }
  
  interface MsgHandler{
    void onGoing(List<String> msgs);
  }
  
  void cmdList(List<String> msgs){
    
    assert( msgs.size() % Player.ctorParaNums == 0  ):"TCP !!!!";
    this.removeAll(this);
    
    for ( int i = 0; i < msgs.size(); i += Player.ctorParaNums){
      
      this.add( new Player( msgs.get(i), msgs.get( i+1), msgs.get( i+2)));
    }    
  }
  
  void cmdListRemove(List<String> msgs){
    
    assert( msgs.size() == 1  ):"TCP !!!!";
    java.util.Iterator<Player> iterator = this.iterator();
    
    while( iterator.hasNext()){
      
      Player p = iterator.next();
      if ( p.getIp().equals( msgs.get(0)) ){
        this.remove(p);
        break;
      }
    }
  }
  
  void cmdListAdd(List<String> msgs){
    
    assert( msgs.size() % Player.ctorParaNumsNoBurger == 0  ):"TCP !!!!";
    
    this.add( new Player( msgs.get(0), msgs.get(1)));
  }

}
