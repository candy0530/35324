package datakit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import EventDispatcher.Observer;;


public class PlayerInfo extends LinkedList<Player> implements Observer{
  
  
  HashMap< String, MsgHandler> handlerMap;
  
  public PlayerInfo() {
    
    super();
    handlerMap = new HashMap<>();
    
    HashMap< String, MsgHandler> handlerMap = new HashMap<>();
    handlerMap.put( "List",new MsgHandler(){
      @Override public void execute(List<String> msgs) { cmdList(msgs);} });
    
    handlerMap.put( "ListRemove", new MsgHandler(){
      @Override public void execute(List<String> msgs) { cmdListRemove(msgs);} });
    
    handlerMap.put( "ListAdd", new MsgHandler() {
      @Override public void execute(List<String> msgs) { cmdListAdd(msgs);} });    
  }

  @Override
  public void receiveNotify(String msg) {
    // TODO Auto-generated method stub
    // lack of message formats.
    List<String> msgs;
    
    msgs = new LinkedList<>( Arrays.asList(msg.split("#")));
    
    MsgHandler handler = handlerMap.get( msgs.get(0));
    msgs.remove(0);
    handler.execute(msgs);     
    
  }
  
  interface MsgHandler{
    void execute(List<String> msgs);
  }
  
  void cmdList(List<String> msgs){
    
    assert( msgs.size() % Player.ctorParaNums == 0  ):"TCP !!!!";
    this.removeAll(this);
    
    for ( int i = 0; i < msgs.size(); i += Player.ctorParaNums){
      
      this.add( new Player( msgs.get(i), msgs.get( i+1), msgs.get( i+2)));
    }    
  }
  
  void cmdListRemove(List<String> msgs){
    
    //compare with the player IP to determine the target.
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
