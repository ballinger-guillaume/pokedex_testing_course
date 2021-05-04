
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.sql2o.*;
import org.junit.jupiter.api.Test;


public class MoveTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Move_instantiatesCorrectly_true() {
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    Assertions.assertTrue(myMove instanceof Move);
  }

  @Test
  public void getName_moveInstantiatesWithName_String() {
    Move myMove = new Move("Solar Beam", "Normal", 50.0, 100);
    Assertions.assertEquals("Solar Beam", myMove.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    Assertions.assertEquals(Move.all().size(), 1);
  }

  @Test
  public void equals_returnsTrueIfMovesAreTheSame_true() {
    Move firstMove = new Move("Punch", "Normal", 50.0, 100);
    Move secondMove = new Move("Punch", "Normal", 50.0, 100);
    Assertions.assertEquals(secondMove, firstMove);
  }

  @Test
  public void save_savesMoveCorrectly_1() {
    Move newMove = new Move("Punch", "Normal", 50.0, 100);
    newMove.save();
    Assertions.assertEquals(6, Move.all().size());
  }

  @Test
  public void find_findsMoveInDatabase_true() {
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    myMove.save();
    Move savedMove = Move.find(myMove.getId());
    Assertions.assertEquals(savedMove, myMove);
  }

  @Test
  public void accuracy_checksAccuracy_false() {
    Move myMove = new Move("Flail", "Normal", 100.0, 0);
    myMove.save();
    Assertions.assertFalse(myMove.hitCalculator());
  }

  @Test
  public void accuracy_checksAccuracy_true() {
    Move myMove = new Move("Flail", "Normal", 100.0, 100);
    myMove.save();
    Assertions.assertTrue(myMove.hitCalculator());
  }
  @Test
  public void effectiveness_test_works() {
    Move myMove = new Move("Flail", "Water", 100.0, 100);
    myMove.save();
    Pokemon otherPokemon = new Pokemon("Flaming Rock Pikachu", "Rock", "Fire", "A flaming rat", 50.0, 12, 16, false);
    Assertions.assertEquals(4, myMove.effectiveness(otherPokemon), 0);
  }

  @Test
  public void effectiveness_test_works_strongAgainstBoth_point25() {
    Move myMove = new Move("Flail", "Water", 100.0, 100);
    Pokemon otherPokemon = new Pokemon("Chia-Squirtle", "Water", "Grass", "A squirtle with chia-pet seeds on its shell", 50.0, 12, 16, false);
    Assertions.assertEquals(.25, myMove.effectiveness(otherPokemon), 0);
  }
  @Test
  public void attack_method_does_damage() {
    Move myMove = new Move("Punch", "Normal", 60.0, 100);
    Pokemon otherPokemon = new Pokemon("Vanilla pokemon", "Normal", "None", "a normal pokemon", 50.0, 12, 16, false);
    Assertions.assertEquals("The attack does 60,00 damage!", myMove.attack(otherPokemon));
  }

  @Test
  public void getPokemons_getPokemonFromMoveSearch() {
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    myMove.save();
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    myPokemon.addMove(myMove);
    Pokemon savedPokemon = myMove.getPokemons().get(0);
    Assertions.assertEquals(savedPokemon, myPokemon);
  }
}
