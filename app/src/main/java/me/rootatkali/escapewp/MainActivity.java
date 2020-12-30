package me.rootatkali.escapewp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Arrays;
import java.util.Map;

import me.rootatkali.escapewp.mechanics.Action;
import me.rootatkali.escapewp.mechanics.Node;

public class MainActivity extends AppCompatActivity {
  private int score;
  private Map<Integer, Node> nodes;
  private SharedPreferences prefs;
  private Node current;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    initializeNodes();
    prefs = getPreferences(Context.MODE_PRIVATE);
    score = prefs.getInt(getString(R.string.score_key), 0);
    current = nodes.get(prefs.getInt(getString(R.string.current_node_key), 0));
    
  }
  
  private void initializeNodes() {
    Node head = new Node(0, "You started Escape. Your eyes are closed.");
    Node n1 = new Node(1, "You are in a dark room.\n" +
        "There is some light coming from the window. You see a light switch.");
    
    head.addOptions(new Action(0, R.string.n0a0, 0, 1));
    
    nodes.put(0, head);
    nodes.put(1, n1);
  }
  
  private Node next(int optionId) {
    Action action = Arrays.stream(current.getOptions()).filter(a -> a.getId() == optionId)
        .findFirst().orElseThrow(RuntimeException::new);
    score += action.getScore();
    Node next = nodes.get(action.getNextNode());
    
    prefs.edit()
        .putInt(getString(R.string.score_key), score)
        .putInt(getString(R.string.current_node_key), next.getId())
        .apply();
  
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    return next;
  }
}