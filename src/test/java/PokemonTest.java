

import static org.junit.Assert.*;

import jdk.jfr.Description;
import org.junit.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.sql2o.*;


public class PokemonTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  @Description("Testing if the instance of the Pokemon is of Pokemon. Testing a single unit => UnitTest")
  public void Pokemon_instantiatesCorrectly_true() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertEquals(true, myPokemon instanceof Pokemon);
  }

  @Test
  @Description("Testing if the name is well set of the pokemon. Testing single unit => UnitTest")
  public void getName_pokemonInstantiatesWithName_String() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertEquals("Squirtle", myPokemon.getName());
  }

  @Test
  @Description("Test if the initialisation of the Pokemon  list is empty. Testing 1 single component/Unit => UnitTest")
  public void all_emptyAtFirst() {
    assertEquals(Pokemon.all().size(), 0);
  }

  @Test
  @Description("Testing if two instances are the same with the same stats. Testing the equal methode => UnitTest")
  public void equals_returnsTrueIfPokemonAreTheSame_true() {
    Pokemon firstPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    Pokemon secondPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertTrue(firstPokemon.equals(secondPokemon));
  }

  @Test
  @Description("Testing the save function in the Pokedex and asserting that the Pokemon list is now at 1 => UnitTesting")
  public void save_savesPokemonCorrectly_1() {
    Pokemon newPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    newPokemon.save();
    assertEquals(1, Pokemon.all().size());
  }

  @Test
  @Description("Testing the selection querry and with the id of the pokemon. Testing 2 component  => IntegrityTest")
  public void find_findsPokemonInDatabase_true() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    Pokemon savedPokemon = Pokemon.find(myPokemon.getId());
    assertTrue(myPokemon.equals(savedPokemon));
  }

  @Test
  @Description("Saving a move to a pokemon and asserting it has been correctly set/saved in the move  array => UnitTest")
  public void addMove_addMoveToPokemon() {
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    myMove.save();
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    myPokemon.addMove(myMove);
    Move savedMove = myPokemon.getMoves().get(0);
    assertTrue(myMove.equals(savedMove));
  }

  @Test
  @Description("Adding moves and deleting them asserting the list of moves is empty => UnitTest")
  public void delete_deleteAllPokemonAndMovesAssociations() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    Move myMove = new Move("Bubble", "Water", 50.0, 100);
    myMove.save();
    myPokemon.addMove(myMove);
    myPokemon.delete();
    assertEquals(0, Pokemon.all().size());
    assertEquals(0, myPokemon.getMoves().size());
  }

  @Test
  @Description("Testing the search function with the database and comparing the resulting pokemon. Testing thus the integration of the search function => IntegrityTest")
  public void searchByName_findAllPokemonWithSearchInputString_List() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    assertEquals(myPokemon, Pokemon.searchByName("squir").get(0));
  }

  public static String[][] data() {
    return new String[][] { { "FIRE" , "ROCK","50","500","400" }, { "Grass" , "Ground","50" ,"12.25","500","400" },
            { "Electric" , "Ground","50" ,"2000","500","500" } };
  }

  @ParameterizedTest
  @MethodSource(value = "data")
  @Description("Testing the damages done after attacking.Here we are testing the Behaviour of the game and what the \"EndUser\" is expecting => AceptenceTest")
  public void fighting_damagesDefender(String[] data) {
    Pokemon myPokemon = new Pokemon("Squirtle", data[0], "Normal", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    myPokemon.hp = Integer.parseInt(data[3]);
    Move myMove = new Move("Bubble", data[1], Double.parseDouble(data[2]), 100);
    myMove.attack(myPokemon);
    System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
        System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
        System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
    assertEquals(Integer.parseInt(data[4]), myPokemon.hp);
  }

}
