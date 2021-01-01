package me.rootatkali.escapewp.mechanics;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Node {
  private final int id;
  private final String message;
  private final List<Action> options;
  
  public Node(int id, String message) {
    this.id = id;
    this.message = message;
    options = new ArrayList<>();
  }
  
  public Action[] getOptions() {
    return options.toArray(new Action[0]);
  }
  
  public Node addOptions(Action... actions) {
    options.addAll(Arrays.asList(actions));
    return this;
  }
  
  public int getId() {
    return id;
  }
  
  public String  getMessage() {
    return message;
  }
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("messageId", message)
        .add("options", options)
        .toString();
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node node = (Node) o;
    return id == node.id &&
        Objects.equals(message, node.message) &&
        Objects.equals(options, node.options);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id, message, options);
  }
}
