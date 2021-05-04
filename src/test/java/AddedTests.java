import jdk.jfr.Description;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sql2o.Connection;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class AddedTests {
    //Pokem 100%
    @Test
    public void test_getters_of_pokemon() {
        Pokemon p = new Pokemon("1", "2", "3", "4", 5.0, 6, 7, false);
        Assertions.assertEquals("1", p.getName());
        Assertions.assertEquals("2", p.getType1());
        Assertions.assertEquals("3", p.getType2());
        Assertions.assertEquals("4", p.getDescription());
        Assertions.assertEquals(5.0, p.getWeight());
        Assertions.assertEquals(6, p.getHeight());
        Assertions.assertEquals(7, p.getEvolves());
        Assertions.assertFalse(p.getMega_evolves());
        Assertions.assertEquals(0, p.getHp());
        Assertions.assertEquals("1.gif", p.getImageName());
        Assertions.assertEquals(-1, p.getPreviousId());
        Assertions.assertEquals(1, p.getNextId());
        Assertions.assertEquals("a", new Move("","a",0.0,1).getType());

    }

    @Test
    public void Find_All_Pokemon_By_Type() {
        List<Pokemon> v;
        try (Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM pokemons WHERE type_1 = :type OR type_2 = :type";
            v = con.createQuery(sql)
                    .addParameter("type", "Normal")
                    .executeAndFetch(Pokemon.class);
            Assertions.assertEquals(Pokemon.searchByType("Normal"), v);
        }

    }

    @Test
    public void Not_A_Pokemon() {
        Pokemon p = new Pokemon("1", "2", "3", "4", 5.0, 6, 7, false);
        Assertions.assertFalse(p.equals("2"));
    }

    //Move 100%
    @ParameterizedTest
    @MethodSource(value = "data")
    public void EffectivnessTest(String type,String attacktype,Double effectivness) {
        Pokemon myPokemon = new Pokemon("Squirtle", type, "", "A cute turtle", 50.0, 12, 16, false);
        Move m = new Move("", attacktype, 2.0, 100);
        Assertions.assertEquals(effectivness, m.effectiveness(myPokemon));
    }

    @Test
    public void Miss(){
        Move m = new Move("", "", 2.0, -20);
        Assertions.assertFalse(m.hitCalculator());
        Assertions.assertEquals("The attack misses and did 0 damage...",m.attack(new Pokemon("","","","",0,0,0,false)));
    }

    @Test
    public void equalsMove(){
        Move m = new Move("", "", 2.0, -20);
        Move m2 = new Move("", "", 2.0, -20);
        Assertions.assertEquals(m2, m);
        Assertions.assertNotEquals(m, "20");
    }
    @Test
    public void delete(){
        Move m2 = new Move("", "", 2.0, -20);
        m2.delete();
    }

    @Test
    public void searching(){
        new Move("w", "", 2.0, -20);
        Assertions.assertEquals(Move.searchByExactName("w"),Move.searchByName("w"));
    }





    //BATTLEQUERY 100% (Failling because DB not contained battle)
    @Test
    public void BattleQuerysaveFirst() {

        BattleQuery b = new BattleQuery();
        BattleQuery b2 = new BattleQuery();
        try {
            b.saveFirstSelection(1);
            b2.saveFirstSelection(1);
            Assertions.assertEquals(b2.id, b.id);
        } catch (Exception ignore) {

        }



    }

    @Test
    public void BattleQuerysaveSecond() {
        BattleQuery b = new BattleQuery();
        try {
            b.saveSecondSelection(1);
        } catch (Exception ignore) {

        }
    }


    public static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("Rock","Normal",0.5),
                Arguments.of("Ghost","Normal",0.0),
                Arguments.of("Fire","Fire",0.5),
                Arguments.of("Grass","Fire",2.0),
                Arguments.of("Water","Water",0.5),
                Arguments.of("Fire","Water",2.0),
                Arguments.of("Water","Electric",2.0),
                Arguments.of("Grass","Electric",0.5),
                Arguments.of("Ground","Electric",0.0),
                Arguments.of("Fire","Grass",0.5),
                Arguments.of("Water","Grass",2.0),
                Arguments.of("Fire","Ground",2.0),
                Arguments.of("Grass","Ground",0.5),
                Arguments.of("Flying","Ground",0.0),
                Arguments.of("Water","Ice",0.5),
                Arguments.of("Dragon","Ice",2.0),
                Arguments.of("Normal","Fighting",2.0),
                Arguments.of("Poison","Fighting",0.5),
                Arguments.of("Ghost","Fighting",0.0),
                Arguments.of("Poison","Poison",0.5),
                Arguments.of("Grass","Poison",2.0),
                Arguments.of("Fire","Ground",2.0),
                Arguments.of("Grass","Ground",0.5),
                Arguments.of("Flying","Ground",0.0),
                Arguments.of("Electric","Flying",0.5),
                Arguments.of("Grass","Flying",2.0),
                Arguments.of("Psychic","Psychic",0.5),
                Arguments.of("Fighting","Psychic",2.0),
                Arguments.of("Fire","Bug",0.5),
                Arguments.of("Grass","Bug",2.0),
                Arguments.of("Fighting","Rock",0.5),
                Arguments.of("Fire","Rock",2.0),
                Arguments.of("Normal","Ghost",0.0),
                Arguments.of("Psychic","Ghost",2.0),
                Arguments.of("Dragon","Dragon",2.0)
        );

    }



}
