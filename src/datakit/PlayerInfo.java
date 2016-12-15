package datakit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class PlayerInfo extends LinkedList<Player> implements EventDispatcher.Observer{

  @Override
  public void receiveNotify(String msg) {
    // TODO Auto-generated method stub
    // lack of message formats.
    List<String> msgs;

    HashMap< String, MsgHandler> handlerMap = new HashMap<>();
    handlerMap.put( "List",new MsgHandler(){
      @Override public void onGoing(List<String> msgs) { cmdList(msgs);} });
    
    handlerMap.put( "ListRemove", new MsgHandler(){
      @Override public void onGoing(List<String> msgs) { cmdListRemove(msgs);} });
    
    handlerMap.put( "ListAdd", new MsgHandler() {
      @Override public void onGoing(List<String> msgs) { cmdListAdd(msgs);} });
    
    msgs = new LinkedList<>( Arrays.asList(msg.split("#")));
    
    MsgHandler handler = handlerMap.get( msgs.get(0));
    msgs.remove(0);
    handler.onGoing(msgs);     
    
  }
  
  interface MsgHandler{
    void onGoing(List<String> msgs);
  }
  
  void cmdList(List<String> msgs){
    
    assert( msgs.size() % Player.buildParaNums == 0  ):"TCP !!!!";
    this.removeAll(this);
    
    for ( int i = 1; i < msgs.size(); i += Player.buildParaNums){
      
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
    
    assert( msgs.size() % Player.buildParaNumsNoBurger == 0  ):"TCP !!!!";
    
    this.add( new Player( msgs.get(0), msgs.get(1)));
  }

}
