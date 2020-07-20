package org.redouane59.topteamsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class PlayerTest {

  private final ObjectMapper MAPPER      = new ObjectMapper();

  @Test
  public void testDeserializeWithoutPosition() throws JsonProcessingException {
    final String playerJson = "{\"id\":\"player1\",\"rating_value\":77.0,\"nb_games_played\":4}";
    Player       player      = MAPPER.readValue(playerJson, Player.class);
      assertEquals("player1",player.getId());
      assertEquals(77,player.getRatingValue());
      assertEquals(4,player.getNbGamesPlayed());
      assertNull(player.getPosition());
  }

  @Test
  public void testDeserializeWithoutGames() throws JsonProcessingException {
    final String playerJson = "{\"id\":\"player1\",\"rating_value\":77.0, \"position\":\"GK\"}";
    Player       player      = MAPPER.readValue(playerJson, Player.class);
    assertEquals("player1",player.getId());
    assertEquals(77,player.getRatingValue());
    assertEquals(PlayerPosition.GK, player.getPosition());
    assertEquals(0,player.getNbGamesPlayed());
  }
}
