package me.rootatkali.escapewp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Context;
import android.content.SharedPreferences;
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
    lblText.append(Html.fromHtml(
        current.getMessage(), HtmlCompat.FROM_HTML_MODE_LEGACY
    ));
    lblText.append("\n");
    vbxButtons.removeAllViews();
    
    // Finish scenario
    if (current.getOptions() == null || current.getOptions().length <= 0) {
      lblText.append("\n");
      lblText.append("The game has ended. Your score was ");
      lblText.append(String.valueOf(score));
      lblText.append(".\n\n");
      
      Button btnReplay = new Button(this);
      btnReplay.setText(R.string.btn_replay);
      btnReplay.setOnClickListener(v -> {
        prefs.edit().clear().apply();
        current = nodes.get(0);
        score = 0;
        lblText.setText("");
        game();
      });
      
      vbxButtons.addView(btnReplay, new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
      ));
      
      Button btnCredits = new Button(this);
      btnCredits.setText(R.string.btn_credits);
      btnCredits.setOnClickListener(v -> {
        lblText.setText("Escape the Wallpaper\n\nCreated by Rotem Moses.\n\n\n");
      });
      vbxButtons.addView(btnCredits, new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
      ));
      
      // Regular game
    } else for (Action a : current.getOptions()) {
      Button btn = new Button(this);
      btn.setText(a.getMessageId());
      btn.setOnClickListener(v -> {
        if (!(a.getResult() == null || a.getResult().isEmpty())) {
          lblText.append(Html.fromHtml(
              String.format("<i>%s</i>", a.getResult()),
              HtmlCompat.FROM_HTML_MODE_LEGACY
          ));
          lblText.append("\n");
        }
        current = next(a);
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
            new Action(0, R.string.n6a0, 5, "You open the drawer.", 8),
            new Action(1, R.string.n6a1, 0, "You look at the papers.", 7)
        );
    Node n7 = new Node(7, "The paper reads as follows:\n" +
        "<i>I do not know for how long I have been here. I only know I want to leave. " +
        "These walls are closing on me. " +
        "I don’t know for how much longer I will be able to keep my head up here.</i>\n" +
        "The rest of the papers are illegible.")
        .addOptions(
            new Action(0, R.string.n6a0, 0, "You open the drawer.", 8),
            new Action(0, R.string.ellipsis, 0, "You sit on the bed.", 9)
        );
    Node n8 = new Node(8, "There is an old key in the drawer.")
        .addOptions(
            new Action(0, R.string.n8a0, 10, "You grab the key. " +
                "The key does not fit the door.\n" +
                "You sit on the bed.", 9),
            new Action(1, R.string.ellipsis, 0, "You sit on the bed.", 9)
        );
    Node n9 = new Node(9, "You hear footsteps outside the door.")
        .addOptions(
            new Action(0, R.string.n9a0, -3, "You: AARGH!", 100),
            new Action(1, R.string.n9a1, 3, "You stiffen.", 200)
        );
    
    Node n100 = new Node(100, "Man: Who's yelling there?")
        .addOptions(
            new Action(0, R.string.n100a0, -2, "You: Get me out of here!", 110),
            new Action(1, R.string.n100a1, 1, "You: Where am I?", 120)
        );
    Node n110 = new Node(110, "Man: Oh. You again.").addOptions(
        new Action(0, R.string.ellipsis, 0, "", 111)
    );
    Node n111 = new Node(111, "Man: When will you learn to stop yelling so much?")
        .addOptions(
            new Action(0, R.string.ellipsis, 0, null, 112)
        );
    Node n112 = new Node(112, "Man: It disturbs everyone here. Anyways, I was about " +
        "to bring you your medicines for today.")
        .addOptions(
            new Action(0, R.string.ellipsis, 0, null, 114),
            new Action(1, R.string.n112a1, -1, "You: Who are you?", 113)
        );
    Node n113 = new Node(113, "Man: This shouldn't concern you.")
        .addOptions(
            new Action(0, R.string.ellipsis, 0, "You: ...", 114)
        );
    Node n114 = new Node(114, "The door opens. A tall man with a tray comes in. " +
        "He places the tray on the desk.")
        .addOptions(
            new Action(0, R.string.ellipsis, 0, null, 115)
        );
    Node n115 = new Node(115, "Man: Take these now. Or else.")
        .addOptions(
            new Action(0, R.string.n115a0, -10, "You: Or else what?!", 116),
            new Action(1, R.string.n115a1, 0, "You take the pills.", 117)
        );
    Node n116 = new Node(116, "Man: Don't try me. I've had enough of you already.")
        .addOptions(
            new Action(0, R.string.n115a1, 0, "You take the pills.", 117)
        );
    Node n117 = new Node(117, "Everything is black. You will not wake up.");
    // END 110
    
    Node n120 = new Node(120, "Man: I guess I need to explain this again.")
        .addOptions(
            new Action(0, R.string.n120a0, 2, "You: Explain what?", 121),
            new Action(1, R.string.ellipsis, 0, "You: ...", 122)
        );
    Node n121 = new Node(121, "Man: You will understand everything later.")
        .addOptions(
            new Action(0, R.string.ellipsis, 0, null, 122)
        );
    Node n122 = new Node(122, "The door opens. A tall man with a tray comes in. " +
        "He places the tray on the desk.")
        .addOptions(
            new Action(0, R.string.ellipsis, 0, null, 123)
        );
    Node n123 = new Node(123, "Man: These are for you. Take them before your... " +
        "condition... gets worse.")
        .addOptions(
            new Action(0, R.string.n123a0, -1, "You take the pills.", 125),
            new Action(1, R.string.n123a1, 1, "You: What is my condition?", 124)
        );
    Node n124 = new Node(124, "Man: Your condition is ought not to be a concern if " +
        "you cooperate. Now take the medicine.")
        .addOptions(
            new Action(0, R.string.n123a0, 0, "You take the pills.", 125)
        );
    // TODO Continue plot 120
    
    Node n200 = new Node(200, "The footsteps are getting closer.")
        .addOptions(
            new Action(0, R.string.ellipsis, +1, null, 220),
            new Action(1, R.string.n200a0, -1, "You hide behind the bed.", 210)
        );
    // TODO Write plots 210 and 220
    
    putNodes(
        n0, n1, n2, n3, n4, n5, n6, n7, n8, n9,
        n100,
        n110, n111, n112, n113, n114, n115, n116, n117,
        n120, n121, n122, n123, n124,
        n200
    );
  }
  
  private void putNodes(Node... nds) {
    for (Node n : nds) {
      this.nodes.put(n.getId(), n);
    }
  }
  
  // Saves game changes to SharedPreferences and calculates next node
  private Node next(Action action) {
    score += action.getScore();
    Node next = Optional.ofNullable(nodes.get(action.getNextNode()))
        .orElseThrow(IllegalArgumentException::new);
    
    // Save game changes
    prefs.edit()
        .putInt(getString(R.string.score_key), score)
        .putInt(getString(R.string.current_node_key), next.getId())
        .apply();
    
    return next;
  }
  
  private Action getAction(int node, int action) {
    return Arrays.stream(Optional.ofNullable(nodes.get(node)).orElseThrow(IllegalArgumentException::new)
        .getOptions()).filter(a -> a.getId() == action).findAny().orElseThrow(IllegalArgumentException::new);
  }
}