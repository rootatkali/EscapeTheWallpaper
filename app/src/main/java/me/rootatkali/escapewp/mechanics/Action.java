package me.rootatkali.escapewp.mechanics;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Action {
  private final int id;
  private final int messageId;
  private final int score;
  private final String result;
  private final int nextNode;
  
  public Action(int id, int messageId, int score, String result, int nextNode) {
    this.id = id;
    this.messageId = messageId;
    this.score = score;
    this.result = result;
    this.nextNode = nextNode;
  }
  
  public int getId() {
    return id;
  }
  
  public int getMessageId() {
    return messageId;
  }
  
  public int getScore() {
    return score;
  }
  
  public String getResult() {
    return result;
  }
  
  public int getNextNode() {
    return nextNode;
  }
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("messageId", messageId)
        .add("score", score)
        .add("result", result)
        .add("nextNode", nextNode)
        .toString();
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Action action = (Action) o;
    return id == action.id &&
        messageId == action.messageId &&
        score == action.score &&
        Objects.equals(result, action.result) &&
        nextNode == action.nextNode;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id, messageId, score, result, nextNode);
  }
}
