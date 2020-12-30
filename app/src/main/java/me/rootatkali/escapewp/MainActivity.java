package me.rootatkali.escapewp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import me.rootatkali.escapewp.mechanics.Action;
import me.rootatkali.escapewp.mechanics.Node;

public class MainActivity extends AppCompatActivity {
  private int score;
  private Map<Integer, Node> nodes;
  private SharedPreferences prefs;
  private Node current;
  private TextView lblText;
  private LinearLayout vbxButtons;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    lblText = findViewById(R.id.lblText);
    vbxButtons = findViewById(R.id.vbxButtons);
    
    lblText.setMovementMethod(new ScrollingMovementMethod());
    
    initializeNodes();
    prefs = getPreferences(Context.MODE_PRIVATE);
    score = prefs.getInt(getString(R.string.score_key), 0);
    current = nodes.get(prefs.getInt(getString(R.string.current_node_key), 0));
    game();
  }
  
  private void game() {
    lblText.append(current.getMessage());
    lblText.append("\n");
    vbxButtons.removeAllViews();
    for (Action a : current.getOptions()) {
      Button btn = new Button(this);
      btn.setText(a.getMessageId());
      btn.setOnClickListener(v -> {
        if (a.getResult() != null) {
          lblText.append(Html.fromHtml(
              String.format("<i>%s</i>", a.getResult()),
              HtmlCompat.FROM_HTML_MODE_LEGACY
          ));
          lblText.append("\n");
        }
        current = next(a.getId());
        game();
      });
      vbxButtons.addView(btn, new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
      ));
    }
  }
  
  private void initializeNodes() {
    nodes = new HashMap<>();
    
    Node n0 = new Node(0, "You started Escape. Your eyes are closed.")
        .addOptions(new Action(0, R.string.n0a0, 0, "You open your eyes.", 1));
    Node n1 = new Node(1, "You are in a dark room.\n" +
        "There is some light coming from the window. You see a light switch.")
        .addOptions(new Action(0, R.string.n1a0, 5, "You flick the switch.", 2));
    Node n2 = new Node(2, "There is a bed in the room. How didn't you see it?")
        .addOptions(new Action(0, R.string.ellipsis, -1, null, 3));
    Node n3 = new Node(3, "You look around the room." +
        " There is a small desk with yellowing paperwork on it." +
        " On the walls of the room, a horrible yellow wallpaper is plastered." +
        " It seems to want to fall off.")
        .addOptions(new Action(0, R.string.ellipsis, 0, null, 4));
    Node n4 = new Node(4, "You see an old, seemingly flimsy closed wooden door.")
        .addOptions(
            new Action(0, R.string.n4a0, 1, "You try to open the door.", 5),
            new Action(1, R.string.n4a1, 6, "You go to the desk.", 6)
        );
    Node n5 = new Node(5, "The door is locked.")
        .addOptions(new Action(0, R.string.n4a1, 5, "You go to the desk.", 6));
    Node n6 = new Node(6, "You see the old papers.\n" +
        "There is a drawer. It appears to not be locked.")
        .addOptions(
            new Action(0, R.string.n6a0, 0, "You open the drawer.", 8),
            new Action(0, R.string.n6a1, 0, "You look at the papers.", 7)
        );
    
    nodes.put(n0.getId(), n0);
    nodes.put(n1.getId(), n1);
    nodes.put(n2.getId(), n2);
    nodes.put(n3.getId(), n3);
    nodes.put(n4.getId(), n4);
    nodes.put(n5.getId(), n5);
    nodes.put(n6.getId(), n6);
  }
  
  private Node next(int optionId) {
    Action action = Arrays.stream(current.getOptions())
        .filter(a -> a.getId() == optionId)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
    score += action.getScore();
    Node next = Optional.ofNullable(nodes.get(action.getNextNode()))
        .orElseThrow(UnsupportedOperationException::new);
    
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